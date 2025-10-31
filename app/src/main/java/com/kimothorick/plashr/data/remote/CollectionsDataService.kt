package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.data.models.collection.Collection
import com.kimothorick.plashr.data.models.collection.CollectionPhoto
import com.kimothorick.plashr.data.models.collection.CreateCollection
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for the Unsplash Collections API.
 *
 * Defines the HTTP operations for interacting with collections, including fetching,
 * creating, updating, and deleting collections and their related data.
 *
 * @see <a href="https://unsplash.com/documentation#collections">Unsplash API - Collections</a>
 */
interface CollectionsDataService {
    /**
     * Retrieves a list of collections.
     *
     * @param page The page number to retrieve. Defaults to 1.
     * @param perPage The number of collections to return per page. Defaults to 10.
     * @return A [Response] containing a list of [Collection] objects.
     * @see <a href="https://unsplash.com/documentation#list-collections">Unsplash API - List Collections</a>
     */
    @GET("/collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Collection>>

    /**
     * Retrieve a single collection. To view a user’s private collections,
     * the bearer token must be used to authenticate the user.
     *
     * @param id The collection’s ID.
     * @return A [Response] containing the requested [Collection] object.
     * @see <a href="https://unsplash.com/documentation#get-a-collection">Unsplash API - Get a collection</a>
     */
    @GET("/collections/{id}")
    suspend fun getCollection(
        @Path("id") id: String,
    ): Response<Collection>

    /**
     * Retrieves a list of photos belonging to a specific collection.
     *
     * @param id The collection's ID.
     * @param page The page number to retrieve. Defaults to 1.
     * @param perPage The number of photos to return per page. Defaults to 10.
     * @return A [Response] containing a list of [CollectionPhoto] objects.
     * @see <a href="https://unsplash.com/documentation#get-a-collections-photos">Unsplash API - Get a collection’s photos</a>
     */
    @GET("/collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<CollectionPhoto>>

    /**
     * Creates a new collection. This requires the `write_collections` scope.
     *
     * @param title The title of the collection. (Required)
     * @param description The description of the collection. (Optional)
     * @param isPrivate Whether to make this collection private.
     *                  Private collections will not be discoverable. (Optional, defaults to false)
     * @return A [Response] containing the newly created [CreateCollection] object.
     * @see <a href="https://unsplash.com/documentation#create-a-new-collection">Unsplash API - Create a new collection</a>
     */
    @POST("/collections")
    suspend fun createCollection(
        @Query("title") title: String,
        @Query("description") description: String?,
        @Query("private") isPrivate: Boolean,
    ): Response<CreateCollection>

    /**
     * Updates an existing collection belonging to the authenticated user.
     *
     * This operation requires the `write_collections` scope.
     *
     * @param id The ID of the collection to update.
     * @param title The new title for the collection.
     * @param description The new description for the collection (optional).
     * @param isPrivate Whether to make the collection private.
     * @return A [Response] containing the updated [Collection] object.
     * @see <a href="https://unsplash.com/documentation#update-a-collection">Unsplash API - Update a collection</a>
     */
    @PUT("/collections/{id}")
    suspend fun updateCollection(
        @Path("id") id: String,
        @Query("title") title: String,
        @Query("description") description: String?,
        @Query("private") isPrivate: Boolean,
    ): Response<Collection>

    /**
     * Deletes a collection belonging to the authenticated user.
     *
     * This action is irreversible. The user must have `write_collections` scope.
     *
     * @param id The ID of the collection to delete.
     * @return A [Response] with an empty body (`Unit`) on successful deletion.
     * @see <a href="https://unsplash.com/documentation#delete-a-collection">Unsplash API - Delete a collection</a>
     */
    @DELETE("/collections/{id}")
    suspend fun deleteCollection(
        @Path("id") id: String,
    ): Response<Unit>
}
