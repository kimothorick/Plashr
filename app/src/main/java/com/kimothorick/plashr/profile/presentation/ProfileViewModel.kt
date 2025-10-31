package com.kimothorick.plashr.profile.presentation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.auth.UnsplashAuthHelper
import com.kimothorick.plashr.data.remote.UserDataService
import com.kimothorick.plashr.profile.domain.ProfileDataStore
import com.kimothorick.plashr.profile.presentation.components.LoginState
import com.kimothorick.plashr.profile.presentation.components.LoginState.IDLE
import com.kimothorick.plashr.profile.presentation.components.ManageAccountState
import com.kimothorick.plashr.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing user authentication, profile data, and related UI state.
 *
 * This class orchestrates the Unsplash user authentication flow, including initiating the
 * login process and handling the redirect result from the OAuth flow. It interacts with
 * [ProfileDataStore] to persist and retrieve user profile details (e.g., username, name,
 * profile picture, access token) and uses [UserDataService] to fetch the user's public
 * profile from the Unsplash API.
 *
 * It exposes various pieces of UI state as [StateFlow]s, including:
 * - [isAppAuthorized]: A boolean indicating if a valid access token exists.
 * - [loginState]: The current status of the authentication process ([LoginState]).
 * - [manageAccountState]: The state for the account management UI ([ManageAccountState]).
 * - User details like [username], [firstName], [lastName], and [profilePictureUrl].
 *
 * It also provides functions to perform actions such as initiating login ([initiateUnsplashLogin]),
 * handling the authentication result ([handleUnsplashAuthResult]), fetching user data ([fetchUserProfile]),
 * and logging out ([logout]).
 *
 * @param profileDataStore The data store for persisting and retrieving user profile information.
 * @param userDataService The service for making API calls to fetch Unsplash user data.
 * @param unsplashAuthHelper A helper class to manage the Unsplash OAuth2 authentication process.
 * @param crashlytics A Firebase service for logging non-fatal exceptions and errors.
 */
