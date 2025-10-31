package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.data.models.photo.DownloadPhotoResponse
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.photo.PhotoCollectionResult
import com.kimothorick.plashr.data.models.photo.PhotoLikeResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for the Unsplash Photos API.
 *
 * Defines the HTTP operations for interacting with photos, including fetching,
 * liking, downloading, and managing photos within collections.
 *
 * @see <a href="https://unsplash.com/documentation#photos">Unsplash API - Photos</a>
 */
interface PhotoDataService {
    /**
     * Retrieves a list of photos.
     *
     * @param page The page number to retrieve.
     * @param perPage The number of items per page.
     * @return A [Response] containing a list of [Photo] objects.
     * @see <a href="https://unsplash.com/documentation#list-photos">List Photos on Unsplash API</a>
     */
    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Photo>>

    /**
     * Retrieves a single photo by its ID.
     *
     * This endpoint allows fetching detailed information about a specific photo.
     *
     * @param id The public ID of the photo to retrieve.
     * @return A [Response] wrapping a [Photo] object on success.
     * @see <a href="https://unsplash.com/documentation#get-a-photo">Get a Photo on Unsplash API</a>
     */
    @GET("/photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String,
    ): Response<Photo>

    /**
     * Triggers a photo download tracking event.
     *
     * This function makes a request to the Unsplash API to track a download.
     * Per the API documentation, this endpoint must be called every time a photo
     * is downloaded by a user. The response will contain a URL to download the
     * actual photo file.
     *
     * @param id The public ID of the photo to download.
     * @return A [Response] containing a [DownloadPhotoResponse], which includes the download URL.
     * @see <a href="https://unsplash.com/documentation#track-a-photo-download">Track a Photo Download on Unsplash API</a>
     */
    @GET("/photos/{id}/download")
    suspend fun triggerPhotoDownload(
        @Path("id") id: String,
    ): Response<DownloadPhotoResponse>

    /**
     * Likes a photo on behalf of the current user.
     *
     * @param id The ID of the photo to like.
     * @return A [Response] wrapping a [PhotoLikeResponse] which contains the updated photo and the user who liked it.
     */
    @POST("/photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") id: String,
    ): Response<PhotoLikeResponse>

    /**
     * Removes a user's like from a photo.
     *
     * This action requires the `write_likes` scope. It is idempotent, meaning
     * that calling it on a photo that is not already liked by the user will not
     * result in an error.
     *
     * @param id The public ID of the photo to unlike.
     * @return A [Response] with an empty body on success. A successful unlike
     * operation returns a 204 No Content status.
     */
    @DELETE("/photos/{id}/like")
    suspend fun unlikePhoto(
        @Path("id") id: String,
    ): Response<Void>

    /**
     * Adds a photo to one of the user's collections.
     *
     * This action requires the `write_collections` scope. If the user does not have this scope,
     * a 403 Forbidden response will be returned.
     *
     * @param collectionId The ID of the collection to add the photo to.
     * @param photoId The ID of the photo to add.
     * @return A [Response] containing a [PhotoCollectionResult] which includes the photo, the collection, and the user who added it.
     * @see <a href="https://unsplash.com/documentation#add-a-photo-to-a-collection">Unsplash API - Add a Photo to a Collection</a>
     */
    @POST("/collections/{collection_id}/add")
    suspend fun addPhotoToCollection(
        @Path("collection_id") collectionId: String,
        @Query("photo_id") photoId: String,
    ): Response<PhotoCollectionResult>

    /**
     * Removes a photo from a collection.
     *
     * This action requires the `write_collections` scope. If the user does not have
     * this scope, a 403 Forbidden response will be returned. This action is idempotent;
     * if the photo is not in the collection, it will not return an error.
     *
     * @param collectionId The ID of the collection to remove the photo from.
     * @param photoId The ID of the photo to remove.
     * @return A [Response] containing a [PhotoCollectionResult] which includes the photo and the collection it was removed from.
     * @see <a href="https://unsplash.com/documentation#remove-a-photo-from-a-collection">Remove a Photo from a Collection on Unsplash API</a>
     */
    @DELETE("/collections/{collection_id}/remove")
    suspend fun removePhotoFromCollection(
        @Path("collection_id") collectionId: String,
        @Query("photo_id") photoId: String,
    ): Response<PhotoCollectionResult>
}
