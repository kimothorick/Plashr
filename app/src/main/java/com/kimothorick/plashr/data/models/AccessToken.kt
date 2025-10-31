package com.kimothorick.plashr.data.models

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    val scope: String,
    @SerializedName("created_at") val createdAt: Int,
)
