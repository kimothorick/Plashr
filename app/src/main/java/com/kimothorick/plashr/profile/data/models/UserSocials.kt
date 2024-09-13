package com.kimothorick.plashr.profile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSocials(
    val instagram_username: String?,
    val portfolio_url: String?,
    val twitter_username: String?,
) : Parcelable