package com.kimothorick.plashr.profile.data.models

import kotlinx.serialization.SerialName

data class UpdateUserProfileRequest(
    val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val location: String? = null,
    @SerialName("url") val portfolioUrl: String? = null,
    @SerialName("instagram_username") val instagramUsername: String? = null,
)
