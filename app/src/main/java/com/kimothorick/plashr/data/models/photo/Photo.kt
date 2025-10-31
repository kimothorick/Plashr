package com.kimothorick.plashr.data.models.photo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kimothorick.plashr.data.models.collection.CurrentUserCollection
import com.kimothorick.plashr.data.models.user.User
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

val Photo.sharedLink: String?
    get() = "${links?.html}?utm_source=plashr_app&utm_medium=referral"

/**
 * Represents a photo object retrieved from the API.
 * This data class contains detailed information about a single photo, including
 * its metadata, URLs, user information, and related data.
 *
 * @property id The unique identifier for the photo.
 * @property createdAt The date and time when the photo was created.
 * @property updatedAt The date and time when the photo was last updated.
 * @property width The width of the photo in pixels.
 * @property height The height of the photo in pixels.
 * @property color The dominant color of the photo (e.g., "#E0E0E0").
 * @property blurHash A compact representation of the photo's blurred appearance.
 * @property downloads The number of times the photo has been downloaded.
 * @property likes The number of likes the photo has received.
 * @property likedByUser Indicates whether the current user has liked the photo.
 * @property description A textual description of the photo.
 * @property exif EXIF data of the photo, if available.
 * @property location Location information associated with the photo.
 * @property tags A list of tags associated with the photo.
 * @property currentUserCollections A list of collections the photo is part of for the current user.
 * @property sponsorship Sponsorship information for the photo, if applicable.
 * @property urls Different sizes and types of URLs for the photo.
 * @property links Hyperlinks related to the photo.
 * @property user Information about the user who uploaded the photo.
 */
@Parcelize
@Serializable
data class Photo(
    val id: String,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    @SerializedName("blur_hash") val blurHash: String?,
    val downloads: Int?,
    val likes: Int?,
    @SerializedName("liked_by_user") val likedByUser: Boolean?,
    val description: String?,
    val exif: Exif?,
    val location: Location?,
    val tags: List<Tag?>?,
    @SerializedName("current_user_collections") val currentUserCollections: List<CurrentUserCollection?>?,
    val sponsorship: Sponsorship?,
    val urls: Urls?,
    val links: Links?,
    val user: User?,
) : Parcelable {
    @Parcelize
    @Serializable
    data class Exif(
        val make: String?,
        val model: String?,
        val name: String?,
        @SerializedName("exposure_time") val exposureTime: String?,
        val aperture: String?,
        @SerializedName("focal_length") val focalLength: String?,
        val iso: Int?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class Location(
        val city: String?,
        val country: String?,
        val position: Position?,
    ) : Parcelable {
        @Parcelize
        @Serializable
        data class Position(
            val latitude: Double?,
            val longitude: Double?,
        ) : Parcelable
    }

    @Parcelize
    @Serializable
    data class Tag(
        val title: String?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class Urls(
        val raw: String?,
        val full: String?,
        val regular: String?,
        val small: String?,
        val thumb: String?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class Links(
        val self: String?,
        val html: String?,
        val download: String?,
        @SerializedName("download_location") val downloadLocation: String?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class Sponsorship(
        val tagline: String?,
        @SerializedName("tagline_url") val taglineUrl: String?,
        val sponsor: User?,
    ) : Parcelable
}
