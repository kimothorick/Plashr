package com.kimothorick.plashr.data.models.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

val User.profile_share_url: String
    get() = "${links!!.html}?utm_source=plashr_app&utm_medium=referral"

@Parcelize
@Serializable
data class User(
    val id: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    val username: String?,
    val name: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("instagram_username") val instagramUsername: String?,
    @SerializedName("twitter_username") val twitterUsername: String?,
    @SerializedName("portfolio_url") val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    @SerializedName("total_likes") val totalLikes: Int?,
    @SerializedName("total_photos") val totalPhotos: Int?,
    @SerializedName("total_collections") val totalCollections: Int?,
    val downloads: Int?,
    val social: Social?,
    @SerializedName("profile_image") val profileImage: ProfileImage?,
    val badge: Badge?,
    val links: Links?,
    val tags: UserTags?,
) : Parcelable {
    @Parcelize
    @Serializable
    data class Social(
        @SerializedName("instagram_username") val instagramUsername: String?,
        @SerializedName("portfolio_url") val portfolioUrl: String?,
        @SerializedName("twitter_username") val twitterUsername: String?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class ProfileImage(
        val small: String?,
        val medium: String?,
        val large: String?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class Badge(
        val title: String?,
        val primary: Boolean?,
        val slug: String?,
        val link: String?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class Links(
        val self: String?,
        val html: String?,
        val photos: String?,
        val likes: String?,
        val portfolio: String?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class UserTags(
        val custom: List<UserCustomTag>,
    ) : Parcelable {
        @Parcelize
        @Serializable
        data class UserCustomTag(
            val type: String?,
            val title: String?,
        ) : Parcelable
    }
}
