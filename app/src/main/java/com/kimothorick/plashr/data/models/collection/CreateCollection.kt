package com.kimothorick.plashr.data.models.collection

import android.os.Parcelable
import com.kimothorick.plashr.data.models.user.User
import com.kimothorick.plashr.data.models.user.UserCollection
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CreateCollection(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("published_at")
    val publishedAt: String,
    @SerialName("last_collected_at")
    val lastCollectedAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("featured")
    val featured: Boolean,
    @SerialName("total_photos")
    val totalPhotos: Int,
    @SerialName("private")
    val `private`: Boolean,
    @SerialName("share_key")
    val shareKey: String,
    @SerialName("cover_photo")
    val coverPhoto: UserCollection.CoverPhoto,
    @SerialName("user")
    val user: User,
    @SerialName("links")
    val links: Links,
) : Parcelable
