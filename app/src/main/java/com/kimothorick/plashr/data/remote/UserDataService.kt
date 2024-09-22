package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.data.models.user.User
import com.kimothorick.plashr.profile.data.models.Me
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserDataService {
    /**
     * Retrieves the logged-in user's private profile information.
     *
     * @return A [Response] object containing the [Me] data if successful.
     */
    @GET("/me")
    suspend fun getLoggedUserProfile(): Response<Me>

    /**
     * Retrieves the logged-in user's public profile information.
     *
     * @param username The username of the logged-in user.
     * @return A [Response] object containing the [User] data if successful.
     */
    @GET("/users/{username}")
    suspend fun getUserPublicProfile(@Path("username") username: String): Response<User>

}
