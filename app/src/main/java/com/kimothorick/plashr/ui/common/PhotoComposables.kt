package com.kimothorick.plashr.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kimothorick.plashr.AppConfig
import com.kimothorick.plashr.home.presentation.components.PhotoCardItem
import com.kimothorick.plashr.home.presentation.components.PhotoCardItemShimmer
import com.kimothorick.plashr.home.presentation.components.PhotoItemData
import com.kimothorick.plashr.home.presentation.components.PhotoListItem
import com.kimothorick.plashr.home.presentation.components.PhotoListItemShimmer
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.valentinilk.shimmer.shimmer

/**
 * A composable that displays a photo item in one of two layouts: card or list.
 * The layout is determined by the [photoLayoutType] parameter. This serves as a
 * wrapper to switch between [PhotoCardItem] and [PhotoListItem].
 *
 * @param modifier The [Modifier] to be applied to the layout.
 * @param photoLayoutType The type of layout to display, either [PhotoLayoutType.CARDS] or another type for a list view.
 * @param appConfig The application configuration, used to get layout-specific settings like padding.
 * @param photo The [PhotoItemData] containing the information about the photo to be displayed.
 * @param onPhotoClick A lambda to be invoked when the photo itself is clicked.
 * @param onUserClick A lambda to be invoked when the user's profile/name is clicked (only applicable in card layout).
 */
@Composable
fun PhotoLayoutItem(
    modifier: Modifier = Modifier,
    photoLayoutType: PhotoLayoutType,
    appConfig: AppConfig,
    photo: PhotoItemData,
    onPhotoClick: () -> Unit,
    onUserClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = appConfig.layoutConfig.photoContentPadding,
            ),
    ) {
        when (photoLayoutType) {
            PhotoLayoutType.CARDS -> {
                PhotoCardItem(
                    photoData = photo,
                    onUserProfileClicked = {
                        onUserClick()
                    },
                    onPhotoClicked = {
                        onPhotoClick()
                    },
                )
            }

            else -> {
                PhotoListItem(
                    photoData = photo,
                    onPhotoClick = {
                        onPhotoClick()
                    },
                    photoLayoutConfig = appConfig.layoutConfig,
                )
            }
        }
    }
}

/**
 * A composable that displays a shimmer loading placeholder for a photo item.
 * The specific shimmer layout (card or list item) is determined by [photoLayoutType].
 * This is used to indicate that photo data is being loaded.
 *
 * @param photoLayoutType The layout type to determine which shimmer placeholder to display.
 * @param appConfig The application configuration, used to apply consistent padding.
 */
@Composable
fun PhotoLayoutItemShimmer(
    photoLayoutType: PhotoLayoutType,
    appConfig: AppConfig,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = appConfig.layoutConfig.photoContentPadding,
            ),
    ) {
        when (photoLayoutType) {
            PhotoLayoutType.CARDS -> {
                PhotoCardItemShimmer()
            }

            else -> {
                PhotoListItemShimmer(
                    aspectRatio = 1f,
                    modifier = Modifier.shimmer(),
                )
            }
        }
    }
}
