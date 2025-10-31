package com.kimothorick.plashr.profile.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Me(
    val id: String,
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
    @SerializedName("followed_by_user") val followedByUser: Boolean?,
    @SerializedName("followers_count") val followersCount: Int?,
    @SerializedName("following_count") val followingCount: Int?,
    val downloads: Int?,
    val social: Social?,
    @SerializedName("profile_image") val profileImage: ProfileImage?,
    val badge: Badge?,
    val email: String?,
    val links: Links?,
    val tags: UserTags?,
) : Parcelable {
    @Parcelize
    data class Social(
        @SerializedName("instagram_username") val instagramUsername: String?,
        @SerializedName("portfolio_url") val portfolioUrl: String?,
        @SerializedName("twitter_username") val twitterUsername: String?,
    ) : Parcelable

    @Parcelize
    data class ProfileImage(
        val small: String?,
        val medium: String?,
        val large: String?,
    ) : Parcelable

    @Parcelize
    data class Badge(
        val title: String?,
        val primary: Boolean?,
        val slug: String?,
        val link: String?,
    ) : Parcelable

    @Parcelize
    data class Links(
        val self: String?,
        val html: String?,
        val photos: String?,
        val likes: String?,
        val portfolio: String?,
    ) : Parcelable

    @Parcelize
    data class UserTags(
        val custom: List<UserCustomTag>,
    ) : Parcelable {
        @Parcelize
        data class UserCustomTag(
            val type: String?,
            val title: String?,
        ) : Parcelable
    }

    @Parcelize
    data class Photos(
        val id: String,
        val slug: String?,
        @SerializedName("created_at") val createdAt: String?,
        @SerializedName("blur_hash") val blurHash: String?,
        val description: String?,
        val urls: Urls?,
        val links: Links?,
    ) : Parcelable {
        @Parcelize
        data class Urls(
            val raw: String?,
            val full: String?,
            val regular: String?,
            val small: String?,
            val thumb: String?,
        ) : Parcelable
    }
}
