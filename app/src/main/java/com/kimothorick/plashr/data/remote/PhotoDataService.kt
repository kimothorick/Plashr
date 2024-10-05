package com.kimothorick.plashr.data.remote

import com.kimothorick.plashr.data.models.photo.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoDataService {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Photo>>

}