package com.kimothorick.plashr.data.models.photo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kimothorick.plashr.data.models.collection.CurrentUserCollection
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PhotoCollectionResult(
    @SerializedName("photo") val photo: Photo?,
    @SerializedName("collection") val collection: Collection?,
    @SerializedName("user") val user: User?,
    @SerializedName("created_at") val createdAt: String?,
) : Parcelable {
    @Serializable
    @Parcelize
    data class Photo(
        @SerializedName("id") val id: String?,
        @SerializedName("created_at") val createdAt: String?,
        @SerializedName("updated_at") val updatedAt: String?,
        @SerializedName("width") val width: Int?,
        @SerializedName("height") val height: Int?,
        @SerializedName("color") val color: String?,
        @SerializedName("blur_hash") val blurHash: String?,
        @SerializedName("likes") val likes: Int?,
        @SerializedName("liked_by_user") val likedByUser: Boolean?,
        @SerializedName("description") val description: String?,
        @SerializedName("user") val user: User?,
        @SerializedName("current_user_collections") val currentUserCollections: List<CurrentUserCollection?>?,
        @SerializedName("urls") val urls: Urls?,
        @SerializedName("links") val links: Links?,
    ) : Parcelable {
        @Serializable
        @Parcelize
        data class Urls(
            @SerializedName("raw") val raw: String?,
            @SerializedName("full") val full: String?,
            @SerializedName("regular") val regular: String?,
            @SerializedName("small") val small: String?,
            @SerializedName("thumb") val thumb: String?,
        ) : Parcelable

        @Serializable
        @Parcelize
        data class Links(
            @SerializedName("self") val self: String?,
            @SerializedName("html") val html: String?,
            @SerializedName("download") val download: String?,
            @SerializedName("download_location") val downloadLocation: String?,
        ) : Parcelable
    }

    @Serializable
    @Parcelize
    data class Collection(
        @SerializedName("id") val id: String?,
        @SerializedName("title") val title: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("published_at") val publishedAt: String?,
        @SerializedName("last_collected_at") val lastCollectedAt: String?,
        @SerializedName("updated_at") val updatedAt: String?,
        @SerializedName("total_photos") val totalPhotos: Int?,
        @SerializedName("private") val `private`: Boolean?,
        @SerializedName("share_key") val shareKey: String?,
        @SerializedName("cover_photo") val coverPhoto: CoverPhoto?,
        @SerializedName("user") val user: User?,
        @SerializedName("links") val links: Links?,
    ) : Parcelable {
        @Serializable
        @Parcelize
        data class CoverPhoto(
            @SerializedName("id") val id: String?,
            @SerializedName("width") val width: Int?,
            @SerializedName("height") val height: Int?,
            @SerializedName("color") val color: String?,
            @SerializedName("blur_hash") val blurHash: String?,
            @SerializedName("user") val user: User?,
            @SerializedName("urls") val urls: Urls?,
            @SerializedName("links") val links: Links?,
        ) : Parcelable {
            @Serializable
            @Parcelize
            data class User(
                @SerializedName("id") val id: String?,
                @SerializedName("username") val username: String?,
                @SerializedName("name") val name: String?,
                @SerializedName("profile_image") val profileImage: ProfileImage?,
                @SerializedName("links") val links: Links?,
            ) : Parcelable {
                @Serializable
                @Parcelize
                data class ProfileImage(
                    @SerializedName("small") val small: String?,
                    @SerializedName("medium") val medium: String?,
                    @SerializedName("large") val large: String?,
                ) : Parcelable

                @Serializable
                @Parcelize
                data class Links(
                    @SerializedName("self") val self: String?,
                    @SerializedName("html") val html: String?,
                    @SerializedName("photos") val photos: String?,
                    @SerializedName("likes") val likes: String?,
                    @SerializedName("portfolio") val portfolio: String?,
                ) : Parcelable
            }

            @Serializable
            @Parcelize
            data class Urls(
                @SerializedName("full") val full: String?,
                @SerializedName("regular") val regular: String?,
                @SerializedName("small") val small: String?,
                @SerializedName("thumb") val thumb: String?,
            ) : Parcelable

            @Serializable
            @Parcelize
            data class Links(
                @SerializedName("self") val self: String?,
                @SerializedName("html") val html: String?,
                @SerializedName("download") val download: String?,
                @SerializedName("download_location") val downloadLocation: String?,
            ) : Parcelable
        }

        @Serializable
        @Parcelize
        data class User(
            @SerializedName("id") val id: String?,
            @SerializedName("updated_at") val updatedAt: String?,
            @SerializedName("username") val username: String?,
            @SerializedName("name") val name: String?,
            @SerializedName("bio") val bio: String?,
            @SerializedName("profile_image") val profileImage: ProfileImage?,
            @SerializedName("links") val links: Links?,
        ) : Parcelable {
            @Serializable
            @Parcelize
            data class ProfileImage(
                @SerializedName("small") val small: String?,
                @SerializedName("medium") val medium: String?,
                @SerializedName("large") val large: String?,
            ) : Parcelable

            @Serializable
            @Parcelize
            data class Links(
                @SerializedName("self") val self: String?,
                @SerializedName("html") val html: String?,
                @SerializedName("photos") val photos: String?,
                @SerializedName("likes") val likes: String?,
                @SerializedName("portfolio") val portfolio: String?,
            ) : Parcelable
        }

        @Serializable
        @Parcelize
        data class Links(
            @SerializedName("self") val self: String?,
            @SerializedName("html") val html: String?,
            @SerializedName("photos") val photos: String?,
        ) : Parcelable
    }

    @Serializable
    @Parcelize
    data class User(
        @SerializedName("id") val id: String?,
        @SerializedName("updated_at") val updatedAt: String?,
        @SerializedName("username") val username: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("profile_image") val profileImage: ProfileImage?,
        @SerializedName("links") val links: Links?,
    ) : Parcelable {
        @Serializable
        @Parcelize
        data class ProfileImage(
            @SerializedName("small") val small: String?,
            @SerializedName("medium") val medium: String?,
            @SerializedName("large") val large: String?,
        ) : Parcelable

        @Serializable
        @Parcelize
        data class Links(
            @SerializedName("self") val self: String?,
            @SerializedName("html") val html: String?,
            @SerializedName("photos") val photos: String?,
            @SerializedName("likes") val likes: String?,
            @SerializedName("portfolio") val portfolio: String?,
        ) : Parcelable
    }
}
