package com.kimothorick.plashr.profile.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides access to user profile information persisted using Jetpack DataStore.
 *
 * This class leverages the Preferences DataStore to store key-value pairs.
 * It is designed to be injected using a dependency injection framework.
 */
@Singleton
class ProfileDataStore @Inject constructor(val context: Context) {

    companion object {
        /**
         * Extension property on `Context` to provide access to the `DataStore<Preferences>` instance.
         */
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "logged_user_profile")
    }

    /** Key for storing the user ID. */
    val USER_ID = stringPreferencesKey("user_id")

    /** Flow to observe changes to the user ID. */
    val userIdFlow = context.dataStore.data.map {preferences ->
        preferences[USER_ID] ?: ""
    }

    /**
     * Sets the user ID.
     *
     * @param userId The new userID to set.
     */
    suspend fun setUserId(userId: String) {
        context.dataStore.edit {preferences ->
            preferences[USER_ID] = userId
        }
    }

    /** Key for storing the username. */
    val USERNAME = stringPreferencesKey("username")

    /** Flow to observe changes to the username. */
    val usernameFlow = context.dataStore.data.map {preferences ->
        preferences[USERNAME] ?: ""
    }

    /**
     * Sets the username.
     *
     * @param username The new username to set.
     */
    suspend fun setUsername(username: String) {
        context.dataStore.edit {preferences ->
            preferences[USERNAME] = username
        }
    }

    /** Key for storing the first name. */
    val FIRST_NAME = stringPreferencesKey("first_name")

    /** Flow to observe changes to the first name. */
    val firstNameFlow = context.dataStore.data.map {preferences ->
        preferences[FIRST_NAME] ?: ""
    }

    /**
     * Sets the first name.
     ** @param firstName The new first name to set.
     */
    suspend fun setFirstName(firstName: String) {
        context.dataStore.edit {preferences ->
            preferences[FIRST_NAME] = firstName
        }
    }

    /** Key for storing the last name. */
    val LAST_NAME = stringPreferencesKey("last_name")

    /** Flow to observe changes to the last name. */
    val lastNameFlow = context.dataStore.data.map {preferences ->
        preferences[LAST_NAME] ?: ""
    }

    /**
     * Sets the last name.
     *
     * @param lastName The new last name to set.
     */
    suspend fun setLastName(lastName: String) {
        context.dataStore.edit {preferences ->
            preferences[LAST_NAME] = lastName
        }
    }

    /** Key for storing the profile picture URL. */
    val PROFILE_PICTURE_URL = stringPreferencesKey("profile_picture_url")

    /** Flow to observe changes to the profile picture URL. */
    val profilePictureUrlFlow = context.dataStore.data.map {preferences ->
        preferences[PROFILE_PICTURE_URL] ?: ""
    }

    /**
     * Sets the profile picture URL.
     *
     * @param profilePictureUrl The new profile picture URL to set.
     */
    suspend fun setProfilePictureUrl(profilePictureUrl: String) {
        context.dataStore.edit {preferences ->
            preferences[PROFILE_PICTURE_URL] = profilePictureUrl
        }
    }

    /** Key for storing the access token. */
    val ACCESS_TOKEN = stringPreferencesKey("access_token")

    /** Flow to observe changes to the access token. */
    val accessTokenFlow = context.dataStore.data.map {preferences ->
        preferences[ACCESS_TOKEN] ?: ""
    }

    /**
     * Sets the access token.
     *
     * @param accessToken The new access token to set.
     */
    suspend fun setAccessToken(accessToken: String) {
        context.dataStore.edit {preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }
}