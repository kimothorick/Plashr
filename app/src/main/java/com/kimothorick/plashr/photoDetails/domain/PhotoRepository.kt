package com.kimothorick.plashr.photoDetails.domain

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.models.photo.DownloadPhotoResponse
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.photo.PhotoCollectionResult
import com.kimothorick.plashr.data.models.photo.PhotoLikeResponse
import com.kimothorick.plashr.data.remote.PhotoDataService
import com.kimothorick.plashr.utils.toFormattedDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Represents the different states for the Photo details screen UI.
 * This sealed class ensures that the UI can only be in one of these well-defined states at a time,
 * making state management more robust and predictable.
 */
sealed class PhotoUiState {
    /**
     * Represents the loading state of the photo details UI.
     * This is emitted while the photo data is being fetched from the remote source.
     */
    object Loading : PhotoUiState()

    /**
     * Represents the successful state of fetching photo details.
     * This state is emitted when the photo data has been successfully retrieved from the API.
     *
     * @param photo The [Photo] object containing all the details about the image.
     * @param formattedDate The creation date of the photo, formatted as a human-readable string.
     */
    data class Success(
        val photo: Photo,
        val formattedDate: String,
    ) : PhotoUiState()

    /**
     * Represents the error state of the photo details UI.
     * This state is emitted when an error occurs during an API call,
     * such as failing to fetch photo details.
     *
     * @property errorMessage A user-friendly message describing the error.
     */
    data class Error(
        val errorMessage: String,
    ) : PhotoUiState()
}

/**
 * A repository responsible for fetching and managing photo-related data.
 *
 * This class acts as a single source of truth for photo details. It communicates with the
 * [PhotoDataService] to perform network operations, handles the resulting data or errors,
 * and exposes the current state via a [StateFlow] of [PhotoUiState].
 *
 * @property photoDataService The remote data source for photo operations.
 * @property errorHandler A utility to convert exceptions into user-friendly error messages.
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
@Singleton
class PhotoRepository @Inject constructor(
    private val photoDataService: PhotoDataService,
    private val errorHandler: ErrorHandler,
    private val crashlytics: FirebaseCrashlytics,
) {
    private val _photoUiState = MutableStateFlow<PhotoUiState>(PhotoUiState.Loading)
    val photoUiState: StateFlow<PhotoUiState> = _photoUiState.asStateFlow()

    /**
     * Sets the current state of the photo details UI.
     *
     * This function is used to manually update the [_photoUiState] Flow,
     * which is useful for scenarios where the UI state needs to be changed
     * from outside the regular data fetching flow, such as when navigating
     * away and needing to reset the state.
     *
     * @param photoUiState The new state to be set for the photo details UI.
     * It can be one of [PhotoUiState.Loading], [PhotoUiState.Success], or [PhotoUiState.Error].
     */
    fun setPhotoUiState(
        photoUiState: PhotoUiState,
    ) {
        _photoUiState.value = photoUiState
    }

    /**
     * Fetches the details of a single photo from the data service.
     *
     * This function initiates a network request to retrieve photo data. It emits different
     * [PhotoUiState] values to represent the state of the operation:
     * - [PhotoUiState.Loading] is emitted immediately to indicate the fetch has started.
     * - [PhotoUiState.Success] is emitted if the photo is fetched successfully, containing the
     *   [Photo] object and its formatted creation date.
     * - [PhotoUiState.Error] is emitted if the request fails, the response is unsuccessful,
     *   or any other exception occurs, containing a user-friendly error message.
     *
     * @param photoId The unique identifier of the photo to retrieve.
     * @return A [Flow] that emits the current [PhotoUiState] of the photo fetch operation.
     */
    fun getPhoto(
        photoId: String,
    ): Flow<PhotoUiState> =
        flow {
            _photoUiState.value = PhotoUiState.Loading
            try {
                val result = photoDataService.getPhoto(id = photoId)
                if (result.isSuccessful) {
                    result.body()?.let { photo ->
                        emit(
                            PhotoUiState.Success(
                                photo = photo,
                                formattedDate = photo.createdAt?.toFormattedDate() ?: "",
                            ),
                        )
                    } ?: emit(
                        PhotoUiState.Error(
                            errorHandler.getErrorMessage(HttpException(result)),
                        ),
                    )
                } else {
                    val httpException = HttpException(result)
                    crashlytics.recordException(httpException)
                    emit(
                        PhotoUiState.Error(
                            errorHandler.getErrorMessage(httpException),
                        ),
                    )
                }
            } catch (exception: Exception) {
                crashlytics.recordException(exception)
                emit(
                    PhotoUiState.Error(
                        errorMessage = errorHandler.getErrorMessage(exception),
                    ),
                )
            }
        }

    /**
     * Likes a photo on behalf of the current user.
     *
     * This function requires a `write_likes` scope. It makes a network request
     * to the `photoDataService` to like a specific photo identified by its ID.
     *
     * @param photoId The ID of the photo to like.
     * @return A [Response] containing a [PhotoLikeResponse] on success. The response
     * will include the liked photo and user details.
     */
    suspend fun likePhoto(
        photoId: String,
    ): Response<PhotoLikeResponse> = photoDataService.likePhoto(photoId)

    /**
     * Dislikes a photo. This action requires the `write_likes` scope.
     *
     * @param photoId The ID of the photo to dislike.
     * @return A [Response] object. A successful response will have a 204 No Content status code.
     */
    suspend fun dislikePhoto(
        photoId: String,
    ): Response<Void> = photoDataService.unlikePhoto(photoId)

    /**
     * Triggers a photo download event on the Unsplash API.
     * This is a required step before a photo can be downloaded, as per the Unsplash API guidelines.
     * It provides a temporary URL for the download.
     *
     * @param photoId The ID of the photo to trigger the download for.
     * @return A [Response] containing the [DownloadPhotoResponse] which includes the download URL.
     */
    suspend fun triggerPhotoDownload(
        photoId: String,
    ): Response<DownloadPhotoResponse> = photoDataService.triggerPhotoDownload(photoId)

    /**
     * Adds a photo to a user's collection.
     *
     * This function calls the data service to perform the API request.
     *
     * @param photoId The ID of the photo to add.
     * @param collectionId The ID of the collection to add the photo to.
     * @return A [Response] object containing the [PhotoCollectionResult] on success.
     */
    suspend fun addPhotoToCollection(
        photoId: String,
        collectionId: String,
    ): Response<PhotoCollectionResult> {
        val result = photoDataService.addPhotoToCollection(collectionId = collectionId, photoId = photoId)
        return result
    }

    /**
     * Removes a photo from a collection. This is a suspending function that makes a network request.
     *
     * @param photoId The ID of the photo to be removed.
     * @param collectionId The ID of the collection from which the photo will be removed.
     * @return A [Response] object containing the result of the operation as a [PhotoCollectionResult].
     */
    suspend fun removePhotoFromCollection(
        photoId: String,
        collectionId: String,
    ): Response<PhotoCollectionResult> {
        val result = photoDataService.removePhotoFromCollection(collectionId = collectionId, photoId = photoId)
        return result
    }
}
