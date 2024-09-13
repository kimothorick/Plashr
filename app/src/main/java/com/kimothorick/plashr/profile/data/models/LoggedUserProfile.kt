package com.kimothorick.plashr.profile.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoggedUserProfile(
    val id: String,
    val updated_at: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val twitter_username: String,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val total_likes: Int,
    val total_photos: Int,
    val total_collections: Int,
    val followed_by_user: Boolean,
    val profile_image: UserProfileImage,
    val downloads: Int,
    val social: UserSocials?,
    val uploads_remaining: Int,
    val instagram_username: String,
    val email: String?,
    val badge: UserBadge?,
    val tags: UserTags?,
) : Parcelable
