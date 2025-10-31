package com.kimothorick.plashr.data.models.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SearchUsersResponse(
    @SerializedName("results") val results: List<Result?>?,
    @SerializedName("total") val total: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
) : Parcelable {
    @Parcelize
    @Serializable
    data class Result(
        @SerializedName("first_name") val firstName: String?,
        @SerializedName("id") val id: String?,
        @SerializedName("instagram_username") val instagramUsername: String?,
        @SerializedName("last_name") val lastName: String?,
        @SerializedName("links") val links: Links?,
        @SerializedName("name") val name: String?,
        @SerializedName("portfolio_url") val portfolioUrl: String?,
        @SerializedName("profile_image") val profileImage: ProfileImage?,
        @SerializedName("total_collections") val totalCollections: Int?,
        @SerializedName("total_likes") val totalLikes: Int?,
        @SerializedName("total_photos") val totalPhotos: Int?,
        @SerializedName("twitter_username") val twitterUsername: String?,
        @SerializedName("username") val username: String?,
    ) : Parcelable {
        @Parcelize
        @Serializable
        data class Links(
            @SerializedName("html") val html: String?,
            @SerializedName("likes") val likes: String?,
            @SerializedName("photos") val photos: String?,
            @SerializedName("self") val self: String?,
        ) : Parcelable

        @Parcelize
        @Serializable
        data class ProfileImage(
            @SerializedName("large") val large: String?,
            @SerializedName("medium") val medium: String?,
            @SerializedName("small") val small: String?,
        ) : Parcelable
    }
}
