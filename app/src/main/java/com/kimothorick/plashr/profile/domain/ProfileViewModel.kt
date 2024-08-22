package com.kimothorick.plashr.profile.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for accessing and modifying user profile information.
 *
 * This ViewModel utilizes [profileDataStore] to persist the data.* It is annotated with `@HiltViewModel` for dependency injection with Hilt.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileDataStore: ProfileDataStore) :
    ViewModel() {

    /**
     * Flow to observe changes to the authorization status.
     * This is based on the accesstoken; if it's empty, the app is not authorized.
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
     * Setsthe profile picture URL.
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
    val accessToken = profileDataStore.ACCESS_TOKEN

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
}