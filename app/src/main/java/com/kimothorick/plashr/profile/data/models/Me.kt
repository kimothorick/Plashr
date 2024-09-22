package com.kimothorick.plashr.profile.data.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Me(
    val id: String,
    val updated_at: String?,
    val username: String?,
    val name: String?,
    val first_name: String?,
    val last_name: String?,
    val instagram_username: String?,
    val twitter_username: String?,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val total_likes: Int?,
    val total_photos: Int?,
    val total_collections: Int?,
    val followed_by_user: Boolean?,
    val followers_count: Int?,
    val following_count: Int?,
    val downloads: Int?,
    val social: Social?,
    val profile_image: ProfileImage?,
    val badge: Badge?,
    val links: Links?,
    val tags: UserTags?
) : Parcelable {
    @Parcelize
    data class Social(
        val instagram_username: String?, val portfolio_url: String?, val twitter_username: String?
    ) : Parcelable

    @Parcelize
    data class ProfileImage(
        val small: String?, val medium: String?, val large: String?
    ) : Parcelable

    @Parcelize
    data class Badge(
        val title: String?, val primary: Boolean?, val slug: String?, val link: String?
    ) : Parcelable

    @Parcelize
    data class Links(
        val self: String?,
        val html: String?,
        val photos: String?,
        val likes: String?,
        val portfolio: String?
    ) : Parcelable

    @Parcelize
    data class UserTags(
        val custom: List<UserCustomTag>
    ) : Parcelable {
        @Parcelize
        data class UserCustomTag(
            val type: String?, val title: String?
        ) : Parcelable
    }

    @Parcelize
    data class Photos(
        val id: String,
        val slug: String?,
        val created_at: String?,
        val blur_hash: String?,
        val description: String?,
        val urls: Urls?,
        val links: Links?
    ) : Parcelable {
        @Parcelize
        data class Urls(
            val raw: String?,
            val full: String?,
            val regular: String?,
            val small: String?,
            val thumb: String?
        ) : Parcelable
    }
}
