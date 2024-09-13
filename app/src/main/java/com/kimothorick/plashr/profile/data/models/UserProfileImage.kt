package com.kimothorick.plashr.profile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileImage(
    val small: String, val medium: String, val large: String
) : Parcelable