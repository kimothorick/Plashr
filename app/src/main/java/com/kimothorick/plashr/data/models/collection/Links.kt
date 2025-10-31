package com.kimothorick.plashr.data.models.collection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Links(
    @SerialName("self")
    val self: String,
    @SerialName("html")
    val html: String,
    @SerialName("photos")
    val photos: String,
) : Parcelable
