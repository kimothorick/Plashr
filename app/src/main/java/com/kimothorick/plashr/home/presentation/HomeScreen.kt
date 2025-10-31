package com.kimothorick.plashr.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowSizeClass
import com.kimothorick.plashr.AppConfig
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.home.presentation.components.PhotoItemData
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.topics.presentation.components.HomeTopicsCard
import com.kimothorick.plashr.topics.presentation.components.HomeTopicsCardShimmer
import com.kimothorick.plashr.ui.common.PhotoLayoutItem
import com.kimothorick.plashr.ui.common.PhotoLayoutItemShimmer
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.ErrorView
import com.kimothorick.plashr.ui.components.InlineErrorView

/**
 * A composable function that acts as the main entry point for the home screen.
 *
 * This function orchestrates the data flow from various ViewModels (`MainViewModel`, `HomeViewModel`,
 * `SettingsViewModel`) to the `HomeScreenContent` composable, which handles the actual UI rendering.
 * It collects state such as photos, topics, photo layout preferences, and app configuration,
 * and passes them down along with navigation callbacks.
 *
 * @param mainViewModel The ViewModel responsible for application-level state, such as `AppConfig`.
 * @param homeViewModel The ViewModel responsible for fetching and managing home screen data, like photos and topics.
 * @param settingsViewModel The ViewModel that provides user-specific settings, such as the preferred photo layout.
 * @param modifier The modifier to be applied to the root composable.
 * @param onPhotoClicked A lambda function to be invoked when a photo is clicked, providing the photo's ID.
 * @param onTopicClicked A lambda function to be invoked when a topic is clicked, providing the topic's ID.
 * @param onUserClicked A lambda function to be invoked when a user's profile is clicked, providing the username.
 * @param windowSizeClass The window size class, used to adapt the layout to different screen sizes. Defaults to the current window's adaptive info.
 */
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    onPhotoClicked: (String) -> Unit,
    onTopicClicked: (String) -> Unit,
    onUserClicked: (String) -> Unit,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    val photos = homeViewModel.photosFlow.collectAsLazyPagingItems()
    val topics = homeViewModel.topicsFlow.collectAsLazyPagingItems()
    val photoLayout by settingsViewModel.photoLayout.collectAsStateWithLifecycle()
    val appConfig by mainViewModel.appConfig.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier,
        appConfig = appConfig,
        photoLayout = photoLayout,
        topics = topics,
        photos = photos,
        getErrorMessage = { error -> homeViewModel.generateErrorMessage(error) },
        windowSizeClass = windowSizeClass,
        onPhotoClicked = onPhotoClicked,
        onTopicClicked = onTopicClicked,
        onUserClicked = onUserClicked,
    )
}

/**
 * A composable that displays the main content of the home screen.
 *
 * It features a pull-to-refresh container that wraps a staggered vertical grid.
 * The grid displays a horizontal list of topics, followed by a list of editorial photos.
 * The layout adapts to different screen sizes and handles loading, error, and content states
 * for both topics and photos.
 *
 * @param modifier The modifier to be applied to the root container.
 * @param appConfig The application's configuration, containing settings like grid spacing and item sizes.
 * @param photoLayout The layout type for displaying photos (e.g., Grid, Staggered).
 * @param topics A [LazyPagingItems] instance for the list of topics to be displayed.
 * @param photos A [LazyPagingItems] instance for the list of editorial photos.
 * @param getErrorMessage A lambda function to generate a user-friendly error message from a [Throwable].
 * @param onPhotoClicked A callback lambda that is invoked when a photo is clicked, providing the photo ID.
 * @param onTopicClicked A callback lambda that is invoked when a topic is clicked, providing the topic ID.
 * @param onUserClicked A callback lambda that is invoked when a user's avatar or name is clicked, providing the username.
 * @param windowSizeClass The [WindowSizeClass] of the current window, used for adaptive layouts.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    appConfig: AppConfig,
    photoLayout: PhotoLayoutType,
    topics: LazyPagingItems<Topic>,
    photos: LazyPagingItems<Photo>,
    getErrorMessage: (Throwable) -> String,
    onPhotoClicked: (String) -> Unit,
    onTopicClicked: (String) -> Unit,
    onUserClicked: (String) -> Unit,
    windowSizeClass: WindowSizeClass,
) {
    val gridSpacing = appConfig.gridSpacing
    val adaptiveGridItemMinimumWidth = appConfig.adaptiveMinSize
    val layoutConfig = appConfig.layoutConfig
    val horizontalContentPadding = layoutConfig.photoContentPadding * 2
    val photoRefreshState = photos.loadState.refresh
    val topicsRefreshState = topics.loadState.refresh
    val isScrollEnabled = photoRefreshState !is LoadState.Loading && topicsRefreshState !is LoadState.Loading
    val isPhotoAndTopicError = photoRefreshState is LoadState.Error && topicsRefreshState is LoadState.Error
    val pullToRefreshState = rememberPullToRefreshState()
    val hapticFeedback = LocalHapticFeedback.current

    val refreshAll: () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
        photos.refresh()
        topics.refresh()
    }

    if (isPhotoAndTopicError) {
        CombinedErrorView(
            errorMessage = getErrorMessage(photoRefreshState.error),
            photos = photos,
            topics = topics,
        )
        return
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = {
            refreshAll()
        },
        state = pullToRefreshState,
        indicator = {
            LoadingIndicator(
                state = pullToRefreshState,
                isRefreshing = isScrollEnabled,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
        modifier = modifier,
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = adaptiveGridItemMinimumWidth),
            verticalItemSpacing = gridSpacing.verticalSpacing,
            horizontalArrangement = Arrangement.spacedBy(gridSpacing.horizontalSpacing - horizontalContentPadding),
            modifier = modifier,
            userScrollEnabled = isScrollEnabled,
        ) {
            //region Topics Section
            item(span = StaggeredGridItemSpan.FullLine) {
                TopicsSection(
                    topics = topics,
                    onTopicClick = { topicId ->
                        onTopicClicked(topicId)
                    },
                    getErrorMessage = getErrorMessage,
                    windowSizeClass = windowSizeClass,
                )
            }
            //endregion

            //region Editorial Header
            item(span = StaggeredGridItemSpan.FullLine) {
                EditorialHeader(
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            //endregion

            //region Photos Content
            when (photoRefreshState) {
                is LoadState.Loading -> {
                    items(6) {
                        PhotoLayoutItemShimmer(
                            photoLayoutType = photoLayout,
                            appConfig = appConfig,
                        )
                    }
                }

                is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = layoutConfig.photoContentPadding),
                        ) {
                            ErrorView(
                                errorTitle = null,
                                errorMessage = getErrorMessage(photoRefreshState.error),
                                onRetry = { photos.retry() },
                            )
                        }
                    }
                }

                is LoadState.NotLoading -> {
                    items(
                        count = photos.itemCount,
                        key = { index ->
                            val currentPhoto = photos.peek(index)
                            "photo_${currentPhoto?.id ?: "no_id"}_$index"
                        },
                    ) { index ->
                        val currentPhoto = photos[index]
                        if (currentPhoto != null) {
                            val photoItemData = PhotoItemData.Photo(currentPhoto)
                            PhotoLayoutItem(
                                photoLayoutType = photoLayout,
                                appConfig = appConfig,
                                photo = photoItemData,
                                onUserClick = {
                                    currentPhoto.user?.username?.let { username ->
                                        onUserClicked(username)
                                    }
                                },
                                onPhotoClick = {
                                    onPhotoClicked(currentPhoto.id)
                                },
                            )
                        }
                    }

                    if (photos.loadState.append.endOfPaginationReached) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            EndOfPagingComponent()
                        }
                    }
                }
            }
            // Handle additional load states for photos
            handleLoadStates(
                pagingItems = photos,
                getErrorMessage = {
                    getErrorMessage(it)
                },
            )
            //endregion
        }
    }
}

/**
 * A composable function that displays a full-screen error view when both the photo and topic
 * data streams fail to load. It provides a single "retry" button that attempts to reload
 * both data streams simultaneously.
 *
 * @param errorMessage The error message to be displayed.
 * @param photos The [LazyPagingItems] for the photos, used to trigger a retry action.
 * @param topics The [LazyPagingItems] for the topics, used to trigger a retry action.
 */
