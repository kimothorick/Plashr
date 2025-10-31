package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.data.models.user.User
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.data.models.user.UserPhoto
import com.kimothorick.plashr.data.models.user.UserPhotoLikes
import com.kimothorick.plashr.profile.data.models.Me
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Defines the API endpoints for user-related data on the Unsplash API.
 * This interface is used by Retrofit to create a service for fetching and updating user information,
 * photos, likes, and collections.
 *
 * @see <a href="https://unsplash.com/documentation#users">Unsplash API - Users</a>
 */
interface UserDataService {
    /**
     * Retrieve the profile of the currently logged-in user.
     *
     * This requires the `read_user` scope.
     *
     * @return A [Response] object containing the user's profile information in a [Me] object.
     * @see <a href="https://unsplash.com/documentation#get-the-users-profile">Get the user's profile</a>
     */
    @GET("/me")
    suspend fun getLoggedUserProfile(): Response<Me>

    /**
     * Update the profile for the currently logged-in user.
     *
     * This requires the `write_user` scope.
     * Note: If you retrieve the user's private information, you will need to re-request their permission to share their private information if you want to update their profile.
     *
     * @param username The user's new username.
     * @param firstName The user's new first name.
     * @param lastName The user's new last name.
     * @param email The user's new email address.
     * @param portfolioUrl The user's new portfolio URL.
     * @param location The user's new location.
     * @param bio The user's new bio.
     * @param instagramHandle The user's new Instagram username.
     * @return A [Response] containing the updated [Me] object on success.
     * @see <a href="https://unsplash.com/documentation#update-the-current-user">Unsplash API: Update the current user</a>
     */
    @PUT("/me")
    suspend fun updateLoggedUserProfile(
        @Query("username") username: String?,
        @Query("first_name") firstName: String?,
        @Query("last_name") lastName: String?,
        @Query("email") email: String?,
        @Query("url") portfolioUrl: String?,
        @Query("location") location: String?,
        @Query("bio") bio: String?,
        @Query("instagram_username") instagramHandle: String?,
    ): Response<Me>

    /**
     * Retrieve a user's public profile.
     *
     * @param username The username of the user to retrieve.
     * @return A [Response] object containing a [User] profile.
     * @see <a href="https://unsplash.com/documentation#get-a-users-public-profile">Official Unsplash API Documentation</a>
     */
    @GET("/users/{username}")
    suspend fun getUserPublicProfile(
        @Path("username") username: String,
    ): Response<User>

    /**
     * Retrieve a list of photos uploaded by a user.
     *
     * @param username The user’s username. Required.
     * @param page Page number to retrieve. (Default: 1)
     * @param perPage Number of items per page. (Default: 10)
     * @param orderBy How to sort the photos. (Valid values: latest, oldest, popular; default: latest)
     * @param stats Show the stats for each user’s photo. (Default: false)
     * @param resolution The frequency of the stats. (Valid values: days)
     * @param quantity The amount of stat data. (Default: 30)
     * @return A [Response] containing a list of [UserPhoto] objects.
     * @see <a href="https://unsplash.com/documentation#get-a-users-photos">Unsplash API: Get a user's photos</a>
     */
    @GET("/users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String,
        @Query("stats") stats: Boolean,
        @Query("resolution") resolution: String,
        @Query("quantity") quantity: Int,
    ): Response<List<UserPhoto>>

    /**
     * Retrieve a single page of photos liked by a user.
     *
     * @param username The user’s username.
     * @param page Page number to retrieve. (Default: 1)
     * @param perPage Number of items per page. (Default: 10)
     * @param orderBy How to sort the photos. (Optional; Valid values: latest, oldest, popular; Default: latest)
     * @param orientation Filter by photo orientation. (Optional; Valid values: landscape, portrait, squarish)
     * @return A [Response] containing a list of [UserPhotoLikes].
     * @see <a href="https://unsplash.com/documentation#list-a-users-liked-photos">List a user's liked photos</a>
     */
    @GET("/users/{username}/likes")
    suspend fun getUserLikes(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String?,
        @Query("orientation") orientation: String?,
    ): Response<List<UserPhotoLikes>>

    /**
     * Retrieve a list of collections created by a specific user.
     *
     * @param username The username of the user whose collections are to be retrieved.
     * @param page The page number to retrieve. Defaults to 1.
     * @param perPage The number of items per page. Defaults to 10.
     * @return A [Response] object containing a list of [UserCollection] objects.
     * @see <a href="https://unsplash.com/documentation#get-a-users-collections">Unsplash API: Get a user's collections</a>
     */
    @GET("/users/{username}/collections")
    suspend fun getUserCollections(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<UserCollection>>
}
