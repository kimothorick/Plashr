package com.kimothorick.plashr.profile.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for accessing and modifying user profile information.
 *
 * This ViewModel utilizes [profileDataStore] to persist the data.
 * It is annotated with `@HiltViewModel` for dependency injection with Hilt.
 *
 * @param profileDataStore The data store for user profile information.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileDataStore: ProfileDataStore) :
    ViewModel() {

    /**
     * Flow to observe changes to the authorization status.
     * This is based on the access token; if it's empty, the app is not authorized.
     */
    val isAppAuthorized: StateFlow<Boolean> = profileDataStore.accessTokenFlow.map {accessToken ->
        accessToken.isNotEmpty()
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = false
    )

    /**
     * Flow to observe changes to the user ID.
     */
    val userId = profileDataStore.userIdFlow

    /**
     * Sets the user ID.
     *
     * @param userId The new user ID to set.
     */
    fun setUserId(userId: String) {
        viewModelScope.launch {
            profileDataStore.setUserId(userId)
        }
    }

    /**
     * Flow to observe changes to the username.
     */
    val username = profileDataStore.usernameFlow

    /**
     * Sets the username.
     *
     * @param username The new username to set.*/
    fun setUsername(username: String) {
        viewModelScope.launch {
            profileDataStore.setUsername(username)
        }
    }

    /**
     * Flow to observe changes to the first name.
     */
    val firstName = profileDataStore.firstNameFlow

    /**
     * Sets the first name.
     *
     * @param firstName The new first name to set.
     */
    fun setFirstName(firstName: String) {
        viewModelScope.launch {
            profileDataStore.setFirstName(firstName)
        }
    }

    /**
     * Flow to observe changes to the last name.
     */
    val lastName = profileDataStore.lastNameFlow

    /**
     * Sets the last name.
     *
     * @param lastName The new last name to set.
     */
    fun setLastName(lastName: String) {
        viewModelScope.launch {
            profileDataStore.setLastName(lastName)
        }
    }

    /**
     * Flow to observe changes to the profile picture URL.
     */
    val profilePictureUrl = profileDataStore.profilePictureUrlFlow

    /**
     * Sets the profile picture URL.
     *
     * @param profilePictureUrl The new profile picture URL to set.
     */
    fun setProfilePictureUrl(profilePictureUrl: String) {
        viewModelScope.launch {
            profileDataStore.setProfilePictureUrl(profilePictureUrl)
        }
    }

    /**
     * Key for storing the access token.
     */
    val accessToken = profileDataStore.accessTokenFlow

    /**
     * Sets the access token.
     *
     * @param accessToken The new access token to set.
     */
    fun setAccessToken(accessToken: String) {
        viewModelScope.launch {
            profileDataStore.setAccessToken(accessToken)
        }
    }

    /**
     * Adds user details to the profile data store.
     *
     * This function calls the `setAllUserDetails` method on the `profileDataStore` to store the
     * provided user information.
     *
     * @param userID The ID of the user.
     * @param username The username of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param profilePictureUrlThe URL of the user's profile picture.
     */
    suspend fun addUserDetails(
        userID: String,
        username: String,
        firstName: String,
        lastName: String,
        profilePictureUrl: String
    ) {
        profileDataStore.setAllUserDetails(userID, username, firstName, lastName, profilePictureUrl)
    }

    /**
     * Logs the user out by clearing the profile data store.
     *
     * This function launches a coroutine to call the `clearDataStore` method on the `profileDataStore`,
     * removing all stored user data.
     */
    fun logout() {
        viewModelScope.launch {
            profileDataStore.clearDataStore()
        }
    }

    /**
     * A flow that emits `true` if all user details fields are populated in the data store,
     * `false` otherwise.
     *
     * This flow combines multiple flows from the `profileDataStore` (accessToken, userId, username,
     * firstName, lastName, profilePictureUrl) and checks if all of them have non-empty string values.
     */
    val areAllFieldsPopulated: Flow<Boolean> = combine(
        profileDataStore.accessTokenFlow,
        profileDataStore.userIdFlow,
        profileDataStore.usernameFlow,
        profileDataStore.firstNameFlow,
        profileDataStore.lastNameFlow,
        profileDataStore.profilePictureUrlFlow
    ) {fields ->
        fields.all {(it as? String)?.isNotEmpty() ?: false}
    }

}