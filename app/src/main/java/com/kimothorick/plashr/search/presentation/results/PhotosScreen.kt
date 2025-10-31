package com.kimothorick.plashr.search.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.kimothorick.plashr.data.models.photo.SearchPhotosResponse
import com.kimothorick.plashr.home.presentation.components.PhotoCardItem
import com.kimothorick.plashr.home.presentation.components.PhotoItemData
import com.kimothorick.plashr.home.presentation.components.PhotoListItem
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EmptyStateComponent
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.ErrorView

/**
 * A Composable that displays a grid of photos from a search result.
 * It handles various loading states from the Paging 3 library, including loading, error, and empty states.
 * The photos are displayed in a [LazyVerticalStaggeredGrid].
 *
 * @param modifier The modifier to be applied to the layout.
 * @param photos The [LazyPagingItems] containing the photo data to display.
 * @param layoutType The type of layout to use for displaying photos (e.g., CARDS, STAGGERED_GRID).
 * @param layoutConfig Configuration for the photo item layout.
 * @param appConfig General application configuration, including grid spacing.
 * @param padding Padding value used for calculating horizontal arrangement spacing.
 * @param lazyStaggeredGridState The state object to be used by the [LazyVerticalStaggeredGrid].
 * @param formatErrorMessage A lambda function to format a [Throwable] into a user-friendly error message string.
 * @param onPhotoSelected A callback invoked when a photo is selected, passing the photo's ID.
 * @param onRetry A callback invoked when the user clicks the retry button in an error state.
 */
@Composable
fun PhotosScreen(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<SearchPhotosResponse.Result>,
    layoutType: PhotoLayoutType,
    layoutConfig: LayoutConfig,
    appConfig: AppConfig,
    padding: Dp,
    lazyStaggeredGridState: LazyStaggeredGridState,
    formatErrorMessage: (Throwable) -> String,
    onPhotoSelected: (String) -> Unit,
    onRetry: () -> Unit,
) {
    when (val photosLoadState = photos.loadState.refresh) {
        is LoadState.Error -> {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState(),
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ErrorView(
                    errorTitle = null,
                    errorMessage = formatErrorMessage(photosLoadState.error),
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
                state = lazyStaggeredGridState,
                contentPadding = PaddingValues(top = 4.dp),
            ) {
                when (photos.loadState.refresh) {
                    is LoadState.Loading -> {
                    }

                    is LoadState.NotLoading -> {
                        if (photos.itemCount == 0) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                EmptyStateComponent(stringResource(R.string.empty_search_photos_message))
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
                                                horizontal = if (layoutType == PhotoLayoutType.CARDS ||
                                                    layoutType == PhotoLayoutType.STAGGERED_GRID
                                                ) {
                                                    layoutConfig.photoContentPadding
                                                } else {
                                                    0.dp
                                                },
                                            ),
                                    ) {
                                        when (layoutType) {
                                            PhotoLayoutType.CARDS -> PhotoCardItem(
                                                photoData = PhotoItemData.SearchPhoto(
                                                    photo,
                                                ),
                                                onPhotoClicked = {
                                                    photo.id?.let { onPhotoSelected(it) }
                                                },
                                            )

                                            else -> PhotoListItem(
                                                photoData = PhotoItemData.SearchPhoto(photo),
                                                onPhotoClick = {
                                                    photo.id?.let { onPhotoSelected(it) }
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
                        formatErrorMessage(it)
                    },
                )
            }
        }
    }
}
