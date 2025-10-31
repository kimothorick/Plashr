package com.kimothorick.plashr.photoDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.collections.presentation.components.CreateCollectionState
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.models.photo.PhotoCollectionResult
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.photoDetails.domain.PhotoRepository
import com.kimothorick.plashr.profile.domain.userCollection.UserCollectionRepository
import com.kimothorick.plashr.utils.DownloadHandler
import com.kimothorick.plashr.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "PhotoActionsViewModel"

/**
 * Represents the various states of a photo download process.
 * This sealed class is used to model the lifecycle of a download, from idle to completion.
 */
sealed class DownloadStatus {
    /**
     * Represents the initial or reset state where no download operation is active.
     */
    object Idle : DownloadStatus()

    /**
     * Represents the state where a download is currently in progress.
     *
     * @param id The unique identifier for the ongoing download, typically provided by the download manager.
     */
    data class InProgress(
        val id: Long,
    ) : DownloadStatus()

    /**
     * Represents the completion state of a download.
     *
     * @param success True if the download was successful, false otherwise.
     * @param message An optional message providing more details about the download completion,
     * such as an error description or a success confirmation.
     */
    data class Complete(
        val success: Boolean,
        val message: String? = null,
    ) : DownloadStatus()
}

/**
 * Represents the type of action performed on a photo within a collection.
 * This is used to differentiate between adding a photo to a collection and removing it,
 * which helps in updating the UI and handling success or error states accordingly.
 */
enum class CollectPhotoType {
    Add,
    Remove,
}

/**
 * Represents the different states of a like/dislike operation on a photo.
 * This sealed class is used to model the UI state for the like action,
 * allowing for distinct representations of idle, loading, success, and error states.
 */
sealed class LikeStatus {
    /**
     * Represents the initial or default state, where no like or dislike action has been initiated.
     */
    object Idle : LikeStatus()

    /**
     * Represents the state where a like or dislike action is currently in progress.
     */
    object Loading : LikeStatus()

    /**
     * Represents a successful like or dislike operation.
     *
     * @param message An optional message describing the success, e.g., "Photo liked successfully".
     * @param isDislike A boolean flag to indicate if the successful operation was a dislike (`true`) or a like (`false`).
     */
    data class Success(
        val message: String? = null,
        val isDislike: Boolean,
    ) : LikeStatus()

    /**
     * Represents the error state of a like/dislike operation.
     *
     * @property message An optional string containing details about the error.
     */
    data class Error(
        val message: String? = null,
    ) : LikeStatus()
}

/**
 * Represents the different states of a photo collection operation (adding or removing a photo from a user's collection).
 * This is used to reflect the state of the operation in the UI.
 */
sealed class PhotoCollectionState {
    /**
     * Represents the initial or resting state, where no collection-related operation
     * (add/remove) is in progress.
     */
    object Idle : PhotoCollectionState()

    /**
     * Represents the state where a photo is currently being added to or removed from a collection.
     * This state is active during the network request.
     *
     * @property collectionId The ID of the collection involved in the operation.
     */
    data class InProgress(
        val collectionId: String,
    ) : PhotoCollectionState()

    /**
     * Represents a successful state for a photo collection operation (add/remove).
     *
     * @param message An optional message describing the success, e.g., "Photo collected successfully".
     * @param collectPhotoType The type of collection operation performed, either [CollectPhotoType.Add] or [CollectPhotoType.Remove].
     * @param photoCollectionResult The result object from the API call, containing details about the photo and collection.
     */
    data class Success(
        val message: String? = null,
        val collectPhotoType: CollectPhotoType,
        val photoCollectionResult: PhotoCollectionResult?,
    ) : PhotoCollectionState()

    /**
     * Represents the state where an error occurred during a photo collection operation (add/remove).
     *
     * @property collectPhotoType The type of collection operation that failed (either [CollectPhotoType.Add] or [CollectPhotoType.Remove]).
     * @property error The [Throwable] that was caught during the operation.
     * @property message A user-friendly error message, if available.
     */
    data class Error(
        val collectPhotoType: CollectPhotoType,
        val error: Throwable,
        val message: String? = null,
    ) : PhotoCollectionState()
}

