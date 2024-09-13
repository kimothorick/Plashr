package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.profile.data.models.LoggedUserProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * An interface defining the API endpoints for interacting with Unsplash API calls
 */
interface UnsplashAPI {

    /**
     * Retrieves the logged-in user's private profile information.
     *
     * @return A [Response] object containing the [LoggedUserProfile] data if successful.
     */
    @GET("/me")
    suspend fun getLoggedUserProfile(): Response<LoggedUserProfile>

    /**
     * Retrieves the logged-in user's public profile information.
     *
     * @param username The username of the logged-in user.
     * @return A [Response] object containing the [LoggedUserProfile] data if successful.
     */
    @GET("/users/{username}")
    suspend fun getLoggedUserPublicProfile(@Path("username") username: String): Response<LoggedUserProfile>
}