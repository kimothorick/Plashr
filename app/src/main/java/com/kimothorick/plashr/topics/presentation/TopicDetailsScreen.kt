package com.kimothorick.plashr.topics.presentation

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.kimothorick.data.PreviewTopic
import com.kimothorick.data.PreviewTopicNoCover
import com.kimothorick.data.PreviewTopicTwo
import com.kimothorick.plashr.AppConfig
import com.kimothorick.plashr.GridSpacing
import com.kimothorick.plashr.LayoutConfig
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.data.models.topics.sharedLink
import com.kimothorick.plashr.home.presentation.components.PhotoItemData.TopicPhoto
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.ui.common.CollapsingLayout
import com.kimothorick.plashr.ui.common.PhotoLayoutItem
import com.kimothorick.plashr.ui.common.PhotoLayoutItemShimmer
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.ErrorView
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.ui.theme.extendedColors
import com.kimothorick.plashr.utils.shareLinkIntent
import com.kimothorick.plashr.utils.toFormattedDate
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.NumberFormat

/**
 * Composable that displays the main content of the Topic Details screen.
 *
 * This function orchestrates the display of topic details, a paginated list of photos associated
 * with the topic, and handles various UI states such as loading, error, and success. It features
 * a collapsing header with the topic's cover photo and details, which transitions into a
 * standard top app bar as the user scrolls. It also includes pull-to-refresh functionality,
 * a bottom sheet for top contributors, and dynamic status bar coloring based on scroll position.
 *
 * @param topicId The unique identifier of the topic to display.
 * @param modifier The modifier to be applied to the component.
 * @param mainViewModel The [MainViewModel] instance for accessing global app configurations.
 * @param settingsViewModel The [SettingsViewModel] instance for accessing user settings like photo layout.
 * @param topicDetailsViewModel The [TopicDetailsViewModel] instance for fetching and managing topic-specific data.
 * @param onPhotoTapped A lambda function invoked when a photo in the grid is tapped, providing the photo's ID.
 * @param onNavigateBack A lambda function invoked when the user taps the back navigation icon.
 * @param onUserTapped A lambda function invoked when a user's profile (e.g., a top contributor) is tapped, providing the user's username.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopicDetailsScreenContent(
    topicId: String,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    topicDetailsViewModel: TopicDetailsViewModel,
    onPhotoTapped: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onUserTapped: (String) -> Unit,
) {
    val isDarkMode = isSystemInDarkTheme()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val topicState by topicDetailsViewModel.topicUiState.collectAsState()
    val photos = topicDetailsViewModel.topicPhotosFlow.collectAsLazyPagingItems()
    val photoLayout by settingsViewModel.photoLayout.collectAsStateWithLifecycle()
    val appConfig by mainViewModel.appConfig.collectAsStateWithLifecycle()
    val applicationLayoutConfig = appConfig.layoutConfig
    val gridSpacingConfig = appConfig.gridSpacing
    val adaptiveGridMinSize = appConfig.adaptiveMinSize
    val horizontalPhotoPadding = applicationLayoutConfig.photoContentPadding * 2
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    var collapsingLayoutProgress by remember { mutableFloatStateOf(0f) }

    val appBarBackgroundAlpha by remember {
        derivedStateOf {
            (collapsingLayoutProgress * 3f).coerceIn(0f, 1f)
        }
    }

    val appBarIconColorUnscrolled = Color.White
    val appBarIconColorScrolled = MaterialTheme.colorScheme.onSurfaceVariant

    val appBarIconColor by remember(appBarBackgroundAlpha, topicState) {
        derivedStateOf {
            val topicHasCoverPhoto = (topicState as? TopicUiState.Success)?.topic?.coverPhoto != null
            if (appBarBackgroundAlpha < 0.1f && topicHasCoverPhoto) {
                appBarIconColorUnscrolled
            } else if (topicState !is TopicUiState.Success && appBarBackgroundAlpha < 0.1f) {
                appBarIconColorScrolled
            } else {
                appBarIconColorScrolled
            }
        }
    }

    val appBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = appBarBackgroundAlpha),
        scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = appBarBackgroundAlpha),
        navigationIconContentColor = appBarIconColor,
        actionIconContentColor = appBarIconColor,
    )

    val isAppBarTitleVisible by remember {
        derivedStateOf {
            collapsingLayoutProgress > 0.3f
        }
    }

    LaunchedEffect(key1 = topicId) {
        topicDetailsViewModel.setTopicId(topicId)
    }

    val currentActivity = LocalActivity.current as? ComponentActivity

    val useLightStatusBar by remember(appBarIconColor) {
        derivedStateOf {
            appBarIconColor == appBarIconColorUnscrolled
        }
    }

    if (currentActivity != null) {
        DisposableEffect(useLightStatusBar) {
            currentActivity.enableEdgeToEdge(
                statusBarStyle = if (useLightStatusBar) {
                    SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { true },
                    )
                } else {
                    SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { it ->
                            isDarkMode
                        },
                    )
                },
            )
            onDispose {
                currentActivity.enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = {
                            isDarkMode
                        },
                    ),
                )
            }
        }
    }
    var showTopContributorsBottomSheet by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by topicDetailsViewModel.isRefreshingAction.collectAsStateWithLifecycle()
    val showRefreshIndicator =
        isRefreshing && topicState !is TopicUiState.Success && (photos.loadState.refresh is LoadState.NotLoading)
    val hapticFeedback = LocalHapticFeedback.current

    val refreshTopicAndPhotos: () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
        topicDetailsViewModel.refreshTopic()
        photos.retry()
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            AppBar(
                showTitle = isAppBarTitleVisible,
                scrollBehavior = scrollBehavior,
                navigateUp = {
                    onNavigateBack()
                },
                appBarColors = appBarColors,
                topicState = topicState,
            )
        },
    ) { contentPadding ->
        PullToRefreshBox(
            isRefreshing = showRefreshIndicator,
            onRefresh = {
                refreshTopicAndPhotos()
            },
            state = pullToRefreshState,
            indicator = {
                LoadingIndicator(
                    state = pullToRefreshState,
                    isRefreshing = showRefreshIndicator,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(
                            top = if (topicState is TopicUiState.Success &&
                                (topicState as TopicUiState.Success).topic.coverPhoto != null
                            ) {
                                contentPadding.calculateTopPadding()
                            } else {
                                0.dp
                            },
                        ),
                )
            },
            modifier = modifier
                .padding(
                    top = if (topicState is TopicUiState.Success && (topicState as TopicUiState.Success).topic.coverPhoto != null) {
                        0.dp
                    } else {
                        contentPadding.calculateTopPadding()
                    },
                    start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = contentPadding.calculateBottomPadding(),
                )
                .fillMaxSize(),
        ) {
            when (topicState) {
                is TopicUiState.Error -> {
                    val message = (topicState as TopicUiState.Error).errorMessage
                    TopicErrorContent(
                        message = message,
                        onTryAgain = {
                            topicDetailsViewModel.refreshTopic()
                            photos.retry()
                        },
                    )
                }

                else -> {
                    CollapsingLayout(
                        header = {
                            when (val state = topicState) {
                                is TopicUiState.Success -> {
                                    TopicDetailsContent(topic = state.topic)
                                }

                                else -> {}
                            }
                        },
                        body = {
                            LazyVerticalStaggeredGrid(
                                state = lazyStaggeredGridState,
                                columns = StaggeredGridCells.Adaptive(minSize = adaptiveGridMinSize),
                                verticalItemSpacing = gridSpacingConfig.verticalSpacing,
                                horizontalArrangement = Arrangement.spacedBy(
                                    gridSpacingConfig.horizontalSpacing - horizontalPhotoPadding,
                                ),
                                userScrollEnabled = topicState !is TopicUiState.Loading,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                when (val state = topicState) {
                                    is TopicUiState.Loading -> {
                                        item(span = StaggeredGridItemSpan.FullLine) {
                                            TopicDetailsContentShimmer()
                                        }

                                        items(6) {
                                            PhotoLayoutItemShimmer(
                                                photoLayoutType = photoLayout,
                                                appConfig = appConfig,
                                            )
                                        }
                                    }

                                    is TopicUiState.Success -> {
                                        val topic = state.topic
                                        val topContributors = topic.topContributors

                                        item(span = StaggeredGridItemSpan.FullLine) {
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 16.dp, end = 16.dp),
                                            ) {
                                                if (topic.description != null) {
                                                    val descriptionText = topic.description.trim()
                                                    if (descriptionText.isNotBlank()) {
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                                        ) {
                                                            Text(
                                                                text = stringResource(R.string.about),
                                                                style = MaterialTheme.typography.titleMedium,
                                                            )
                                                            Text(
                                                                text = descriptionText,
                                                                style = MaterialTheme.typography.bodySmall,
                                                                fontWeight = FontWeight.Normal,
                                                                overflow = TextOverflow.Ellipsis,
                                                            )
                                                        }
                                                    }
                                                }
                                                if (topContributors?.isEmpty() != true) {
                                                    TopicContributors(
                                                        topContributors = topContributors,
                                                        onContributorClicked = { showTopContributorsBottomSheet = true },
                                                    )
                                                }

                                                Text(
                                                    text = stringResource(R.string.photos),
                                                    style = MaterialTheme.typography.titleMedium,
                                                )
                                            }
                                        }

                                        if (topic.mediaTypes?.contains("illustration") == true) {
                                            item(span = StaggeredGridItemSpan.FullLine) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Build,
                                                        contentDescription = stringResource(R.string.no_photos_to_show),
                                                        modifier = Modifier.size(36.dp),
                                                        tint = MaterialTheme.colorScheme.onBackground,
                                                    )
                                                    Text(
                                                        text = stringResource(R.string.topic_contains_illustration_photos),
                                                        textAlign = TextAlign.Center,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                    )
                                                }
                                            }
                                        } else {
                                            when (photos.loadState.refresh) {
                                                is LoadState.Loading -> {
                                                    items(6) {
                                                        PhotoLayoutItemShimmer(
                                                            photoLayoutType = photoLayout,
                                                            appConfig = appConfig,
                                                        )
                                                    }
                                                }

                                                is LoadState.Error -> {
                                                    val errorState = photos.loadState.refresh as LoadState.Error
                                                    item(span = StaggeredGridItemSpan.FullLine) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(horizontal = applicationLayoutConfig.photoContentPadding),
                                                        ) {
                                                            ErrorView(
                                                                errorTitle = null,
                                                                errorMessage = topicDetailsViewModel.generatePhotoLoadErrorMessage(
                                                                    errorState.error,
                                                                ),
                                                                onRetry = {
                                                                    photos.retry()
                                                                },
                                                            )
                                                        }
                                                    }
                                                }

                                                is LoadState.NotLoading -> {
                                                    items(
                                                        count = photos.itemCount,
                                                        key = { index ->
                                                            val photo = photos[index]
                                                            "photo_${photo?.id ?: "no_id"}_$index"
                                                        },
                                                    ) { photoIndex ->
                                                        val photo = photos[photoIndex]

                                                        if (photo != null) {
                                                            PhotoLayoutItem(
                                                                photoLayoutType = photoLayout,
                                                                appConfig = appConfig,
                                                                photo = TopicPhoto(photo),
                                                                onUserClick = {
                                                                    photo.user?.username?.let {
                                                                        onUserTapped(it)
                                                                    }
                                                                },
                                                                onPhotoClick = {
                                                                    onPhotoTapped(photo.id)
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
                                        }
                                    }

                                    else -> {}
                                }

                                handleLoadStates(
                                    photos,
                                    getErrorMessage = {
                                        topicDetailsViewModel.generatePhotoLoadErrorMessage(
                                            it,
                                        )
                                    },
                                )
                            }
                        },
                        onProgress = { collapsingLayoutProgress = it },
                    )
                }
            }
        }

        if (showTopContributorsBottomSheet) {
            val topicUiStateSuccess = topicState as? TopicUiState.Success
            val topContributors = topicUiStateSuccess?.topic?.topContributors
            if (!topContributors.isNullOrEmpty()) {
                TopContributorsBottomSheet(
                    topContributors = topContributors.filterNotNull(),
                    onDismiss = { showTopContributorsBottomSheet = false },
                    coroutineScope = coroutineScope,
                    sheetState = sheetState,
                    userClicked = { username ->
                        showTopContributorsBottomSheet = false
                        onUserTapped(username)
                    },
                )
            }
        }
    }
}

/**
 * A composable that displays the main header content for the topic details screen.
 *
 * This component shows the topic's title, its curators, publication date, and total photo count.
 * If the topic has a cover photo, it is displayed as a background with a gradient overlay,
 * and the text color is set to white for contrast. If there is no cover photo, the content
 * is displayed with standard on-surface text colors. It also displays a "Featured" chip if
 * the topic is marked as featured.
 *
 * @param topic The [Topic] data object to display. If null, the composable will render with
 *              empty text fields.
 * @param modifier The modifier to be applied to the root [Box] composable.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopicDetailsContent(
    topic: Topic?,
    modifier: Modifier = Modifier,
) {
    val topicOwnersNames = topic?.owners?.mapNotNull { it?.name }?.joinToString(", ") ?: ""
    val formattedPublishedDate = topic?.publishedAt?.toFormattedDate()

    val coverPhotoBottomPadding = when (topic?.coverPhoto) {
        null -> 0.dp
        else -> 16.dp
    }
    val topicHeaderImageHeight = 325.dp

    val textColor = if (topic?.coverPhoto == null) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Color.White
    }
    Box(
        modifier = Modifier
            .padding(
                bottom = coverPhotoBottomPadding,
            )
            .then(modifier),
        contentAlignment = Alignment.BottomStart,
    ) {
        if (topic?.coverPhoto != null) {
            TopicHeaderImageWithOverlay(
                topicImageUrl = topic.coverPhoto.urls?.regular,
                contentDescription = null,
                height = topicHeaderImageHeight,
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = topic?.title ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    color = textColor,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = stringResource(R.string.curated_by_user, topicOwnersNames),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    fontSize = 12.sp,
                    color = textColor,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = stringResource(
                        R.string.category_date_photocount,
                        formattedPublishedDate ?: "",
                        NumberFormat.getInstance().format(topic?.totalPhotos) ?: "",
                    ),
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (topic?.featured == true) {
                Column(
                    modifier = Modifier
                        .clip(
                            shape = MaterialTheme.shapes.extraLargeIncreased,
                        )
                        .background(
                            color = extendedColors.featuredChipBackground,
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp,
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.featured),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = extendedColors.featuredChipTextColor,
                    )
                }
            }
        }
    }
}

/**
 * A composable that displays a horizontal list of a topic's top contributors.
 *
 * This component shows a "Top Contributors" title followed by a `LazyRow` of circular
 * profile images for each contributor. If the `topContributors` list is null or empty,
 * nothing is rendered. Each contributor's image is clickable, triggering the
 * `onContributorClicked` lambda, which is intended to open a more detailed view
 * like a bottom sheet.
 *
 * @param topContributors A list of [Topic.TopContributor] objects. The component will not be
 *   displayed if this list is null or empty.
 * @param onContributorClicked A lambda function that is invoked when any contributor's
 *   image is clicked.
 */
