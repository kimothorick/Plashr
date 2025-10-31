package com.kimothorick.plashr.common

import androidx.compose.ui.unit.dp

/**
 * A singleton object holding application-wide constants.
 *
 * This object centralizes various static values used throughout the Plashr app,
 * such as API endpoints, paging configurations, UI layout values, deep link URLs,
 * and developer profile information. Grouping these constants makes them easier
 * to manage and update.
 */
internal object Constants {
    object Paging {
        const val NETWORK_PAGE_SIZE = 30
        const val UNSPLASH_STARTING_PAGE_INDEX = 1
    }

    object UnsplashLinks {
        const val USER_PROFILE_DEEP_LINK_BASE_URL = "https://unsplash.com/@"
        const val PHOTOS_DEEP_LINK_BASE_URL = "https://unsplash.com/photos/"
        const val COLLECTION_DEEP_LINK_BASE_URL = "https://unsplash.com/collections/"
        const val TOPIC_DEEP_LINK_BASE_URL = "https://unsplash.com/t/"
    }

    object Profile {
        const val RICK_PROFILE_PICTURE_URL =
            "https://gravatar.com/avatar/8b8a63856ab36232cbe94be36567dca681bc043f85288292448cd631dd4ea226?size=256"
        const val RICK_NAME = "Rick Kimotho"
        const val RICK_THREADS = "https://www.threads.com/@kimothorick"
        const val RICK_GITHUB = "https://github.com/kimothorick"
        const val RICK_INSTAGRAM = "https://www.instagram.com/kimothorick/"
        const val RICK_BLUESKY = "https://bsky.app/profile/kimothorick.bsky.social"
    }

    object UnsplashLogin {
        const val AUTHORIZATION_URL = "https://unsplash.com/oauth/authorize"
        const val TOKEN_URL = "https://unsplash.com/oauth/token"
    }

    const val PLASHR_UNSPLASH_REFERRAL_URL = "https://unsplash.com/?utm_source=plashr_app&utm_medium=referral"
    const val PLASHR_UNSPLASH_PLUS_REFERRAL_URL = "https://unsplash.com/plus/?utm_source=plashr_app&utm_medium=referral"
    const val PLASHR_GITHUB_ISSUES_URL = "https://github.com/kimothorick/Plashr/issues"
    const val PLASHR_GITHUB_PRIVACY_POLICY_URL = "https://github.com/kimothorick/Plashr/blob/main/PRIVACY_POLICY.md"

    object LayoutValues {
        val MIN_ADAPTIVE_SIZE = 361.dp
        const val INITIAL_SHIMMER_COUNT = 6
        val COLLECTION_CARD_ADAPTIVE_MIN_SIZE = 357.dp
        const val MAX_NAME_LENGTH = 60
        const val MAX_DESCRIPTION_LENGTH = 250
        val BOTTOM_SHEET_MAX_WIDTH = 420.dp
        val CARD_IMAGE_HEIGHT = 230.dp
    }
}
