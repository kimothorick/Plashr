package com.kimothorick.plashr.data.models.collection

import android.os.Parcelable
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    val published_at: String?,
    val last_collected_at: String?,
    val updated_at: String?,
    val featured: Boolean?,
    val total_photos: Int?,
    val private: Boolean?,
    val share_key: String?,
    val links: Links?,
    val user: User?,
    val cover_photo: Photo?,
    val preview_photos: List<PreviewPhoto?>?
) : Parcelable {
    @Parcelize
    data class Links(
        val self: String?,
        val html: String?,
        val photos: String?,
        val related: String?
    ) : Parcelable

    @Parcelize
    data class PreviewPhoto(
        val id: String?,
        val slug: String?,
        val created_at: String?,
        val updated_at: String?,
        val blur_hash: String?,
        val asset_type: String?,
        val urls: Urls?
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