@Composable
fun TopicContributors(
    topContributors: List<Topic.TopContributor?>?,
    onContributorClicked: () -> Unit = {},
) {
    if (!topContributors.isNullOrEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.top_contributors),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy((-8).dp),
            ) {
                items(
                    count = topContributors.size,
                    key = { index -> topContributors[index]?.id ?: index },
                ) { contributorIndex ->
                    AsyncImage(
                        model = topContributors[contributorIndex]?.profileImage?.large,
                        contentDescription = "",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
                                CircleShape,
                            )
                            .clickable {
                                onContributorClicked()
                            },
                        contentScale = ContentScale.Crop,
                        placeholder = BrushPainter(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                ),
                            ),
                        ),
                    )
                }
            }
        }
    }
}

/**
 * A composable that displays a modal bottom sheet listing the top contributors for a topic.
 *
 * This function wraps the [TopContributorsBottomSheetContent] in a [ModalBottomSheet],
 * providing the standard bottom sheet behavior, including dismissal handling.
 * When dismissed (either by back press, clicking outside, or a swipe gesture), it launches a
 * coroutine to animate the sheet hiding and then calls the [onDismiss] lambda.
 *
 * @param onDismiss A lambda function invoked when the bottom sheet is requested to be dismissed.
 * @param coroutineScope A [CoroutineScope] used for managing the sheet's hide animation.
 * @param sheetState The state object that controls the visibility and behavior of the [ModalBottomSheet].
 * @param topContributors A list of [Topic.TopContributor] objects to display in the sheet.
 * @param userClicked A lambda function that is invoked when a contributor's item is clicked,
 *                    providing the contributor's username.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopContributorsBottomSheet(
    onDismiss: () -> Unit,
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
    topContributors: List<Topic.TopContributor>,
    userClicked: (String) -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                onDismiss()
            }
        },
        sheetState = sheetState,
        modifier = Modifier
            .padding(end = 8.dp, start = 8.dp)
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(Color.Transparent),
        containerColor = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.extraLarge,
        sheetMaxWidth = 420.dp,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
            shouldDismissOnClickOutside = true,
        ),
    ) {
        TopContributorsBottomSheetContent(
            topicContributors = topContributors,
            onContributorClick = userClicked,
        )
    }
}

/**
 * The content displayed inside the top contributors' modal bottom sheet.
 *
 * This composable presents a title and a vertically scrollable list ([LazyColumn])
 * of the topic's top contributors. Each item in the list shows the contributor's
 * profile image, name, and username. Tapping on a contributor's item triggers the
 * [onContributorClick] callback with their username.
 *
 * @param topicContributors A list of [Topic.TopContributor] objects to be displayed.
 * @param onContributorClick A lambda function that is invoked when a contributor is clicked,
 *                           providing their username as a `String`. Defaults to an empty lambda.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopContributorsBottomSheetContent(
    topicContributors: List<Topic.TopContributor>,
    onContributorClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.top_contributors),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
        )

        LazyColumn(
            modifier = Modifier
                .clip(MaterialTheme.shapes.largeIncreased)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (topicContributors.isNotEmpty()) {
                items(
                    count = topicContributors.size,
                    key = { index -> topicContributors[index].id ?: index },
                ) { index ->
                    if (topicContributors[index].profileImage != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 14.dp)
                                .clickable(
                                    onClick = {
                                        topicContributors[index].username?.let { username ->
                                            onContributorClick(username)
                                        }
                                    },
                                ),
                        ) {
                            AsyncImage(
                                model = topicContributors[index].profileImage?.large,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                            )
                            Column {
                                Text(
                                    text = topicContributors[index].name.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = "@${topicContributors[index].username}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Displays an image from a URL with a vertical gradient overlay.
 *
 * This composable is used to create a header image for the topic details screen.
 * It loads an image asynchronously using [AsyncImage], crops it to fill the
 * specified height, and draws a transparent-to-black vertical gradient over it.
 * This overlay helps ensure that text or other UI elements placed on top of the
 * image remain legible.
 *
 * @param topicImageUrl The URL of the image to display.
 * @param contentDescription A description of the image for accessibility.
 * @param height The fixed height for the image composable.
 */
