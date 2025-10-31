package com.kimothorick.plashr.profile.domain

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.data.remote.UserDataService
import com.kimothorick.plashr.profile.data.models.Me
import com.kimothorick.plashr.profile.data.models.UpdateUserProfileRequest
import jakarta.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing the logged-in user's profile data.
 *
 * This class acts as a single source of truth for user profile information, abstracting the
 * data source (in this case, the [UserDataService]) from the UI/ViewModel layer. It provides
 * methods to fetch and update the user's profile.
 *
 * @property userDataService The remote data source for user-related network calls.
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 * @constructor Creates an instance of the UserProfileRepository. Injected by Dagger/Hilt.
 */
@Singleton
class UserProfileRepository @Inject constructor(
    private val userDataService: UserDataService,
    private val crashlytics: FirebaseCrashlytics,
) {
    /**
     * Fetches the profile of the currently logged-in user.
     *
     * This function makes a network request to retrieve the user's public profile information.
     * It uses a `Result` wrapper to handle both successful and failed outcomes gracefully.
     *
     * @return A `Result<Me>` which is a `Result.success(Me)` containing the user's profile data
     * on a successful request, or a `Result.failure(Exception)` if the request fails
     * due to network issues or an error response from the server.
     */
    suspend fun getLoggedUserProfile(): Result<Me> { // Using Result for better error handling
        return try {
            val response = userDataService.getLoggedUserProfile()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val exception = Exception("Error fetching profile: ${response.message()}")
                crashlytics.recordException(exception)
                Result.failure(exception)
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }

    /**
     * Updates the profile information for the currently logged-in user.
     *
     * This function takes a [UpdateUserProfileRequest] object containing the new profile data,
     * sends it to the [UserDataService] to perform the update via a network request, and
     * returns the updated user profile information.
     *
     * @param updateUserProfileRequest An object containing the user's new profile details.
     * @return A [Result] object which is either a [Result.success] containing the updated [Me] object
     * or a [Result.Failure] containing an exception if the update fails due to network issues
     * or an error response from the server.
     */
    suspend fun updateUserProfile(
        updateUserProfileRequest: UpdateUserProfileRequest,
    ): Result<Me> {
        return try {
            val response = userDataService.updateLoggedUserProfile(
                firstName = updateUserProfileRequest.firstName,
                lastName = updateUserProfileRequest.lastName,
                username = updateUserProfileRequest.username,
                email = updateUserProfileRequest.email,
                bio = updateUserProfileRequest.bio,
                location = updateUserProfileRequest.location,
                portfolioUrl = updateUserProfileRequest.portfolioUrl,
                instagramHandle = updateUserProfileRequest.instagramUsername,
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val exception = Exception("Error updating profile: ${response.message()}")
                crashlytics.recordException(exception)
                Result.failure(exception)
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.failure(e)
        }
    }
}
