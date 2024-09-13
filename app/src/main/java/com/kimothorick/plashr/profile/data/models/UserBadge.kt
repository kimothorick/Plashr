package com.kimothorick.plashr.profile.data.models


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserBadge(
    val link: String, val primary: Boolean, val slug: String, val title: String
) : Parcelable