@Composable
fun TopicHeaderImageWithOverlay(
    topicImageUrl: String?,
    contentDescription: String?,
    height: Dp,
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black), // Transparent to black gradient
    )
    AsyncImage(
        model = topicImageUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(height)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = gradient,
                )
            },
    )
}

/**
 * A configurable top app bar for the topic details screen.
 *
 * It includes a navigation icon to go back, an animated title that appears on scroll,
 * and actions to share the topic or open it in a browser. The colors and scroll behavior
 * are customizable. Actions are only shown when the topic data has been successfully loaded.
 *
 * @param showTitle A boolean that determines whether the topic title should be visible.
 * @param scrollBehavior A [TopAppBarScrollBehavior] that defines how the app bar interacts with scrolling content.
 * @param appBarColors The [TopAppBarColors] to apply to the app bar, controlling its background and content colors.
 * @param topicState The current state of the topic UI, used to access topic details like title and shared link.
 * @param navigateUp A lambda function invoked when the navigation (back) icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    showTitle: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    appBarColors: TopAppBarColors,
    topicState: TopicUiState,
    navigateUp: () -> Unit = {},
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    TopAppBar(
        title = {
            if (topicState is TopicUiState.Success && topicState.topic.title != null) {
                AnimatedVisibility(
                    visible = showTitle,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                ) {
                    Text(
                        text = topicState.topic.title,
                        fontSize = 20.sp,
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button_cd),
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = appBarColors,
        actions = {
            if (topicState is TopicUiState.Success) {
                IconButton(
                    onClick = {
                        uriHandler.openUri(uri = topicState.topic.sharedLink)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Public,
                        contentDescription = stringResource(R.string.open_on_browser),
                    )
                }
                IconButton(
                    onClick = {
                        shareLinkIntent(topicState.topic.sharedLink, context)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = stringResource(id = R.string.share_button_cd),
                    )
                }
            }
        },
    )
}

/**
 * A composable that displays a shimmer-based placeholder for the main content of the topic details screen.
 * This is used to indicate a loading state while the actual topic details and photos are being fetched.
 *
 * It mimics the layout of the actual content, showing placeholders for:
 * - The topic header image and details (`TopicHeaderImageWithOverlayShimmer`).
 * - The "About" section with several lines of placeholder text.
 * - The "Top Contributors" section with circular avatar placeholders.
 * - The "Photos" section title.
 *
 * @param modifier The modifier to be applied to the root Column composable.
 */
