package com.kimothorick.plashr.data.models.topics


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topics(
    val id: String?,
    val slug: String?,
    val title: String?,
    val description: String?,
    val published_at: String?,
    val updated_at: String?,
    val starts_at: String?,
    val ends_at: String?,
    val only_submissions_after: String?,
    val visibility: String?,
    val featured: Boolean?,
    val total_photos: Int?,
    val links: Links?,
    val status: String?,
    val owners: List<Owner?>?,
    val cover_photo: CoverPhoto?
) : Parcelable {
    @Parcelize
    data class Links(
        val self: String?, val html: String?, val photos: String?
    ) : Parcelable

    @Parcelize
    data class Owner(
        val id: String?,
        val updated_at: String?,
        val username: String?,
        val name: String?,
        val first_name: String?,
        val last_name: String?,
        val twitter_username: String?,
        val portfolio_url: String?,
        val bio: String?,
        val location: String?,
        val links: Links?,
        val profile_image: ProfileImage?,
        val instagram_username: String?,
        val total_collections: Int?,
        val total_likes: Int?,
        val total_photos: Int?,
        val accepted_tos: Boolean?
    ) : Parcelable {
        @Parcelize
        data class Links(
            val self: String?,
            val html: String?,
            val photos: String?,
            val likes: String?,
            val portfolio: String?,
            val following: String?,
            val followers: String?
        ) : Parcelable

        @Parcelize
        data class ProfileImage(
            val small: String?, val medium: String?, val large: String?
        ) : Parcelable
    }

    @Parcelize
    data class CoverPhoto(
        val id: String?,
        val created_at: String?,
        val updated_at: String?,
        val promoted_at: String?,
        val width: Int?,
        val height: Int?,
        val color: String?,
        val blur_hash: String?,
        val description: String?,
        val alt_description: String?,
        val urls: Urls?,
        val links: Links?,
        val user: User?,
        val preview_photos: List<PreviewPhoto?>?
    ) : Parcelable {
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

        @Parcelize
        data class User(
            val id: String?,
            val updated_at: String?,
            val username: String?,
            val name: String?,
            val first_name: String?,
            val last_name: String?,
            val twitter_username: String?,
            val portfolio_url: String?,
            val bio: String?,
            val location: String?,
            val links: Links?,
            val profile_image: ProfileImage?,
            val instagram_username: String?,
            val total_collections: Int?,
            val total_likes: Int?,
            val total_photos: Int?,
            val accepted_tos: Boolean?
        ) : Parcelable {
            @Parcelize
            data class Links(
                val self: String?,
                val html: String?,
                val photos: String?,
                val likes: String?,
                val portfolio: String?,
                val following: String?,
                val followers: String?
            ) : Parcelable

            @Parcelize
            data class ProfileImage(
                val small: String?, val medium: String?, val large: String?
            ) : Parcelable
        }

        @Parcelize
        data class PreviewPhoto(
            val id: String?, val created_at: String?, val updated_at: String?, val urls: Urls?
        ) : Parcelable {
            @Parcelize
            data class Urls(
                val raw: String?,
                val full: String?,
                val regular: String?,
                val small: String?,
                val thumb: String?
            ) : Parcelable
        }
    }
}
