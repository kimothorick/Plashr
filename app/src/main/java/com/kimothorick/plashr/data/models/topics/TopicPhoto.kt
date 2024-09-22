package com.kimothorick.plashr.data.models.topics


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicPhoto(
    val id: String?,
    val created_at: String?,
    val updated_at: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    val blur_hash: String?,
    val likes: Int?,
    val liked_by_user: Boolean?,
    val description: String?,
    val user: User?,
    val urls: Urls?,
    val links: Links?
) : Parcelable {
    @Parcelize
    data class User(
        val id: String?,
        val username: String?,
        val name: String?,
        val portfolio_url: String?,
        val bio: String?,
        val location: String?,
        val total_likes: Int?,
        val total_photos: Int?,
        val total_collections: Int?,
        val instagram_username: String?,
        val twitter_username: String?,
        val profile_image: ProfileImage?,
        val links: Links?
    ) : Parcelable {
        @Parcelize
        data class ProfileImage(
            val small: String?,
            val medium: String?,
            val large: String?
        ) : Parcelable

        @Parcelize
        data class Links(
            val self: String?,
            val html: String?,
            val photos: String?,
            val likes: String?,
            val portfolio: String?
        ) : Parcelable
    }

    @Parcelize
    data class Urls(
        val raw: String?,
        val full: String?,
        val regular: String?,
        val small: String?,
        val thumb: String?
    ) : Parcelable

    @Parcelize
    data class Links(
        val self: String?,
        val html: String?,
        val download: String?,
        val download_location: String?
    ) : Parcelable
}