@Composable
fun TopicDetailsContentShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        TopicHeaderImageWithOverlayShimmer()
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.about),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .shimmer(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(MaterialTheme.shapes.medium)
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(MaterialTheme.shapes.medium)
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .clip(MaterialTheme.shapes.medium)
                        .height(20.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.top_contributors),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy((-8).dp),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .shimmer(),
            ) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
                                CircleShape,
                            ),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.photos),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

/**
 * A composable that displays a shimmer-based placeholder for the topic header image and details.
 * This is used to indicate a loading state while the actual topic data is being fetched.
 *
 * It consists of a large box for the image area with a shimmer effect, and several smaller
 * placeholder boxes at the bottom to represent the topic title and other metadata.
 *
 * @param modifier The modifier to be applied to the outer Box composable.
 * @param imageHeight The height of the placeholder image area. Defaults to 325.dp.
 */
@Composable
fun TopicHeaderImageWithOverlayShimmer(
    modifier: Modifier = Modifier,
    imageHeight: Dp = 325.dp,
) {
    Box(
        modifier = modifier
            .height(imageHeight)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .shimmer(),
        contentAlignment = Alignment.BottomStart,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(12.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(12.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                )
            }
        }
    }
}

/**
 * A composable that displays an error message with a retry button, centered on the screen.
 * This is typically shown when fetching topic details fails.
 *
 * @param message The error message to be displayed.
 * @param onTryAgain A lambda function to be invoked when the user clicks the "Try Again" button.
 */
