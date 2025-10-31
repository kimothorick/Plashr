package com.kimothorick.plashr.profile.presentation.editprofile

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.auth.AuthRepository
import com.kimothorick.plashr.profile.data.models.UpdateUserProfileRequest
import com.kimothorick.plashr.profile.domain.UserProfileRepository
import com.kimothorick.plashr.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represents the type of error that can occur on the edit profile screen.
 * This is used to distinguish between different error scenarios, such as failing to load
 * user data versus failing to save updated user data.
 */
enum class ErrorType {
    LOADING_ERROR,
    SAVING_ERROR,
    DEFAULT,
}

/**
 * Represents the different types of loading operations that can occur on the edit profile screen.
 * This is used to differentiate between various loading states, allowing the UI to react
 * appropriately, for example, by showing a specific loading indicator or navigating away upon success.
 */
enum class LoadType {
    PROFILE_UPDATE,
    DEFAULT,
}

/**
 * Represents the various states of the Edit Profile screen.
 * This sealed class is used to model the UI state, ensuring that the screen
 * can only be in one of these well-defined states at any given time.
 */
sealed class EditProfileScreenState {
    /**
     * Represents the initial or default state of the Edit Profile screen,
     * where no operation (like loading, saving, or showing an error) is active.
     */
    object Idle : EditProfileScreenState()

    /**
     * Represents the loading state of the edit profile screen.
     *
     * This state is used to indicate that an asynchronous operation, such as fetching user profile data
     * or saving updated profile information, is in progress.
     *
     * @property message A user-facing message to display, e.g., "Fetching User Profile..." or "Saving...".
     * @property loadType The specific type of loading operation being performed, which can be used to
     *                    customize the UI response (e.g., show a full-screen loader vs. a smaller indicator).
     *                    Defaults to [LoadType.DEFAULT].
     */
    data class Loading(
        val message: String,
        val loadType: LoadType = LoadType.DEFAULT,
    ) : EditProfileScreenState()

    /**
     * Represents the error state of the Edit Profile screen.
     * This state is used to communicate that an operation has failed.
     *
     * @property message The error message to be displayed to the user.
     * @property retryAction A lambda function that can be invoked to retry the failed operation.
     * @property errorType The specific type of error that occurred, used to tailor the UI response.
     */
    data class Error(
        val message: String,
        val retryAction: () -> Unit = {},
        val errorType: ErrorType = ErrorType.DEFAULT,
    ) : EditProfileScreenState()

    /**
     * Represents the successful state of an operation on the Edit Profile screen.
     * This state is typically used to display a success message to the user,
     * for example, after their profile has been fetched or updated successfully.
     *
     * @param message The success message to be displayed to the user.
     * @param loadType Specifies the type of operation that succeeded, allowing for different UI responses.
     *                 Defaults to [LoadType.DEFAULT]. For example, [LoadType.PROFILE_UPDATE]
     *                 can be used to trigger a navigation event after the profile is saved.
     */
    data class Success(
        val message: String,
        val loadType: LoadType = LoadType.DEFAULT,
    ) : EditProfileScreenState()
}

/**
 * ViewModel for the Edit Profile screen.
 *
 * This ViewModel is responsible for managing the state and business logic related to editing a user's profile.
 * It fetches the current user's profile data, holds the editable state of profile fields,
 * validates user input, and handles the saving of updated profile information.
 *
 * It observes the authorization status from [AuthRepository] and loads the user profile
 * from [UserProfileRepository] once the user is authorized.
 *
 * The UI state is exposed via [editProfileScreenState], which reflects the current status of
 * data loading or saving operations (Idle, Loading, Success, Error).
 *
 * @param userProfileRepository The repository for fetching and updating user profile data.
 * @param authRepository The repository for checking the application's authorization status.
 * @param crashlytics The service for logging exceptions and non-fatal errors.
 */