@Composable
private fun CombinedErrorView(
    errorMessage: String,
    photos: LazyPagingItems<Photo>,
    topics: LazyPagingItems<Topic>,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        ErrorView(
            errorTitle = null,
            errorMessage = errorMessage,
            onRetry = {
                photos.retry()
                topics.retry()
            },
        )
    }
}

/**
 * A composable that displays a horizontal list of topics.
 *
 * This section includes a title and a `LazyRow` that shows topic cards. It handles
 * different load states:
 * - **Loading:** Displays a shimmer effect with placeholder cards.
 * - **Error:** Shows an inline error message with a retry button.
 * - **NotLoading:** Presents the actual list of topics.
 *
 * @param modifier The modifier to be applied to the `Column` that contains the section.
 * @param topics The `LazyPagingItems` containing the topic data to be displayed.
 * @param onTopicClick A lambda function to be invoked when a topic card is clicked,
 * passing the topic's ID.
 * @param windowSizeClass The `WindowSizeClass` to adapt the layout of topic cards.
 * @param getErrorMessage A function that takes a `Throwable` and returns a human-readable
 * error string to be displayed in case of an error.
 */
@Composable
fun TopicsSection(
    modifier: Modifier = Modifier,
    topics: LazyPagingItems<Topic>,
    onTopicClick: (String) -> Unit,
    windowSizeClass: WindowSizeClass,
    getErrorMessage: (Throwable) -> String,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.topics),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val topicsRefreshState = topics.loadState.refresh) {
            is LoadState.Loading -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    userScrollEnabled = false,
                ) {
                    items(5) {
                        HomeTopicsCardShimmer(windowSizeClass = windowSizeClass)
                    }
                }
            }

            is LoadState.Error -> {
                InlineErrorView(
                    errorMessage = getErrorMessage(topicsRefreshState.error),
                ) {
                    topics.retry()
                }
            }

            is LoadState.NotLoading -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(
                        count = topics.itemCount,
                        key = { index ->
                            val topic = topics.peek(index)
                            "topic_${topic?.id ?: "no_id"}_$index"
                        },
                    ) { index ->
                        val topic = topics[index]
                        if (topic != null) {
                            HomeTopicsCard(
                                title = topic.title!!,
                                photo = topic.coverPhoto!!,
                                onTopicClicked = {
                                    onTopicClick(topic.id)
                                },
                                windowSizeClass = windowSizeClass,
                            )
                        } else {
                            HomeTopicsCardShimmer(windowSizeClass = windowSizeClass)
                        }
                    }
                }
            }
        }
    }
}

/**
 * A composable that displays the header for the "Editorial" section.
 * This typically includes a title text and spacing.
 *
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
private fun EditorialHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.editorial),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