@Composable
fun TopicErrorContent(
    message: String,
    onTryAgain: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ErrorView(
            errorTitle = null,
            errorMessage = message,
            onRetry = {
                onTryAgain()
            },
        )
    }
}

//region PREVIEWS
@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "AppBar - Scrolled, From PreviewTopic")
@Composable
fun AppBarScrolledPreview() {
    PlashrTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        AppBar(
            showTitle = true,
            scrollBehavior = scrollBehavior,
            navigateUp = {},
            appBarColors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            ),
            topicState = TopicUiState.Success(PreviewTopic),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "AppBar - Top, From PreviewTopic, Light Icons")
@Composable
fun AppBarTopPreview() {
    PlashrTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        AppBar(
            showTitle = true,
            scrollBehavior = scrollBehavior,
            navigateUp = {},
            appBarColors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            ),
            topicState = TopicUiState.Success(PreviewTopic),
        )
    }
}

@Preview(name = "TopicDetailsContent - From PreviewTopicTwo")
@Composable
fun TopicDetailsContentFullPreview() {
    PlashrTheme {
        val gradientColors = listOf(
            Color.Transparent,
            Color.Black,
        )
        TopicDetailsContent(
            topic = PreviewTopicTwo,
            modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                ),
            ),
        )
    }
}

