package com.kimothorick.plashr.data.models.photo

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.kimothorick.plashr.data.models.user.User

@Parcelize
data class Photo(
    val id: String,
    val created_at: String?,
    val updated_at: String?,
    val width: Int?,
    val height: Int?,
    val color: String?,
    val blur_hash: String?,
    val downloads: Int?,
    val likes: Int?,
    val liked_by_user: Boolean?,
    val description: String?,
    val exif: Exif?,
    val location: Location?,
    val tags: List<Tag?>?,
    val current_user_collections: List<CurrentUserCollection?>?,
    val sponsorship: Sponsorship?,
    val urls: Urls?,
    val links: Links?,
    val user: User?
) : Parcelable {
    @Parcelize
    data class Exif(
        val make: String?,
        val model: String?,
        val name: String?,
        val exposure_time: String?,
        val aperture: String?,
        val focal_length: String?,
        val iso: Int?
    ) : Parcelable

    @Parcelize
    data class Location(
        val city: String?, val country: String?, val position: Position?
    ) : Parcelable {
        @Parcelize
        data class Position(
            val latitude: Double?, val longitude: Double?
        ) : Parcelable
    }

    @Parcelize
    data class Tag(
        val title: String?
    ) : Parcelable

    @Parcelize
    data class CurrentUserCollection(
        val id: Int?,
        val title: String?,
        val published_at: String?,
        val last_collected_at: String?,
        val updated_at: String?,
        val cover_photo: Photo?,
        val user: User?
    ) : Parcelable

    @Parcelize
    data class Urls(
        val raw: String?, val full: String?, val regular: String?, val small: String?, val thumb: String?
    ) : Parcelable

    @Parcelize
    data class Links(
        val self: String?, val html: String?, val download: String?, val download_location: String?
    ) : Parcelable

    @Parcelize
    data class Sponsorship(
        val tagline: String?,
        val tagline_url: String?,
        val sponsor: User?
    ) : Parcelable

}