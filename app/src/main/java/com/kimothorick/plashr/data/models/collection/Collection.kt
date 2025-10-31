package com.kimothorick.plashr.data.models.collection

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.user.User
import kotlinx.parcelize.Parcelize

val Collection.sharedLink: String
    get() = "${links?.html}?utm_source=plashr_app&utm_medium=referral"

@Parcelize
data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("last_collected_at") val lastCollectedAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    val featured: Boolean?,
    @SerializedName("total_photos") val totalPhotos: Int?,
    val private: Boolean?,
    @SerializedName("share_key") val shareKey: String?,
    val links: Links?,
    val user: User?,
    @SerializedName("cover_photo") val coverPhoto: Photo?,
    @SerializedName("preview_photos") val previewPhotos: List<PreviewPhoto?>?,
) : Parcelable {
    @Parcelize
    data class Links(
        val self: String?,
        val html: String?,
        val photos: String?,
        val related: String?,
    ) : Parcelable

    @Parcelize
    data class PreviewPhoto(
        val id: String?,
        val slug: String?,
        @SerializedName("created_at") val createdAt: String?,
        @SerializedName("updated_at") val updatedAt: String?,
        @SerializedName("blur_hash") val blurHash: String?,
        @SerializedName("asset_type") val assetType: String?,
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