@HiltViewModel
class EditProfileScreenViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val authRepository: AuthRepository,
    private val crashlytics: FirebaseCrashlytics,
) : ViewModel() {
    var username by mutableStateOf("")
        private set
    var firstName by mutableStateOf("")
        private set
    var lastName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var portfolioUrl by mutableStateOf("")
        private set
    var location by mutableStateOf("")
        private set
    var bio by mutableStateOf("")
        private set
    var instagramUsername by mutableStateOf("")
        private set

    private val _editProfileScreenState = MutableStateFlow<EditProfileScreenState>(EditProfileScreenState.Idle)
    val editProfileScreenState: StateFlow<EditProfileScreenState> = _editProfileScreenState.asStateFlow()

    /**
     * Sets the current state of the edit profile screen.
     *
     * This function updates the [_editProfileScreenState] which is observed by the UI to show
     * loading, error, success, or idle states. It's used to communicate the status of
     * asynchronous operations like fetching or saving profile data.
     *
     * @param state The new [EditProfileScreenState] to be set.
     */
    fun setEditProfileScreenState(
        state: EditProfileScreenState,
    ) {
        _editProfileScreenState.value = state
    }

    init {
        viewModelScope.launch {
            authRepository.isAppAuthorized.filterNotNull().distinctUntilChanged().collectLatest { isAppAuthorized ->
                if (isAppAuthorized) {
                    loadProfileData(
                        "Error fetching profile!",
                        "Profile fetched successfully!",
                        "Fetching User Profile!",
                    )
                }
            }
        }
    }

    /**
     * Updates the username state.
     *
     * This function is called when the user changes their username in the UI.
     * It updates the mutable state `username` with the new value provided.
     *
     * @param newUsername The new username string entered by the user.
     */
    fun onUsernameChanged(
        newUsername: String,
    ) {
        username = newUsername
    }

    /**
     * Updates the first name state with a new value.
     *
     * This function is called when the user modifies the first name input field
     * in the UI. It updates the `firstName` mutable state, which is then
     * used for validation and for the profile update request.
     *
     * @param newFirstName The new first name string from the input field.
     */
    fun onFirstNameChanged(
        newFirstName: String,
    ) {
        firstName = newFirstName
    }

    /**
     * Updates the last name state with a new value.
     *
     * This function is called when the user modifies the last name input field in the UI.
     *
     * @param newLastName The new last name string provided by the user.
     */
    fun onLastNameChanged(
        newLastName: String,
    ) {
        lastName = newLastName
    }

    /**
     * Updates the user's email address in the ViewModel's state.
     *
     * This function is called when the user modifies the email input field in the UI.
     *
     * @param newEmail The new email string entered by the user.
     */
    fun onEmailChanged(
        newEmail: String,
    ) {
        email = newEmail
    }

    val emailHasErrors by derivedStateOf {
        if (email.isBlank()) {
            true
        } else {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    val usernameHasErrors by derivedStateOf {
        val usernamePattern = Regex("^[a-zA-Z0-9_]+$")
        if (username.isBlank()) {
            true
        } else {
            !usernamePattern.matches(username)
        }
    }

    val firstNameHasErrors by derivedStateOf {
        firstName.isBlank()
    }

    /**
     * Updates the state of the user's portfolio URL.
     *
     * This function is called when the user modifies the portfolio URL input field
     * in the edit profile screen. It updates the [portfolioUrl] mutable state,
     * which is then observed by the UI to reflect the changes.
     *
     * @param newUrl The new portfolio URL string entered by the user.
     */
    fun onPortfolioUrlChanged(
        newUrl: String,
    ) {
        portfolioUrl = newUrl
    }

    /**
     * Updates the user's location in the view model's state.
     *
     * This function is called when the user changes their location in the UI.
     *
     * @param newLocation The new location string provided by the user.
     */
    fun onLocationChanged(
        newLocation: String,
    ) {
        location = newLocation
    }

    /**
     * Updates the user's biography state.
     *
     * This function is called when the user modifies their biography in the UI.
     * It updates the `bio` mutable state variable with the new text.
     *
     * @param newBio The updated biography text from the input field.
     */
    fun onBioChanged(
        newBio: String,
    ) {
        bio = newBio
    }

    /**
     * Updates the state of the user's Instagram username.
     *
     * This function is called when the user modifies the Instagram username field in the UI.
     * It updates the [instagramUsername] mutable state, which recomposes any UI elements
     * that observe it.
     *
     * @param newInstagramUsername The new Instagram username entered by the user.
     */
    fun onInstagramUsernameChanged(
        newInstagramUsername: String,
    ) {
        instagramUsername = newInstagramUsername
    }

    /**
     * Fetches the current logged-in user's profile data from the repository and updates the UI state.
     *
     * This function initiates an asynchronous operation to retrieve the user's profile.
     * - On initiation, it sets the [editProfileScreenState] to [EditProfileScreenState.Loading] with the provided [loadingProfileMessage].
     * - On successful retrieval, it populates the ViewModel's properties (e.g., [username], [firstName]) with the fetched data
     *   and updates the state to [EditProfileScreenState.Success] with the [successfulProfileLoadMessage].
     * - On failure, it updates the state to [EditProfileScreenState.Error] with the [errorProfileMessage] and provides a retry action.
     *
     * @param loadingProfileMessage The message to display while the profile is being loaded.
     * @param successfulProfileLoadMessage The message to display upon successful profile load.
     * @param errorProfileMessage The message to display if an error occurs during loading.
     */
    fun loadProfileData(
        loadingProfileMessage: String,
        successfulProfileLoadMessage: String,
        errorProfileMessage: String,
    ) {
        viewModelScope.launch {
            setEditProfileScreenState(EditProfileScreenState.Loading(loadingProfileMessage, LoadType.DEFAULT))
            userProfileRepository.getLoggedUserProfile().onSuccess { profile ->
                _editProfileScreenState.value = EditProfileScreenState.Success(successfulProfileLoadMessage, LoadType.DEFAULT)
                LogUtil.log(
                    "EditProfileScreenViewModel",
                    "loadProfileData: $profile",
                )
                username = profile.username ?: ""
                firstName = profile.firstName ?: ""
                lastName = profile.lastName ?: ""
                email = profile.email ?: ""
                portfolioUrl = profile.portfolioUrl ?: ""
                location = profile.location ?: ""
                bio = profile.bio ?: ""
                instagramUsername = profile.instagramUsername ?: ""
            }.onFailure { exception ->
                crashlytics.recordException(exception)
                setEditProfileScreenState(
                    EditProfileScreenState.Error(
                        message = errorProfileMessage,
                        errorType = ErrorType.LOADING_ERROR,
                        retryAction = {
                            loadProfileData(
                                loadingProfileMessage,
                                successfulProfileLoadMessage,
                                errorProfileMessage,
                            )
                        },
                    ),
                )
            }
        }
    }

    /**
     * Saves the updated user profile data to the repository.
     *
     * This function launches a coroutine to handle the profile update asynchronously.
     * It first sets the screen state to [EditProfileScreenState.Loading] to indicate that an update is in progress.
     * It then constructs an [UpdateUserProfileRequest] with the current values from the ViewModel's state,
     * sending only non-blank fields for the update.
     *
     * On successful update, it updates the ViewModel's properties with the new data returned from the repository
     * and sets the screen state to [EditProfileScreenState.Success].
     *
     * On failure, it logs the error and sets the screen state to [EditProfileScreenState.Error],
     * providing an error message and a `retryAction` lambda that allows the user to attempt the save operation again.
     *
     * @param updatingProfileMessage The message to display while the profile is being updated.
     * @param updateProfileErrorMessage The message to display if the update fails.
     * @param profileUpdateSuccessMessage The message to display upon a successful update.
     */
    fun saveProfile(
        updatingProfileMessage: String,
        updateProfileErrorMessage: String,
        profileUpdateSuccessMessage: String,
    ) {
        viewModelScope.launch {
            setEditProfileScreenState(EditProfileScreenState.Loading(updatingProfileMessage, LoadType.PROFILE_UPDATE))
            val updateUserProfileRequest = UpdateUserProfileRequest(
                username = username.takeIf { it.isNotBlank() },
                firstName = firstName.takeIf { it.isNotBlank() },
                lastName = lastName.takeIf { it.isNotBlank() },
                email = email.takeIf { it.isNotBlank() },
                portfolioUrl = portfolioUrl.takeIf { it.isNotBlank() },
                location = location.takeIf { it.isNotBlank() },
                bio = bio.takeIf { it.isNotBlank() },
                instagramUsername = instagramUsername.takeIf { it.isNotBlank() },
            )

            userProfileRepository.updateUserProfile(updateUserProfileRequest)
                .onSuccess { updatedProfile ->
                    username = updatedProfile.username ?: ""
                    firstName = updatedProfile.firstName ?: ""
                    lastName = updatedProfile.lastName ?: ""
                    email = updatedProfile.email ?: ""
                    portfolioUrl = updatedProfile.portfolioUrl ?: ""
                    location = updatedProfile.location ?: ""
                    bio = updatedProfile.bio ?: ""
                    instagramUsername = updatedProfile.instagramUsername ?: ""

                    setEditProfileScreenState(EditProfileScreenState.Success(profileUpdateSuccessMessage, LoadType.PROFILE_UPDATE))
                }.onFailure { exception ->
                    crashlytics.recordException(exception)
                    LogUtil.log(
                        "EditProfileScreenViewModel",
                        "saveProfile failed: ${exception.message}",
                        LogUtil.LogType.ERROR,
                    )

                    setEditProfileScreenState(
                        EditProfileScreenState.Error(
                            message = updateProfileErrorMessage,
                            errorType = ErrorType.SAVING_ERROR,
                            retryAction = {
                                saveProfile(
                                    updatingProfileMessage,
                                    updateProfileErrorMessage,
                                    profileUpdateSuccessMessage,
                                )
                            },
                        ),
                    )
                }
        }
    }
}
