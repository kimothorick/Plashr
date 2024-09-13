package com.kimothorick.plashr.profile.data.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserLinks(
    val html: String,
    val likes: String,
    val photos: String,
    val portfolio: String,
    val self: String
) : Parcelable