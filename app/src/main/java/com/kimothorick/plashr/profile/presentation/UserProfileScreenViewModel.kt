package com.kimothorick.plashr.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.models.user.User
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.data.models.user.UserPhoto
import com.kimothorick.plashr.data.models.user.UserPhotoLikes
import com.kimothorick.plashr.data.remote.UserDataService
import com.kimothorick.plashr.profile.domain.userCollection.UserCollectionRepository
import com.kimothorick.plashr.profile.domain.userLikes.UserLikesRepository
import com.kimothorick.plashr.profile.domain.userPhoto.UserPhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Represents the different states for the user profile screen.
 *
 * This sealed class is used to manage the UI state when fetching a user's profile data.
 * It can be in one of three states: loading, success, or error.
 */
sealed class UserProfileState {
    /**
     * Represents the loading state, indicating that the user profile data is currently being fetched.
     */
    object Loading : UserProfileState()

    /**
     * Represents the successful state of fetching a user's profile, containing the user's data.
     *
     * @param user The [User] object containing the profile information.
     */
    data class Success(
        val user: User,
    ) : UserProfileState()

    /**
     * Represents the error state of the user profile screen.
     * This state is triggered when there's an issue fetching the user's profile data.
     *
     * @property message A user-friendly string describing the error that occurred.
     */
    data class Error(
        val message: String,
    ) : UserProfileState()
}

/**
 * ViewModel for the User Profile screen.
 *
 * This ViewModel is responsible for fetching and managing user profile data,
 * including user details, photos, collections, and liked photos. It exposes this
 * data to the UI through a [StateFlow] for the user's profile information and
 * [Flow]s of [PagingData] for the user's content.
 *
 * The data fetching is triggered by calling [setUsername], which initiates the process
 * of loading the profile and the paginated content for the specified user.
 *
 * @param userPhotoRepository Repository for fetching user's photos.
 * @param userCollectionRepository Repository for fetching user's collections.
 * @param userLikesRepository Repository for fetching user's liked photos.
 * @param userDataService Service to fetch public user profile data.
 * @param errorHandler A utility to generate user-friendly error messages from exceptions.
 * @param crashlytics A service for logging and reporting crashes.
 */
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileScreenViewModel @Inject constructor(
    private val userPhotoRepository: UserPhotoRepository,
    private val userCollectionRepository: UserCollectionRepository,
    private val userLikesRepository: UserLikesRepository,
    private val userDataService: UserDataService,
    val errorHandler: ErrorHandler,
    private val crashlytics: FirebaseCrashlytics,
) : ViewModel() {
    private val userProfileState = MutableStateFlow<UserProfileState>(UserProfileState.Loading)
    val userProfileUiState: StateFlow<UserProfileState> = userProfileState
    private val requestedUsername = MutableStateFlow<String?>(null)
    private val activeUsername = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            requestedUsername.filterNotNull().collectLatest { requestedUsername ->
                fetchUserProfile(requestedUsername)
            }
        }
    }

    /**
     * Sets the username for the profile to be displayed.
     *
     * This triggers a flow that collects the new username and initiates fetching
     * the corresponding user profile data.
     *
     * @param requestedUsername The username of the user to fetch and display.
     */
    fun setUsername(
        requestedUsername: String,
    ) {
        this.requestedUsername.value = requestedUsername
    }

    /**
     * Fetches the public profile for a given user from the API.
     *
     * This is a suspend function that performs a network request to retrieve user data.
     * It updates the [userProfileState] to reflect the current state of the fetch operation:
     * - [UserProfileState.Loading]: Immediately set upon function call.
     * - [UserProfileState.Success]: Set when the API call is successful and a user profile is received.
     * - [UserProfileState.Error]: Set if the API call fails, the response is unsuccessful, or any other exception occurs.
     *
     * It also sets the [activeUsername] to the username being fetched.
     *
     * @param requestedUsername The username of the user profile to fetch.
     */
    private suspend fun fetchUserProfile(
        requestedUsername: String,
    ) {
        activeUsername.value = requestedUsername
        userProfileState.value = UserProfileState.Loading

        try {
            val result = userDataService.getUserPublicProfile(username = requestedUsername)
            if (result.isSuccessful) {
                result.body()?.let { user ->
                    userProfileState.value = UserProfileState.Success(user)
                } ?: run {
                    val exception = HttpException(result)
                    crashlytics.recordException(exception)
                    userProfileState.value = UserProfileState.Error(
                        errorHandler.getErrorMessage(
                            exception,
                        ),
                    )
                }
            } else {
                val httpException = HttpException(result)
                crashlytics.recordException(httpException)
                userProfileState.value = UserProfileState.Error(message = errorHandler.getErrorMessage(httpException))
            }
        } catch (exception: Exception) {
            crashlytics.recordException(exception)
            userProfileState.value = UserProfileState.Error(errorHandler.getErrorMessage(exception))
        }
    }

    /**
     * Refreshes the user profile data.
     *
     * This function re-fetches the profile information for the currently active user. It is
     * typically used for a pull-to-refresh action, ensuring the displayed data is up-to-date.
     * It uses the last successfully fetched username stored in `activeUsername` to make the request.
     */
    fun refreshUserProfile() {
        viewModelScope.launch {
            activeUsername.value?.let { requestedUsername ->
                fetchUserProfile(requestedUsername)
            }
        }
    }

    val userPhotosFlow: Flow<PagingData<UserPhoto>> = requestedUsername.filterNotNull().flatMapLatest { requestedUsername ->
        userPhotoRepository.getPhotos(username = requestedUsername)
    }.cachedIn(viewModelScope)

    val userCollectionsFlow: Flow<PagingData<UserCollection>> = requestedUsername.filterNotNull().flatMapLatest { requestedUsername ->
        userCollectionRepository.getCollections(username = requestedUsername)
    }.cachedIn(viewModelScope)

    val userLikesFlow: Flow<PagingData<UserPhotoLikes>> = requestedUsername.filterNotNull().flatMapLatest { requestedUsername ->
        userLikesRepository.getLikes(username = requestedUsername)
    }.cachedIn(viewModelScope)
}
