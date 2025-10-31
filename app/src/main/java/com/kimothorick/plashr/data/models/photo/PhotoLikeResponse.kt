package com.kimothorick.plashr.data.models.photo

import com.google.gson.annotations.SerializedName

data class PhotoLikeResponse(
    val id: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val width: Long,
    val height: Long,
    val color: String,
    @SerializedName("blur_hash") val blurHash: String,
    val likes: Long,
    @SerializedName("liked_by_user") val likedByUser: Boolean,
    val description: String,
    val user: User,
    @SerializedName("current_user_collections") val currentUserCollections: List<CurrentUserCollection>,
    val urls: Urls,
    val links: Links2,
)

data class User(
    val id: String,
    val username: String,
    val name: String,
    @SerializedName("portfolio_url") val portfolioUrl: String,
    val bio: String,
    val location: String,
    @SerializedName("total_likes") val totalLikes: Long,
    @SerializedName("total_photos") val totalPhotos: Long,
    @SerializedName("total_collections") val totalCollections: Long,
    @SerializedName("instagram_username") val instagramUsername: String,
    @SerializedName("twitter_username") val twitterUsername: String,
    @SerializedName("profile_image") val profileImage: ProfileImage,
    val links: Links,
)

data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String,
)

data class Links(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String,
)

data class CurrentUserCollection(
    val id: Long,
    val title: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("last_collected_at") val lastCollectedAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("cover_photo") val coverPhoto: Any?,
    val user: Any?,
)

data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
)

data class Links2(
    val self: String,
    val html: String,
    val download: String,
    @SerializedName("download_location") val downloadLocation: String,
)
