package com.kimothorick.plashr.data.models.collection

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SearchCollectionsResponse(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("results")
    val results: List<Result?>?,
) : Parcelable {
    @Parcelize
    @Serializable
    data class Result(
        @SerializedName("cover_photo")
        val coverPhoto: CoverPhoto?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("featured")
        val featured: Boolean?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("last_collected_at")
        val lastCollectedAt: String?,
        @SerializedName("links")
        val links: Links?,
        @SerializedName("private")
        val `private`: Boolean?,
        @SerializedName("published_at")
        val publishedAt: String?,
        @SerializedName("share_key")
        val shareKey: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("total_photos")
        val totalPhotos: Int?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("user")
        val user: User?,
    ) : Parcelable {
        @Parcelize
        @Serializable
        data class CoverPhoto(
            @SerializedName("blur_hash")
            val blurHash: String?,
            @SerializedName("color")
            val color: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("height")
            val height: Int?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("liked_by_user")
            val likedByUser: Boolean?,
            @SerializedName("likes")
            val likes: Int?,
            @SerializedName("links")
            val links: Links?,
            @SerializedName("urls")
            val urls: Urls?,
            @SerializedName("user")
            val user: User?,
            @SerializedName("width")
            val width: Int?,
        ) : Parcelable {
            @Parcelize
            @Serializable
            data class Links(
                @SerializedName("download")
                val download: String?,
                @SerializedName("html")
                val html: String?,
                @SerializedName("self")
                val self: String?,
            ) : Parcelable

            @Parcelize
            @Serializable
            data class Urls(
                @SerializedName("full")
                val full: String?,
                @SerializedName("raw")
                val raw: String?,
                @SerializedName("regular")
                val regular: String?,
                @SerializedName("small")
                val small: String?,
                @SerializedName("thumb")
                val thumb: String?,
            ) : Parcelable

            @Parcelize
            @Serializable
            data class User(
                @SerializedName("first_name")
                val firstName: String?,
                @SerializedName("id")
                val id: String?,
                @SerializedName("instagram_username")
                val instagramUsername: String?,
                @SerializedName("last_name")
                val lastName: String?,
                @SerializedName("links")
                val links: Links?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("portfolio_url")
                val portfolioUrl: String?,
                @SerializedName("profile_image")
                val profileImage: ProfileImage?,
                @SerializedName("twitter_username")
                val twitterUsername: String?,
                @SerializedName("username")
                val username: String?,
            ) : Parcelable {
                @Parcelize
                @Serializable
                data class Links(
                    @SerializedName("html")
                    val html: String?,
                    @SerializedName("likes")
                    val likes: String?,
                    @SerializedName("photos")
                    val photos: String?,
                    @SerializedName("self")
                    val self: String?,
                ) : Parcelable

                @Parcelize
                @Serializable
                data class ProfileImage(
                    @SerializedName("large")
                    val large: String?,
                    @SerializedName("medium")
                    val medium: String?,
                    @SerializedName("small")
                    val small: String?,
                ) : Parcelable
            }
        }

        @Parcelize
        @Serializable
        data class Links(
            @SerializedName("html")
            val html: String?,
            @SerializedName("photos")
            val photos: String?,
            @SerializedName("related")
            val related: String?,
            @SerializedName("self")
            val self: String?,
        ) : Parcelable

        @Parcelize
        @Serializable
        data class User(
            @SerializedName("bio")
            val bio: String?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("links")
            val links: Links?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("portfolio_url")
            val portfolioUrl: String?,
            @SerializedName("profile_image")
            val profileImage: ProfileImage?,
            @SerializedName("username")
            val username: String?,
        ) : Parcelable {
            @Parcelize
            @Serializable
            data class Links(
                @SerializedName("html")
                val html: String?,
                @SerializedName("likes")
                val likes: String?,
                @SerializedName("photos")
                val photos: String?,
                @SerializedName("self")
                val self: String?,
            ) : Parcelable

            @Parcelize
            @Serializable
            data class ProfileImage(
                @SerializedName("large")
                val large: String?,
                @SerializedName("medium")
                val medium: String?,
                @SerializedName("small")
                val small: String?,
            ) : Parcelable
        }
    }
}
