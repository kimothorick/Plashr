package com.kimothorick.plashr.profile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class UserCustomTag(
    val type: String,
    val title: String
) : Parcelable