package com.kimothorick.plashr.profile.remote

import com.kimothorick.plashr.profile.data.models.LoggedUserProfile
import retrofit2.http.GET

interface UserDataService {

        @GET("/me")
        suspend fun getLoggedUserProfile(): LoggedUserProfile

}
