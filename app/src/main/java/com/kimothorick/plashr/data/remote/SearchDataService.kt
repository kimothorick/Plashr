package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.data.models.collection.SearchCollectionsResponse
import com.kimothorick.plashr.data.models.photo.SearchPhotosResponse
import com.kimothorick.plashr.data.models.user.SearchUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the API endpoints for searching content on the Unsplash API.
 * This interface is used by Retrofit to create a service for making search-related network requests.
 *
 * @see <a href="https://unsplash.com/documentation#search">Unsplash API - Search</a>
 */
interface SearchDataService {
    /**
     * Retrieves a paginated list of photos matching a search query.
     * Corresponds to the `GET /search/photos` endpoint.
     *
     * @param query The search terms.
     * @param page The page number to retrieve. Defaults to 1.
     * @param perPage The number of items per page. Defaults to 10.
     * @param orderBy How to sort the photos. Valid values are "latest" and "relevant". Defaults to "relevant".
     * @param contentFilter The content filter level. Valid values are "low" and "high". Defaults to "low".
     * @param color Filter results by color. Valid values: "black_and_white", "black", "white", "yellow", "orange", "red", "purple", "magenta", "green", "teal", "blue".
     * @param orientation Filter by photo orientation. Valid values: "landscape", "portrait", "squarish".
     * @return A [Response] object containing a [SearchPhotosResponse] which holds the list of found photos and pagination details.
     * @see <a href="https://unsplash.com/documentation#search-photos">Unsplash API - Search Photos</a>
     */
    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String?,
        @Query("content_filter") contentFilter: String?,
        @Query("color") color: String?,
        @Query("orientation") orientation: String?,
    ): Response<SearchPhotosResponse>

    /**
     * Retrieves a paginated list of collections that match a search query.
     *
     * @param query The search terms to query for.
     * @param page The page number to retrieve. Defaults to 1.
     * @param perPage The number of items per page. Defaults to 10.
     * @return A [Response] object containing a [SearchCollectionsResponse].
     * @see <a href="https://unsplash.com/documentation#search-collections">Unsplash API - Search Collections</a>
     */
    @GET("/search/collections")
    suspend fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<SearchCollectionsResponse>

    /**
     * Retrieves a paginated list of users matching a search query.
     * Corresponds to the `GET /search/users` endpoint.
     *
     * @param query The search terms.
     * @param page The page number to retrieve. Defaults to 1.
     * @param perPage The number of users to return per page. Defaults to 10, max is 30.
     * @return A [Response] object containing a [SearchUsersResponse] with the search results.
     * @see <a href="https://unsplash.com/documentation#search-users">Unsplash API - Search Users</a>
     */
    @GET("/search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<SearchUsersResponse>
}