/**
 * A [ViewModel] for handling user actions on a photo, such as liking, downloading,
 * and managing collections. It orchestrates interactions between the UI, repositories,
 * and utility services like the download handler.
 *
 * This class exposes various [StateFlow]s to the UI, allowing it to reactively observe
 * the status of asynchronous operations like:
 * - [likeStatus]: The state of a like/dislike action.
 * - [downloadStatus]: The progress and result of a photo download.
 * - [photoCollectionState]: The state of adding/removing a photo from a user's collection.
 * - [createCollectionState]: The status of creating a new collection.
 *
 * It also manages the visibility of UI components like bottom sheets for collecting photos
 * and creating new collections.
 *
 * @param photoRepository Repository for photo-specific actions (like, download trigger, add/remove from collection).
 * @param downloadHandler Utility for managing Android's `DownloadManager`.
 * @param userCollectionsRepository Repository for user-specific collection data (fetching and creating).
 * @param errorHandler Utility for parsing exceptions into user-friendly messages.
 * @param crashlytics Service for logging non-fatal exceptions for monitoring.
 */
@HiltViewModel
class PhotoActionsViewModel
    @Inject constructor(
        private val photoRepository: PhotoRepository,
        private val downloadHandler: DownloadHandler,
        private val userCollectionsRepository: UserCollectionRepository,
        private val errorHandler: ErrorHandler,
        private val crashlytics: FirebaseCrashlytics,
    ) : ViewModel() {
        private val _downloadStatus = MutableStateFlow<DownloadStatus>(DownloadStatus.Idle)
        val downloadStatus: StateFlow<DownloadStatus> = _downloadStatus
        private val _likeStatus = MutableStateFlow<LikeStatus>(LikeStatus.Idle)
        val likeStatus: StateFlow<LikeStatus> = _likeStatus.asStateFlow()
        private var isLiking = false
        private val _photoCollectionState = MutableStateFlow<PhotoCollectionState>(PhotoCollectionState.Idle)
        val photoCollectionState: StateFlow<PhotoCollectionState> = _photoCollectionState.asStateFlow()
        private var isCollecting = false

        /**
         * Adds a photo to a specified collection.
         *
         * This function initiates an asynchronous operation to add a photo to a collection.
         * It updates the [photoCollectionState] to reflect the progress, success, or failure of the operation.
         * If another collection operation is already in progress, this request will be ignored.
         *
         * @param photoId The unique identifier of the photo to be added.
         * @param collectionId The unique identifier of the collection to which the photo will be added.
         */
        fun addPhotoToCollection(
            photoId: String,
            collectionId: String,
        ) {
            if (isCollecting) {
                LogUtil.log(
                    keyName = TAG,
                    value = "Already collecting photo. Ignoring request.",
                    type = LogUtil.LogType.WARN,
                )
                return
            }
            viewModelScope.launch {
                isCollecting = true
                _photoCollectionState.update { PhotoCollectionState.InProgress(collectionId) }

                try {
                    val result = photoRepository.addPhotoToCollection(photoId, collectionId)

                    _photoCollectionState.update {
                        PhotoCollectionState.Success(
                            message = "Photo collected successfully",
                            CollectPhotoType.Add,
                            photoCollectionResult = result.body(),
                        )
                    }
                    LogUtil.log(
                        keyName = TAG,
                        value = "Photo collected successfully. Response: $result ${
                            result.body()
                        }",
                        type = LogUtil.LogType.INFO,
                    )
                } catch (exception: Exception) {
                    crashlytics.recordException(exception)
                    _photoCollectionState.update {
                        PhotoCollectionState.Error(
                            error = exception,
                            collectPhotoType = CollectPhotoType.Add,
                            message = errorHandler.getErrorMessage(exception),
                        )
                    }
                    LogUtil.log(
                        keyName = TAG,
                        value = "Photo collection failed: ${errorHandler.getErrorMessage(exception)}",
                        type = LogUtil.LogType.ERROR,
                    )
                } finally {
                    isCollecting = false
                }
            }
        }

        /**
         * Removes a photo from a specified collection.
         *
         * This function initiates an asynchronous operation to remove a photo from a user's collection.
         * It updates the `_photoCollectionState` to reflect the current status of the operation (in progress,
         * success, or error). It prevents concurrent removal operations by checking the `isCollecting` flag.
         *
         * On success, the state is updated with a success message and the result from the repository.
         * On failure, the state is updated with the error information. The `isCollecting` flag is reset
         * in the `finally` block to ensure it's always available for subsequent requests.
         *
         * @param photoId The ID of the photo to be removed.
         * @param collectionId The ID of the collection from which the photo will be removed.
         */
        fun removePhotoFromCollection(
            photoId: String,
            collectionId: String,
        ) {
            if (isCollecting) {
                LogUtil.log(
                    keyName = TAG,
                    value = "Already collecting photo. Ignoring request.",
                    type = LogUtil.LogType.WARN,
                )
                return
            }
            viewModelScope.launch {
                isCollecting = true
                _photoCollectionState.update { PhotoCollectionState.InProgress(collectionId) }
                try {
                    val result = photoRepository.removePhotoFromCollection(photoId, collectionId)

                    _photoCollectionState.update {
                        PhotoCollectionState.Success(
                            "Photo removed successfully",
                            CollectPhotoType.Remove,
                            result.body(),
                        )
                    }

                    LogUtil.log(
                        keyName = TAG,
                        value = "Response: $result ${result.body()}",
                        type = LogUtil.LogType.INFO,
                    )
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    _photoCollectionState.update {
                        PhotoCollectionState.Error(
                            error = e,
                            message = "Photo removal failed: ${e.message}",
                            collectPhotoType = CollectPhotoType.Remove,
                        )
                    }
                    LogUtil.log(
                        keyName = TAG,
                        value = "Photo removal failed: ${e.message}",
                        type = LogUtil.LogType.ERROR,
                    )
                } finally {
                    isCollecting = false
                }
            }
        }

        /**
         * Initiates the download of a photo.
         *
         * This function launches a coroutine to handle the download process. It updates the `_downloadStatus`
         * StateFlow to reflect the current state of the download (Idle, InProgress, Complete).
         * Upon successful completion (and if the file didn't already exist), it triggers a download
         * notification to the Unsplash API via [triggerDownload].
         *
         * @param downloadUrl The URL from which to download the photo.
         * @param fileName The desired file name for the downloaded photo.
         * @param photoId The ID of the photo, used to notify the API about the download.
         */
        fun startDownloadPhoto(
            downloadUrl: String,
            fileName: String,
            photoId: String,
        ) {
            viewModelScope.launch {
                _downloadStatus.value = DownloadStatus.Idle

                downloadHandler.startDownload(
                    downloadUrl,
                    fileName = fileName,
                    onDownloadStarted = { id ->
                        _downloadStatus.value = DownloadStatus.InProgress(id)
                    },
                    onDownloadComplete = { success, message ->
                        _downloadStatus.value = DownloadStatus.Complete(success, message)

                        if (message != "File already exists") {
                            viewModelScope.launch {
                                triggerDownload(photoId = photoId)
                            }
                        }
                    },
                )
            }
        }

        /**
         * Notifies the Unsplash API that a photo download has occurred.
         * This is a requirement of the Unsplash API to track download statistics.
         * This function should be called after a photo has been successfully downloaded.
         *
         * @param photoId The ID of the photo that was downloaded.
         */
        private suspend fun triggerDownload(
            photoId: String,
        ) {
            try {
                val result = photoRepository.triggerPhotoDownload(photoId)
                if (result.isSuccessful) {
                    LogUtil.log(
                        keyName = TAG,
                        value = "Download triggered successfully",
                        type = LogUtil.LogType.INFO,
                    )
                } else {
                    val exception = HttpException(result)
                    crashlytics.recordException(exception)
                    LogUtil.log(
                        keyName = TAG,
                        value = "Download failed with code: ${result.code()}",
                        type = LogUtil.LogType.ERROR,
                    )
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
            }
        }

        /**
         * Initiates an asynchronous request to like a photo.
         *
         * This function launches a coroutine to call the `likePhoto` method in the `photoRepository`.
         * It prevents concurrent liking requests by checking the `isLiking` flag.
         *
         * The UI state is updated via `_likeStatus` to reflect the current status of the operation:
         * - [LikeStatus.Loading] when the request starts.
         * - [LikeStatus.Success] upon a successful API response.
         * - [LikeStatus.Error] if an exception occurs during the process.
         *
         * @param photoId The unique identifier of the photo to be liked.
         */
        fun likePhoto(
            photoId: String,
        ) {
            if (isLiking) {
                LogUtil.log(
                    keyName = TAG,
                    value = "Already liking photo. Ignoring request.",
                    type = LogUtil.LogType.WARN,
                )
                return
            }

            viewModelScope.launch {
                isLiking = true
                _likeStatus.update { LikeStatus.Loading }

                try {
                    val result = photoRepository.likePhoto(photoId)
                    _likeStatus.update { LikeStatus.Success("Photo liked successfully", false) }
                    LogUtil.log(
                        keyName = TAG,
                        value = "Photo liked successfully",
                        type = LogUtil.LogType.INFO,
                    )
                    LogUtil.log(
                        keyName = TAG,
                        value = "Response: $result",
                        type = LogUtil.LogType.INFO,
                    )
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    _likeStatus.update { LikeStatus.Error("Photo like failed: ${e.message}") }
                    LogUtil.log(
                        keyName = TAG,
                        value = "Photo like failed: ${e.message}",
                        type = LogUtil.LogType.ERROR,
                    )
                } finally {
                    isLiking = false
                }
            }
        }

        private val username = MutableStateFlow<String?>(null)

        /**
         * Sets the username for the current user.
         * This is used to fetch the user's collections.
         *
         * @param username The username of the user.
         */
        fun setUsername(
            username: String,
        ) {
            this.username.value = username
        }

        private val userCollectionFlowCache = mutableMapOf<String, Flow<PagingData<UserCollection>>>()

        @OptIn(ExperimentalCoroutinesApi::class)
        val userCollectionsFlow: Flow<PagingData<UserCollection>> = username.filterNotNull().flatMapLatest { username ->
            userCollectionFlowCache.getOrPut(username) {
                userCollectionsRepository.getCollections(username = username)
            }.flowOn(Dispatchers.IO)
        }

        private val _showCollectPhotoBottomSheet = MutableStateFlow(false)
        val showCollectPhotoBottomSheet: StateFlow<Boolean> = _showCollectPhotoBottomSheet.asStateFlow()

        /**
         * Sets the visibility of the bottom sheet used for collecting a photo into a user's collection.
         *
         * @param value `true` to show the bottom sheet, `false` to hide it.
         */
        fun setShowCollectPhotoBottomSheet(
            value: Boolean,
        ) {
            _showCollectPhotoBottomSheet.value = value
        }

        /**
         * Dislikes a photo identified by its ID.
         *
         * This function initiates an asynchronous operation to remove a "like" from a photo.
         * It prevents concurrent like/dislike operations by checking the `isLiking` flag.
         *
         * The function updates the `_likeStatus` state flow to reflect the current state of the operation:
         * - `LikeStatus.Loading` when the request starts.
         * - `LikeStatus.Success` upon successful completion.
         * - `LikeStatus.Error` if the request fails.
         *
         * @param photoId The unique identifier of the photo to dislike.
         */
        fun dislikePhoto(
            photoId: String,
        ) {
            if (isLiking) {
                LogUtil.log(
                    keyName = TAG,
                    value = "Already disliking photo. Ignoring request.",
                    type = LogUtil.LogType.WARN,
                )
                return
            }
            viewModelScope.launch {
                isLiking = true
                _likeStatus.update { LikeStatus.Loading }
                try {
                    val result = photoRepository.dislikePhoto(photoId)
                    _likeStatus.update { LikeStatus.Success("Photo disliked successfully", true) }
                    LogUtil.log(
                        keyName = TAG,
                        value = "Photo disliked successfully",
                        type = LogUtil.LogType.INFO,
                    )
                    LogUtil.log(
                        keyName = TAG,
                        value = "Response: $result",
                        type = LogUtil.LogType.INFO,
                    )
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    _likeStatus.update { LikeStatus.Error("Photo dislike failed: ${e.message}") }
                    LogUtil.log(
                        keyName = TAG,
                        value = "Photo dislike failed: ${e.message}",
                        type = LogUtil.LogType.ERROR,
                    )
                } finally {
                    isLiking = false
                }
            }
        }

        /**
         * Resets the download status to its initial idle state.
         * This is useful for clearing the download progress/completion indicator from the UI,
         * for instance, after the user has acknowledged the download result.
         */
        fun resetDownloadState() {
            _downloadStatus.value = DownloadStatus.Idle
        }

        private val _showCreateCollectionBottomSheet = MutableStateFlow(false)
        val showCreateCollectionBottomSheet: StateFlow<Boolean> = _showCreateCollectionBottomSheet.asStateFlow()

        private val _createCollectionState = MutableStateFlow<CreateCollectionState>(CreateCollectionState.Idle)
        val createCollectionState: StateFlow<CreateCollectionState> = _createCollectionState.asStateFlow()

        /**
         * Handles the UI state transition to show the "Create a new collection" bottom sheet.
         * This is typically triggered by a user action, like clicking a "Create new collection" button
         * from within the "Add to collection" sheet. It hides the current sheet and displays the
         * creation sheet.
         */
        fun onShowCreateCollectionSheet() {
            _showCollectPhotoBottomSheet.value = false
            _showCreateCollectionBottomSheet.value = true
        }

        /**
         * Handles the dismissal of the create collection bottom sheet.
         * This function hides the create collection sheet, resets its state to idle,
         * and re-displays the collect photo bottom sheet, returning the user to the
         * previous screen in the flow.
         */
        fun onDismissCreateCollectionSheet() {
            _showCreateCollectionBottomSheet.value = false
            _createCollectionState.value = CreateCollectionState.Idle
            _showCollectPhotoBottomSheet.value = true
        }

        /**
         * Finishes the create collection flow. This is typically called after a collection has been successfully created
         * and a photo has been added to it.
         *
         * This function hides the create collection bottom sheet, resets the creation state to idle,
         * and re-shows the collect photo bottom sheet, allowing the user to see the updated list of collections.
         */
        fun finishCreateCollectionFlow() {
            _showCreateCollectionBottomSheet.value = false
            _createCollectionState.value = CreateCollectionState.Idle
            _showCollectPhotoBottomSheet.value = true
        }

        /**
         * Creates a new user collection.
         *
         * This function initiates a coroutine to handle the creation of a new collection via the
         * `userCollectionsRepository`. It updates the `_createCollectionState` to reflect the
         * current status of the operation (InProgress, Success, or Failed).
         *
         * If a `photoId` is provided, the function will also attempt to add the specified photo
         * to the newly created collection upon successful creation.
         *
         * @param name The title of the new collection.
         * @param description An optional description for the collection.
         * @param isPrivate An optional boolean indicating whether the collection should be private.
         * @param photoId An optional ID of a photo to add to the collection immediately after creation.
         */
        fun createCollection(
            name: String,
            description: String?,
            isPrivate: Boolean?,
            photoId: String?,
        ) {
            viewModelScope.launch {
                _createCollectionState.value = CreateCollectionState.InProgress
                try {
                    val result = userCollectionsRepository.createCollection(name, description, isPrivate)
                    if (result.isSuccess) {
                        val newCollection = result.getOrNull()
                        if (newCollection != null) {
                            if (photoId != null) {
                                addPhotoToCollection(
                                    collectionId = newCollection.id,
                                    photoId = photoId,
                                )
                            }
                            _createCollectionState.value = CreateCollectionState.Success
                        } else {
                            val exception = IllegalStateException("createCollection returned successful result but null body")
                            crashlytics.recordException(exception)
                            _createCollectionState.value =
                                CreateCollectionState.Failed(errorMessage = errorHandler.getErrorMessage(exception))
                        }
                    } else {
                        val exception = result.exceptionOrNull() ?: IllegalStateException("createCollection failed with null exception")
                        crashlytics.recordException(exception)
                        _createCollectionState.value =
                            CreateCollectionState.Failed(errorMessage = errorHandler.getErrorMessage(exception))
                    }
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    _createCollectionState.value = CreateCollectionState.Failed(e.message)
                    LogUtil.log(
                        keyName = TAG,
                        value = "Failed to create collection: ${e.message}",
                        type = LogUtil.LogType.ERROR,
                    )
                }
            }
        }

        override fun onCleared() {
            super.onCleared()
            downloadHandler.cleanup()
        }
    }