@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val profileDataStore: ProfileDataStore,
        private val userDataService: UserDataService,
        private val unsplashAuthHelper: UnsplashAuthHelper,
        private val crashlytics: FirebaseCrashlytics,
    ) : ViewModel() {
        val isAppAuthorized: StateFlow<Boolean> =
            profileDataStore.accessTokenFlow.map { accessToken ->
                accessToken.isNotEmpty()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false,
            )

        /**
         * Asynchronously sets and persists the user's unique ID.
         * This ID is typically obtained after a successful authentication process.
         *
         * @param userId The unique identifier for the user.
         */
        fun setUserId(
            userId: String,
        ) {
            viewModelScope.launch {
                profileDataStore.setUserId(userId)
            }
        }

        val username = profileDataStore.usernameFlow

        /**
         * Asynchronously sets and persists the user's username.
         *
         * This function launches a coroutine in the `viewModelScope` to update the username
         * in the `profileDataStore`.
         *
         * @param username The new username to be saved.
         */
        fun setUsername(
            username: String,
        ) {
            viewModelScope.launch {
                profileDataStore.setUsername(username)
            }
        }

        val firstName = profileDataStore.firstNameFlow

        val lastName = profileDataStore.lastNameFlow

        val profilePictureUrl =
            profileDataStore.profilePictureUrlFlow.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                null,
            )

        val accessToken = profileDataStore.accessTokenFlow

        /**
         * Persists the user's Unsplash access token to the DataStore.
         *
         * This function is called after a successful authentication flow to save the
         * token required for making authenticated API requests. The operation is
         * performed asynchronously within the `viewModelScope`.
         *
         * @param accessToken The access token string obtained from the Unsplash OAuth flow.
         */
        fun setAccessToken(
            accessToken: String,
        ) {
            viewModelScope.launch {
                profileDataStore.setAccessToken(accessToken)
            }
        }

        /**
         * Initiates a request to fetch the public profile of the currently logged-in user.
         * This function launches a coroutine to call [getLoggedUserProfile] asynchronously.
         *
         * @param username The username of the user whose profile is to be fetched.
         */
        fun fetchUserProfile(
            username: String,
        ) {
            viewModelScope.launch {
                getLoggedUserProfile(username)
            }
        }

        /**
         * Logs the user out by clearing all their data from the local data store.
         * This includes the access token and all profile details. It then resets the
         * login state to [IDLE] and updates the account management state to
         * [ManageAccountState.LOGOUT_SUCCESS] to reflect the successful logout.
         */
        fun logout() {
            viewModelScope.launch {
                profileDataStore.clearDataStore()
                setLoginState(IDLE)
                setManageAccountState(ManageAccountState.LOGOUT_SUCCESS)
            }
        }

        val areAllFieldsPopulated: Flow<Boolean> =
            combine(
                profileDataStore.accessTokenFlow,
                profileDataStore.userIdFlow,
                profileDataStore.usernameFlow,
                profileDataStore.firstNameFlow,
                profileDataStore.lastNameFlow,
                profileDataStore.profilePictureUrlFlow,
            ) { userProfileDetails ->
                userProfileDetails.all { it.isNotEmpty() }
            }

        /**
         * Fetches the public profile of the logged-in user from the Unsplash API.
         *
         * This function initiates a network request to retrieve the user's public details. On a successful
         * response, it persists the user's ID, username, first name, last name, and profile picture URL
         * to the `profileDataStore`. It then updates the `loginState` to [LoginState.SUCCESS] and the
         * `manageAccountState` to [ManageAccountState.PROFILE_DETAILS].
         *
         * If the API call fails, or if the provided `username` is empty, the `loginState` is set to
         * [LoginState.FAILED].
         *
         * @param username The username of the user whose profile is to be fetched.
         */
        suspend fun getLoggedUserProfile(
            username: String,
        ) {
            if (username.isEmpty()) {
                LogUtil.log(
                    keyName = "LoggedUserProfile",
                    value = "getLoggedUserProfile called with empty username. Aborting.",
                    type = LogUtil.LogType.WARN,
                )
                setLoginState(LoginState.FAILED)
                return
            }

            try {
                LogUtil.log(
                    keyName = "LoggedUserProfile",
                    value = "getLoggedUserProfile called: $username",
                    type = LogUtil.LogType.INFO,
                )
                val response = userDataService.getUserPublicProfile(username)
                if (response.isSuccessful && response.body() != null) {
                    LogUtil.log(
                        keyName = "LoggedUserProfile",
                        value = "getLoggedUserProfile: successful.",
                        type = LogUtil.LogType.INFO,
                    )
                    val user = response.body()!!
                    profileDataStore.setAllUserDetails(
                        userId = user.id.toString(),
                        username = user.username ?: "",
                        firstName = user.firstName ?: "",
                        lastName = user.lastName ?: "",
                        profilePictureUrl = user.profileImage?.large ?: "",
                    )

                    if (areAllFieldsPopulated.first()) {
                        setLoginState(LoginState.SUCCESS)
                        setManageAccountState(ManageAccountState.PROFILE_DETAILS)
                    } else {
                        LogUtil.log(
                            keyName = "LoggedUserProfile",
                            value = "getLoggedUserProfile: fields not populated after fetch. Current token: ${accessToken.first()}",
                            type = LogUtil.LogType.WARN,
                        )
                    }
                } else {
                    LogUtil.log(
                        keyName = "LoggedUserProfile",
                        value = "getLoggedUserProfile: failed or empty body. Code: ${response.code()}",
                        type = LogUtil.LogType.ERROR,
                    )
                    crashlytics.recordException(
                        Exception("getLoggedUserProfile request failed with code: ${response.code()}"),
                    )
                    setLoginState(LoginState.FAILED)
                }
            } catch (e: Exception) {
                LogUtil.log(
                    keyName = "LoggedUserProfile",
                    value = "getLoggedUserProfile: threw an exception ${e.message}",
                    type = LogUtil.LogType.ERROR,
                )
                crashlytics.recordException(e)
                setLoginState(LoginState.FAILED)
            }
        }

        private val _loginState = MutableStateFlow(IDLE)

        val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

        /**
         * Updates the current login state.
         *
         * This function changes the value of the internal `_loginState` Flow, which in turn
         * updates the public `loginState` StateFlow. This is used to communicate the status
         * of the login process (e.g., in progress, success, failure) to the UI.
         * A log entry is also created to track state changes.
         *
         * @param newState The new [LoginState] to be set.
         */
        fun setLoginState(
            newState: LoginState,
        ) {
            _loginState.value = newState
            LogUtil.log("LoginState", "Login newState changed to: $newState", LogUtil.LogType.INFO)
        }

        private val _manageAccountState = MutableStateFlow(ManageAccountState.PROFILE_DETAILS)
        val manageAccountState: StateFlow<ManageAccountState> = _manageAccountState.asStateFlow()

        /**
         * Updates the state for the account management screen.
         * This state determines which view to show, such as profile details or logout confirmation.
         *
         * @param newState The new [ManageAccountState] to be set.
         */
        fun setManageAccountState(
            newState: ManageAccountState,
        ) {
            _manageAccountState.value = newState
        }

        private val _unsplashAuthIntent = MutableStateFlow<Intent?>(null)
        val unsplashAuthIntent: StateFlow<Intent?> = _unsplashAuthIntent.asStateFlow()

        /**
         * Initiates the Unsplash login process.
         *
         * This function launches a coroutine in the `viewModelScope`. It first updates the
         * login state to `IN_PROGRESS` to reflect that the authentication process has started.
         * It then uses [UnsplashAuthHelper] to create an authentication `Intent`. This intent is
         * then emitted to the `_unsplashAuthIntent` StateFlow, which the UI observes to launch
         * the web browser for Unsplash's OAuth flow.
         */
        fun initiateUnsplashLogin() {
            viewModelScope.launch {
                setLoginState(LoginState.IN_PROGRESS)
                val intent = unsplashAuthHelper.createAuthIntent()
                _unsplashAuthIntent.update { intent }
            }
        }

        /**
         * Consumes the Unsplash authentication intent by setting it to null.
         *
         * This is a one-time event handler. After the intent has been launched by the UI,
         * this function should be called to prevent it from being launched again on configuration
         * changes (e.g., screen rotation).
         */
        fun consumeUnsplashAuthIntent() {
            _unsplashAuthIntent.update { null }
        }

        /**
         * Processes the result from the Unsplash authentication activity.
         *
         * This function is called after the user attempts to log in via the Unsplash website.
         * It checks if the authentication was successful (`RESULT_OK`).
         *
         * - On success, it extracts the authorization data from the intent and uses [UnsplashAuthHelper]
         *   to exchange the authorization code for an access token. The received access token, user ID,
         *   and username are then stored, and the user's profile is fetched.
         *
         * - If the result is not `RESULT_OK` (e.g., the user cancelled the login), it updates the
         *   login state to [LoginState.IDLE].
         *
         * - If the result is `RESULT_OK` but the data is null, it logs an error and sets the
         *   login state to [LoginState.FAILED].
         *
         * @param unsplashAuthResult The result returned from the authentication activity, containing
         *   the result code and intent data.
         */
        fun handleUnsplashAuthResult(
            unsplashAuthResult: ActivityResult,
        ) {
            if (unsplashAuthResult.resultCode == Activity.RESULT_OK) {
                val authResultData = unsplashAuthResult.data
                if (authResultData != null) {
                    unsplashAuthHelper.handleAuthorizationResponse(
                        data = authResultData,
                        onSuccess = { accessToken, userId, username ->
                            viewModelScope.launch {
                                setAccessToken(accessToken)
                                setUserId(userId)
                                setUsername(username)
                                fetchUserProfile(username)
                            }
                        },
                        onError = { loginState ->
                            setLoginState(loginState)
                        },
                    )
                } else {
                    LogUtil.log("ProfileViewModel", "Auth result data is null", LogUtil.LogType.ERROR)
                    crashlytics.recordException(IllegalStateException("Auth result data from Unsplash is null"))
                    setLoginState(LoginState.FAILED)
                }
            } else {
                LogUtil.log(
                    "ProfileViewModel",
                    "Auth result code not OK: ${unsplashAuthResult.resultCode}, User may have cancelled.",
                    LogUtil.LogType.INFO,
                )

                setLoginState(IDLE)
            }
        }

        override fun onCleared() {
            super.onCleared()
            unsplashAuthHelper.onDestroy()
            LogUtil.log("ProfileViewModel", "onCleared called, UnsplashAuthHelper destroyed.", LogUtil.LogType.INFO)
        }
    }
