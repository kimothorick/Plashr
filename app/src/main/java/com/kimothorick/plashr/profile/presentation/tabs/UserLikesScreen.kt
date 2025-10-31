package com.kimothorick.plashr.profile.presentation.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.kimothorick.plashr.data.models.user.UserPhotoLikes
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
 * A composable that displays a grid of photos liked by a user.
 * It handles different loading states such as loading, error, and empty content.
 * The layout of the photos can be customized.
 *
 * @param modifier The modifier to be applied to the component.
 * @param photoLayoutType The type of layout to use for displaying photos (e.g., CARDS, STAGGERED_GRID).
 * @param layoutConfig Configuration for the photo layout, such as padding.
 * @param appConfig General application configuration, including grid spacing and adaptive sizes.
 * @param padding Additional padding to adjust horizontal arrangement.
 * @param onPhotoClicked A callback function that is invoked when a photo is clicked, providing the photo's ID.
 * @param userLikedPhotos A [LazyPagingItems] of [UserPhotoLikes] to be displayed in the grid.
 * @param gridState The state object to be used for the staggered grid, allowing control over its position.
 * @param getErrorMessage A function to convert a [Throwable] into a user-friendly error message string.
 * @param onRetry A callback function to be invoked when the user clicks the retry button in an error state.
 */
@Composable
fun UserLikesScreen(
    modifier: Modifier = Modifier,
    photoLayoutType: PhotoLayoutType,
    layoutConfig: LayoutConfig,
    appConfig: AppConfig,
    padding: Dp,
    onPhotoClicked: (String) -> Unit,
    userLikedPhotos: LazyPagingItems<UserPhotoLikes>,
    gridState: LazyStaggeredGridState,
    getErrorMessage: (Throwable) -> String,
    onRetry: () -> Unit = {},
) {
    when (userLikedPhotos.loadState.refresh) {
        is LoadState.Error -> {
            val refreshError = userLikedPhotos.loadState.refresh as LoadState.Error
            Column(
                modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                InlineErrorView(
                    errorMessage = getErrorMessage(refreshError.error),
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
                modifier = modifier.fillMaxSize(),
                state = gridState,
                contentPadding = PaddingValues(top = 4.dp),
            ) {
                when (userLikedPhotos.loadState.refresh) {
                    is LoadState.Loading -> {
                        items(6) {
                            PhotoLayoutItemShimmer(
                                photoLayoutType = photoLayoutType,
                                appConfig = appConfig,
                            )
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (userLikedPhotos.itemCount == 0) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                EmptyStateComponent(stringResource(R.string.empty_user_likes_message))
                            }
                        } else {
                            items(
                                count = userLikedPhotos.itemCount,
                                key = { index ->
                                    val userLikedPhoto = userLikedPhotos.peek(index)
                                    "photo_${userLikedPhoto?.id ?: "no_id"}_$index"
                                },
                            ) { index ->
                                val userLikedPhoto = userLikedPhotos[index]
                                if (userLikedPhoto != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = if (photoLayoutType == PhotoLayoutType.CARDS ||
                                                    photoLayoutType == PhotoLayoutType.STAGGERED_GRID
                                                ) {
                                                    layoutConfig.photoContentPadding
                                                } else {
                                                    0.dp
                                                },
                                            ),
                                    ) {
                                        when (photoLayoutType) {
                                            PhotoLayoutType.CARDS -> PhotoCardItem(
                                                photoData = PhotoItemData.UserLikes(
                                                    userLikedPhoto,
                                                ),
                                                onPhotoClicked = {
                                                    userLikedPhoto.id?.let { onPhotoClicked(it) }
                                                },
                                            )

                                            else -> PhotoListItem(
                                                photoData = PhotoItemData.UserLikes(userLikedPhoto),
                                                onPhotoClick = {
                                                    userLikedPhoto.id?.let { onPhotoClicked(it) }
                                                },
                                                photoLayoutConfig = layoutConfig,
                                            )
                                        }
                                    }
                                }
                            }
                            if (userLikedPhotos.loadState.append.endOfPaginationReached) {
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    EndOfPagingComponent()
                                }
                            }
                        }
                    }

                    else -> {}
                }
                handleLoadStates(
                    pagingItems = userLikedPhotos,
                    getErrorMessage = {
                        getErrorMessage(it)
                    },
                )
            }
        }
    }
}
