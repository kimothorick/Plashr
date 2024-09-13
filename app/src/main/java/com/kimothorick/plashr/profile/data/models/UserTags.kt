package com.kimothorick.plashr.profile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTags(
    val custom: List<UserCustomTag>
) : Parcelable