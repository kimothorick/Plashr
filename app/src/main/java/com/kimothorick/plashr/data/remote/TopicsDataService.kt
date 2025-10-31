package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.data.models.topics.TopicPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A Retrofit service interface for interacting with the Unsplash Topics API.
 *
 * This interface defines the HTTP operations for fetching topic-related data,
 * such as lists of topics, details of a specific topic, and photos within a topic.
 *
 * @see <a href="https://unsplash.com/documentation#topics">Unsplash API - Topics</a>
 */
interface TopicsDataService {
    /**
     * Retrieves a list of topics.
     *
     * @param page The page number to retrieve. (Default: 1)
     * @param perPage The number of items per page. (Default: 10)
     * @return A [Response] object containing a list of [Topic] objects.
     * @see <a href="https://unsplash.com/documentation#list-topics">List Topics | Unsplash API</a>
     */
    @GET("/topics")
    suspend fun getTopics(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Topic>>

    /**
     * Retrieve a single topic.
     *
     * @param id The topic’s ID or slug.
     * @return A [Response] object containing a [Topic].
     * @see <a href="https://unsplash.com/documentation#get-a-topic">Get a Topic | Unsplash API</a>
     */
    @GET("/topics/{id_or_slug}")
    suspend fun getTopic(
        @Path("id_or_slug") id: String,
    ): Response<Topic>

    /**
     * Retrieve photos for a specific topic.
     *
     * @param id The topic’s ID or slug.
     * @param page Page number to retrieve. (Default: 1)
     * @param perPage Number of items per page. (Default: 10)
     * @param orderBy How to sort the photos. (Valid values: latest, oldest, popular; default: latest)
     * @return A [Response] containing a list of [TopicPhoto] objects.
     * @see <a href="https://unsplash.com/documentation#get-a-topics-photos">Unsplash API documentation</a>
     */
    @GET("/topics/{id_or_slug}/photos")
    suspend fun getTopicPhotos(
        @Path("id_or_slug") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String,
    ): Response<List<TopicPhoto>>
}
