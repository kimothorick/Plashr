package com.kimothorick.plashr.data.models.photo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DownloadPhotoResponse(
    val url: String,
) : Parcelable
