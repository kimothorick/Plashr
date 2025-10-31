package com.kimothorick.plashr.profile.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the logged-in user's profile data and authentication state using Jetpack DataStore.
 *
 * This class acts as a single source of truth for user-related information such as user ID,
 * username, name, profile picture URL, and the access token. It provides reactive [Flow]s
 * to observe data changes and `suspend` functions to update the stored values.
 *
 * The data is stored in a `Preferences` DataStore named "logged_user_profile".
 *
 * @property context The application context, used to access the DataStore instance.
 * @constructor Creates an instance of ProfileDataStore. Injected by Hilt.
 */
@Singleton
class ProfileDataStore
    @Inject constructor(
        val context: Context,
    ) {
        companion object {
            val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "logged_user_profile")
        }

        val userIdPrefKey = stringPreferencesKey("user_id")

        val userIdFlow = context.dataStore.data.map { preferences ->
            preferences[userIdPrefKey] ?: ""
        }

        /**
         * Persists the user's ID to the DataStore.
         *
         * This is a suspend function that safely updates the DataStore preferences
         * with the provided user ID.
         *
         * @param userId The unique identifier for the user to be saved.
         */
        suspend fun setUserId(
            userId: String,
        ) {
            context.dataStore.edit { preferences ->
                preferences[userIdPrefKey] = userId
            }
        }

        val usernamePrefKey = stringPreferencesKey("username")

        val usernameFlow = context.dataStore.data.map { preferences ->
            preferences[usernamePrefKey] ?: ""
        }

        /**
         * Saves the user's username to the DataStore.
         *
         * This is a suspending function that updates the preferences with the provided username.
         *
         * @param username The username to be stored.
         */
        suspend fun setUsername(
            username: String,
        ) {
            context.dataStore.edit { preferences ->
                preferences[usernamePrefKey] = username
            }
        }

        val firstNamePrefKey = stringPreferencesKey("first_name")

        val firstNameFlow = context.dataStore.data.map { preferences ->
            preferences[firstNamePrefKey] ?: ""
        }

        val lastNamePrefKey = stringPreferencesKey("last_name")

        val lastNameFlow = context.dataStore.data.map { preferences ->
            preferences[lastNamePrefKey] ?: ""
        }

        val profilePicUrlPrefKey = stringPreferencesKey("profile_picture_url")

        val profilePictureUrlFlow = context.dataStore.data.map { preferences ->
            preferences[profilePicUrlPrefKey] ?: ""
        }

        val accessTokenPrefKey = stringPreferencesKey("access_token")

        val accessTokenFlow = context.dataStore.data.map { preferences ->
            preferences[accessTokenPrefKey] ?: ""
        }

        /**
         * Persists the user's access token to the DataStore.
         *
         * This is a suspend function that safely updates the preferences with the new access token.
         *
         * @param accessToken The access token string to be saved.
         */
        suspend fun setAccessToken(
            accessToken: String,
        ) {
            context.dataStore.edit { preferences ->
                preferences[accessTokenPrefKey] = accessToken
            }
        }

        /**
         * Asynchronously sets all user details in the DataStore in a single transaction.
         * This is more efficient than setting each detail individually.
         *
         * @param userId The unique identifier for the user.
         * @param username The user's username.
         * @param firstName The user's first name.
         * @param lastName The user's last name.
         * @param profilePictureUrl The URL to the user's profile picture.
         */
        suspend fun setAllUserDetails(
            userId: String,
            username: String,
            firstName: String,
            lastName: String,
            profilePictureUrl: String,
        ) {
            context.dataStore.edit { preferences ->
                preferences[userIdPrefKey] = userId
                preferences[usernamePrefKey] = username
                preferences[firstNamePrefKey] = firstName
                preferences[lastNamePrefKey] = lastName
                preferences[profilePicUrlPrefKey] = profilePictureUrl
            }
        }

        /**
         * Clears all data from the user profile DataStore.
         *
         * This function removes all key-value pairs stored in the "logged_user_profile" preferences,
         * effectively logging the user out by deleting their session data such as user ID, username,
         * and access token.
         */
        suspend fun clearDataStore() {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }

        val isAppAuthorized: Flow<Boolean> = accessTokenFlow.map { accessToken ->
            accessToken.isNotEmpty()
        }
    }
