package com.kimothorick.plashr.profile.presentation.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kimothorick.plashr.AppConfig
import com.kimothorick.plashr.LayoutConfig
import com.kimothorick.plashr.R
import com.kimothorick.plashr.data.models.user.UserPhoto
import com.kimothorick.plashr.home.presentation.components.PhotoCardItem
import com.kimothorick.plashr.home.presentation.components.PhotoItemData
import com.kimothorick.plashr.home.presentation.components.PhotoListItem
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.kimothorick.plashr.ui.common.PhotoLayoutItemShimmer
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EmptyStateComponent
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.InlineErrorView

/**
 * A composable that displays a grid of photos belonging to a specific user.
 * It handles loading, error, and empty states for paginated photo data.
 *
 * @param modifier The [Modifier] to be applied to the component.
 * @param photos The [LazyPagingItems] containing the user's photos to display.
 * @param photoLayout The [PhotoLayoutType] to determine how photos are rendered (e.g., cards, list).
 * @param layoutConfig Configuration for the layout of photo items.
 * @param appConfig General application configuration, like grid spacing.
 * @param padding Horizontal padding to adjust the grid spacing.
 * @param onPhotoClicked A callback lambda that is invoked when a photo is clicked, providing the photo's ID.
 * @param lazyStaggeredGridState The state object for the staggered grid, used to control and observe scroll position.
 * @param getErrorMessage A function to convert a [Throwable] into a user-friendly error message string.
 * @param onRetry A callback lambda to be invoked when the user requests to retry a failed data load.
 */
@Composable
fun UserPhotosScreen(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<UserPhoto>,
    photoLayout: PhotoLayoutType,
    layoutConfig: LayoutConfig,
    appConfig: AppConfig,
    padding: Dp,
    onPhotoClicked: (String) -> Unit,
    lazyStaggeredGridState: LazyStaggeredGridState,
    getErrorMessage: (Throwable) -> String,
    onRetry: () -> Unit = {},
) {
    when (val refreshLoadState = photos.loadState.refresh) {
        is LoadState.Error -> {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                InlineErrorView(
                    errorMessage = getErrorMessage(refreshLoadState.error),
                    onRetry = {
                        onRetry()
                    },
                )
            }
        }

        else -> {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = appConfig.adaptiveMinSize),
                verticalItemSpacing = appConfig.gridSpacing.verticalSpacing,
                horizontalArrangement = Arrangement.spacedBy(appConfig.gridSpacing.horizontalSpacing - padding),
                modifier = modifier,
                state = lazyStaggeredGridState,
                contentPadding = PaddingValues(top = 4.dp),
            ) {
                when (photos.loadState.refresh) {
                    is LoadState.Loading -> {
                        items(6) {
                            PhotoLayoutItemShimmer(
                                photoLayoutType = photoLayout,
                                appConfig = appConfig,
                            )
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (photos.itemCount == 0) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                EmptyStateComponent(stringResource(R.string.empty_user_photos_message))
                            }
                        } else {
                            items(
                                count = photos.itemCount,
                                key = { index ->
                                    val photo = photos.peek(index)
                                    "photo_${photo?.id ?: "no_id"}_$index"
                                },
                            ) { index ->
                                val photo = photos[index]
                                if (photo != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = if (photoLayout == PhotoLayoutType.CARDS ||
                                                    photoLayout == PhotoLayoutType.STAGGERED_GRID
                                                ) {
                                                    layoutConfig.photoContentPadding
                                                } else {
                                                    0.dp
                                                },
                                            ),
                                    ) {
                                        when (photoLayout) {
                                            PhotoLayoutType.CARDS -> PhotoCardItem(
                                                photoData = PhotoItemData.UserPhoto(
                                                    photo,
                                                ),
                                                onPhotoClicked = {
                                                    photo.id?.let { onPhotoClicked(it) }
                                                },
                                            )

                                            else -> PhotoListItem(
                                                photoData = PhotoItemData.UserPhoto(photo),
                                                onPhotoClick = {
                                                    photo.id?.let { onPhotoClicked(it) }
                                                },
                                                photoLayoutConfig = layoutConfig,
                                            )
                                        }
                                    }
                                }
                            }
                            if (photos.loadState.append.endOfPaginationReached) {
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    EndOfPagingComponent()
                                }
                            }
                        }
                    }

                    else -> {}
                }
                handleLoadStates(
                    pagingItems = photos,
                    getErrorMessage = {
                        getErrorMessage(it)
                    },
                )
            }
        }
    }
}
