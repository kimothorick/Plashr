package com.kimothorick.plashr.data.models.topics

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

val Topic.sharedLink: String
    get() = "${links?.html}?utm_source=plashr_app&utm_medium=referral"

@Parcelize
data class Topic(
    val id: String,
    val slug: String?,
    val title: String?,
    val description: String?,
    @SerializedName("published_at")val publishedAt: String?,
    @SerializedName("updated_at")val updatedAt: String?,
    @SerializedName("starts_at")val startsAt: String?,
    @SerializedName("ends_at")val endsAt: String?,
    @SerializedName("only_submissions_after")val onlySubmissionsAfter: String?,
    val visibility: String?,
    val featured: Boolean?,
    @SerializedName("total_photos")val totalPhotos: Int?,
    val links: Links?,
    val status: String?,
    val owners: List<Owner?>?,
    @SerializedName("top_contributors")val topContributors: List<TopContributor?>?,
    @SerializedName("cover_photo")val coverPhoto: CoverPhoto?,
    @SerializedName("media_types") val mediaTypes: List<String>?,
) : Parcelable {
    @Parcelize
    data class Links(
        val self: String?,
        val html: String?,
        val photos: String?,
    ) : Parcelable

    @Parcelize
    data class Owner(
        val id: String?,
        @SerializedName("updated_at")val updatedAt: String?,
        val username: String?,
        val name: String?,
        @SerializedName("first_name")val firstName: String?,
        @SerializedName("last_name")val lastName: String?,
        @SerializedName("twitter_username")val twitterUsername: String?,
        @SerializedName("portfolio_url")val portfolioUrl: String?,
        val bio: String?,
        val location: String?,
        val links: Links?,
        @SerializedName("profile_image")val profileImage: ProfileImage?,
        @SerializedName("instagram_username")val instagramUsername: String?,
        @SerializedName("total_collections")val totalCollections: Int?,
        @SerializedName("total_likes")val totalLikes: Int?,
        @SerializedName("total_photos")val totalPhotos: Int?,
        @SerializedName("accepted_tos")val acceptedTos: Boolean?,
    ) : Parcelable {
        @Parcelize
        data class Links(
            val self: String?,
            val html: String?,
            val photos: String?,
            val likes: String?,
            val portfolio: String?,
            val following: String?,
            val followers: String?,
        ) : Parcelable

        @Parcelize
        data class ProfileImage(
            val small: String?,
            val medium: String?,
            val large: String?,
        ) : Parcelable
    }

    @Parcelize
    data class TopContributor(
        val id: String?,
        @SerializedName("updated_at")val updatedAt: String?,
        val username: String?,
        val name: String?,
        @SerializedName("first_name")val firstName: String?,
        @SerializedName("last_name")val lastName: String?,
        @SerializedName("twitter_username")val twitterUsername: String?,
        @SerializedName("portfolio_url")val portfolioUrl: String?,
        val bio: String?,
        val location: String?,
        val links: Links?,
        @SerializedName("profile_image")val profileImage: ProfileImage?,
        @SerializedName("instagram_username")val instagramUsername: String?,
        @SerializedName("total_collections")val totalCollections: Int?,
        @SerializedName("total_likes")val totalLikes: Int?,
        @SerializedName("total_photos")val totalPhotos: Int?,
        @SerializedName("accepted_tos")val acceptedTos: Boolean?,
    ) : Parcelable {
        @Parcelize
        data class Links(
            val self: String?,
            val html: String?,
            val photos: String?,
            val likes: String?,
            val portfolio: String?,
            val following: String?,
            val followers: String?,
        ) : Parcelable

        @Parcelize
        data class ProfileImage(
            val small: String?,
            val medium: String?,
            val large: String?,
        ) : Parcelable
    }

    @Parcelize
    data class CoverPhoto(
        val id: String?,
        @SerializedName("created_at")val createdAt: String?,
        @SerializedName("updated_at")val updatedAt: String?,
        @SerializedName("promoted_at")val promotedAt: String?,
        val width: Int?,
        val height: Int?,
        val color: String?,
        @SerializedName("blur_hash")val blurHash: String?,
        val description: String?,
        @SerializedName("alt_description")val altDescription: String?,
        val urls: Urls?,
        val links: Links?,
        val user: User?,
        @SerializedName("preview_photos")val previewPhotos: List<PreviewPhoto?>?,
    ) : Parcelable {
        @Parcelize
        data class Urls(
            val raw: String?,
            val full: String?,
            val regular: String?,
            val small: String?,
            val thumb: String?,
        ) : Parcelable

        @Parcelize
        data class Links(
            val self: String?,
            val html: String?,
            val download: String?,
            @SerializedName("download_location")val downloadLocation: String?,
        ) : Parcelable

        @Parcelize
        data class User(
            val id: String?,
            @SerializedName("updated_at")val updatedAt: String?,
            val username: String?,
            val name: String?,
            @SerializedName("first_name")val firstName: String?,
            @SerializedName("last_name")val lastName: String?,
            @SerializedName("twitter_username")val twitterUsername: String?,
            @SerializedName("portfolio_url")val portfolioUrl: String?,
            val bio: String?,
            val location: String?,
            val links: Links?,
            @SerializedName("profile_image")val profileImage: ProfileImage?,
            @SerializedName("instagram_username")val instagramUsername: String?,
            @SerializedName("total_collections")val totalCollections: Int?,
            @SerializedName("total_likes")val totalLikes: Int?,
            @SerializedName("total_photos")val totalPhotos: Int?,
            @SerializedName("accepted_tos")val acceptedTos: Boolean?,
        ) : Parcelable {
            @Parcelize
            data class Links(
                val self: String?,
                val html: String?,
                val photos: String?,
                val likes: String?,
                val portfolio: String?,
                val following: String?,
                val followers: String?,
            ) : Parcelable

            @Parcelize
            data class ProfileImage(
                val small: String?,
                val medium: String?,
                val large: String?,
            ) : Parcelable
        }

        @Parcelize
        data class PreviewPhoto(
            val id: String?,
            @SerializedName("created_at")val createdAt: String?,
            @SerializedName("updated_at")val updatedAt: String?,
            val urls: Urls?,
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
}