@Preview(name = "TopicDetailsContent - From PreviewTopicNoCover")
@Composable
fun TopicDetailsContentMinimalPreview() {
    PlashrTheme {
        TopicDetailsContent(
            topic = PreviewTopicNoCover,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(
    name = "TopicDetailsContentShimmerPreview",
)
@Composable
fun TopicDetailsScreenShimmerPreview() {
    PlashrTheme {
        Surface {
            val appConfig = AppConfig(
                layoutConfig = LayoutConfig(
                    contentPadding = 16.dp,
                    gridEndPadding = 0.dp,
                    photoCornerRadius = false,
                    photoContentPadding = 16.dp,
                    headerContentPadding = 0.dp,
                ),
                gridSpacing = GridSpacing(0.dp, 0.dp, 0.dp, 0.dp),
                adaptiveMinSize = 361.dp,
            )

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = Constants.LayoutValues.MIN_ADAPTIVE_SIZE),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    TopicDetailsContentShimmer()
                }
                items(10) {
                    PhotoLayoutItemShimmer(
                        photoLayoutType = PhotoLayoutType.STAGGERED_GRID,
                        appConfig = appConfig,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(name = "TopContributorsBottomSheetContent Preview")
@Composable
fun TopContributorsBottomSheetContentPreview() {
    PlashrTheme {
        val topContributors = PreviewTopic.topContributors
        if (topContributors?.isNotEmpty() == true) {
            TopContributorsBottomSheetContent(
                topicContributors = topContributors.filterNotNull(),
            )
        }
    }
}
//endregion
