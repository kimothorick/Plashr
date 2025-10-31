package com.kimothorick.plashr.collectionDetails.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.collections.presentation.components.EditCollectionBottomSheet
import com.kimothorick.plashr.collections.presentation.components.EditCollectionState
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.Collection
import com.kimothorick.plashr.data.models.collection.sharedLink
import com.kimothorick.plashr.home.presentation.components.PhotoCardItem
import com.kimothorick.plashr.home.presentation.components.PhotoItemData
import com.kimothorick.plashr.home.presentation.components.PhotoListItem
import com.kimothorick.plashr.profile.presentation.ProfileViewModel
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.ui.common.PhotoLayoutItemShimmer
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.ErrorView
import com.kimothorick.plashr.ui.components.InlineErrorView

import com.kimothorick.plashr.utils.shareLinkIntent
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import java.text.NumberFormat

/**
 * A composable that displays the details of a specific collection, including its metadata
 * and a paginated list of its photos. It handles loading, error, and success states for
 * both the collection details and the photos within it.
 *
 * This screen features a collapsing app bar, pull-to-refresh functionality, and a dynamic
 * layout for the photo grid based on user settings. If the logged-in user is the owner
 * of the collection, a floating action button is provided to edit or delete the collection.
 *
 * @param collectionId The unique identifier for the collection to display.
 * @param modifier The [Modifier] to be applied to the screen.
 * @param mainViewModel The [MainViewModel] instance for accessing global app configurations.
 * @param settingsViewModel The [SettingsViewModel] instance for accessing user-defined layout preferences.
 * @param collectionDetailsViewModel The [CollectionDetailsViewModel] instance that manages the state and logic for this screen.
 * @param onPhotoClicked A callback lambda that is invoked when a photo in the collection is clicked, providing the photo's ID.
 * @param onBackClicked A callback lambda that is invoked when the back button is clicked. The boolean parameter indicates if the navigation was triggered by a specific event (e.g., after deleting a collection).
 * @param onUserClicked A callback lambda that is invoked when the collection's creator's profile is clicked, providing the user's username.
 * @param profileViewModel The [ProfileViewModel] instance to get the logged-in user's information.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CollectionDetailsScreen(
    collectionId: String,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    collectionDetailsViewModel: CollectionDetailsViewModel,
    onPhotoClicked: (String) -> Unit,
    onBackClicked: (Boolean) -> Unit,
    onUserClicked: (String) -> Unit,
    profileViewModel: ProfileViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val uiState by collectionDetailsViewModel.collectionUiState.collectAsStateWithLifecycle()
    val collectionPhotos = collectionDetailsViewModel.collectionPhotosFlow.collectAsLazyPagingItems()
    val photosLayoutType by settingsViewModel.photoLayout.collectAsStateWithLifecycle()
    val appConfig by mainViewModel.appConfig.collectAsStateWithLifecycle()
    val layoutConfig = appConfig.layoutConfig
    val gridSpacing = appConfig.gridSpacing
    val adaptiveMinSize = appConfig.adaptiveMinSize
    val horizontalContentPadding = layoutConfig.photoContentPadding * 2 // padding for both sides
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val pullToRefreshState = rememberPullToRefreshState()
    val loggedInUsername = profileViewModel.username.collectAsStateWithLifecycle("")
    val hapticFeedback = LocalHapticFeedback.current
    val showTitleInAppBar = lazyStaggeredGridState.firstVisibleItemIndex > 0 || lazyStaggeredGridState.firstVisibleItemScrollOffset > 0
    val isScrollEnabled = uiState !is CollectionUiState.Loading && collectionPhotos.loadState.refresh !is LoadState.Loading
    val isRefreshing by collectionDetailsViewModel.isRefreshingAction.collectAsStateWithLifecycle()
    val showRefreshIndicator =
        isRefreshing && uiState is CollectionUiState.Success && (collectionPhotos.loadState.refresh is LoadState.NotLoading)
    val onRefresh: () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
        collectionDetailsViewModel.refreshCollection()
        collectionPhotos.refresh()
    }
    val showEditButton by remember {
        derivedStateOf {
            !lazyStaggeredGridState.lastScrolledForward
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val editCollectionState by collectionDetailsViewModel.editCollectionState.collectAsStateWithLifecycle()
    val showEditCollectionSheet by collectionDetailsViewModel.showEditCollectionSheet.collectAsStateWithLifecycle()
    val editCollectionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val isOwner = (
        uiState is CollectionUiState.Success &&
            (uiState as CollectionUiState.Success).collection.user?.username == loggedInUsername.value
    )

    if (showEditCollectionSheet) {
        EditCollectionBottomSheet(
            sheetState = editCollectionSheetState,
            initialCollectionState = uiState,
            editCollectionState = editCollectionState,
            onDismiss = {
                collectionDetailsViewModel.onEditCollectionDismissed()
            },
            onSave = { name, description, isPrivate ->
                coroutineScope.launch {
                    collectionDetailsViewModel.updateCollection(
                        title = name,
                        description = description,
                        isPrivate = isPrivate,
                    )
                }
            },
            onDelete = {
                collectionDetailsViewModel.updateEditCollectionState(EditCollectionState.Delete)
            },
            onConfirmDelete = {
                coroutineScope.launch {
                    collectionDetailsViewModel.deleteCollection()
                }
            },
            reset = {
                collectionDetailsViewModel.updateEditCollectionState(EditCollectionState.Idle)
            },
        )
    }

    LaunchedEffect(key1 = collectionId) {
        collectionDetailsViewModel.setCollectionId(collectionId = collectionId)
    }

    LaunchedEffect(key1 = Unit) {
        collectionDetailsViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.NavigateBack -> {
                    onBackClicked(true)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            CollectionsAppBar(
                scrollBehavior = scrollBehavior,
                onBackClicked = { onBackClicked(false) },
                collectionUiState = uiState,
                showTitleInAppBar = showTitleInAppBar,
            )
        },
        floatingActionButton = {
            if (isOwner) {
                AnimatedVisibility(
                    visible = showEditButton,
                    enter = slideInVertically(initialOffsetY = { it * 2 }),
                    exit = slideOutVertically(targetOffsetY = { it * 2 }),
                ) {
                    FloatingActionButton(
                        onClick = {
                            collectionDetailsViewModel.onEditCollectionClicked()
                        },
                    ) {
                        Icon(Icons.Rounded.Edit, null)
                    }
                }
            }
        },
    ) { contentPadding ->
        PullToRefreshBox(
            isRefreshing = showRefreshIndicator,
            onRefresh = {
                onRefresh()
            },
            state = pullToRefreshState,
            indicator = {
                LoadingIndicator(
                    state = pullToRefreshState,
                    isRefreshing = showRefreshIndicator,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            },
            modifier = Modifier.padding(
                contentPadding,
            ),
        ) {
            when (uiState) {
                is CollectionUiState.Error -> {
                    val errorMessage = (uiState as CollectionUiState.Error).errorMessage
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ErrorView(
                            errorTitle = null,
                            errorMessage = errorMessage,
                            onRetry = {
                                collectionDetailsViewModel.refreshCollection()
                                collectionPhotos.retry()
                            },
                        )
                    }
                }

                else -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(minSize = adaptiveMinSize),
                        verticalItemSpacing = gridSpacing.verticalSpacing,
                        horizontalArrangement = Arrangement.spacedBy(gridSpacing.horizontalSpacing - horizontalContentPadding),
                        modifier = Modifier.fillMaxSize(),
                        state = lazyStaggeredGridState,
                        userScrollEnabled = isScrollEnabled,
                    ) {
                        when (uiState) {
                            is CollectionUiState.Success -> {
                                val collection = (uiState as CollectionUiState.Success).collection
                                val formattedDate = (uiState as CollectionUiState.Success).formattedDate

                                //region Collection Details
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    CollectionDetailsContent(
                                        collection = collection,
                                        date = formattedDate,
                                        userClicked = { username ->
                                            onUserClicked(username)
                                        },
                                        isCollectionOwner = isOwner,
                                    )
                                }
                                //endregion

                                //region Subheader
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                    ) {
                                        Spacer(modifier = modifier.height(16.dp))
                                        Text(
                                            text = stringResource(R.string.photos),
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                                //endregion

                                //region Collection Photos
                                when (val photosLoadState = collectionPhotos.loadState.refresh) {
                                    is LoadState.Loading -> {
                                        items(6) {
                                            PhotoLayoutItemShimmer(
                                                photoLayoutType = photosLayoutType,
                                                appConfig = appConfig,
                                            )
                                        }
                                    }

                                    is LoadState.Error -> {
                                        item(span = StaggeredGridItemSpan.FullLine) {
                                            Column(modifier = Modifier.fillMaxSize()) {
                                                InlineErrorView(
                                                    errorMessage = collectionDetailsViewModel.generatePhotoLoadErrorMessage(
                                                        photosLoadState.error,
                                                    ),
                                                    onRetry = {
                                                        collectionPhotos.retry()
                                                    },
                                                )
                                            }
                                        }
                                    }

                                    is LoadState.NotLoading -> {
                                        if (collectionPhotos.itemCount == 0) {
                                            item(span = StaggeredGridItemSpan.FullLine) {
                                                if (collection.totalPhotos != null && collection.totalPhotos > 0) {
                                                    val errorText = buildAnnotatedString {
                                                        append(stringResource(R.string.unsplash_plus_prompt))
                                                        append(" ")
                                                        withLink(
                                                            LinkAnnotation.Url(
                                                                Constants.PLASHR_UNSPLASH_PLUS_REFERRAL_URL,
                                                                TextLinkStyles(
                                                                    style = SpanStyle(
                                                                        color = MaterialTheme.colorScheme.onBackground,
                                                                        fontWeight = FontWeight.Bold,
                                                                    ),
                                                                ),
                                                            ),
                                                        ) {
                                                            append(stringResource(R.string.unsplash_plus_prompt_action))
                                                        }
                                                    }
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.layoutstaggeredgrid),
                                                            contentDescription = stringResource(R.string.no_photos_to_show),
                                                            modifier = Modifier.size(36.dp),
                                                            tint = MaterialTheme.colorScheme.onBackground,
                                                        )
                                                        Text(
                                                            text = errorText,
                                                            textAlign = TextAlign.Center,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                        )
                                                    }
                                                } else {
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.layoutstaggeredgrid),
                                                            contentDescription = stringResource(R.string.no_photos_to_show),
                                                            modifier = Modifier.size(36.dp),
                                                            tint = MaterialTheme.colorScheme.onBackground,
                                                        )
                                                        Text(
                                                            text = stringResource(R.string.no_photos_to_show),
                                                            textAlign = TextAlign.Center,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            items(count = collectionPhotos.itemCount) { index ->
                                                collectionPhotos[index]?.let { photo ->
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                horizontal = if (photosLayoutType == PhotoLayoutType.CARDS ||
                                                                    photosLayoutType == PhotoLayoutType.STAGGERED_GRID
                                                                ) {
                                                                    layoutConfig.photoContentPadding
                                                                } else {
                                                                    0.dp
                                                                },
                                                            ),
                                                    ) {
                                                        when (photosLayoutType) {
                                                            PhotoLayoutType.CARDS -> PhotoCardItem(
                                                                photoData = PhotoItemData.CollectionPhoto(
                                                                    photo,
                                                                ),
                                                                onPhotoClicked = {
                                                                    photo.id?.let { onPhotoClicked(it) }
                                                                },
                                                            )

                                                            else -> PhotoListItem(
                                                                photoData = PhotoItemData.CollectionPhoto(
                                                                    photo,
                                                                ),
                                                                onPhotoClick = {
                                                                    photo.id?.let { onPhotoClicked(it) }
                                                                },
                                                                photoLayoutConfig = layoutConfig,
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            if (collectionPhotos.loadState.append.endOfPaginationReached) {
                                                item(span = StaggeredGridItemSpan.FullLine) {
                                                    EndOfPagingComponent()
                                                }
                                            }

                                            // Handle additional load states for photos
                                            handleLoadStates(
                                                pagingItems = collectionPhotos,
                                                getErrorMessage = {
                                                    collectionDetailsViewModel.generatePhotoLoadErrorMessage(it)
                                                },
                                            )
                                        }
                                    }
                                }
                                //endregion
                            }

                            is CollectionUiState.Loading -> {
                                //region Loading Shimmer
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    CollectionDetailsContentShimmer()
                                }
                                items(6) {
                                    PhotoLayoutItemShimmer(
                                        photoLayoutType = photosLayoutType,
                                        appConfig = appConfig,
                                    )
                                }
                                //endregion
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

/**
 * A composable that displays the main content of a collection's details.
 *
 * This includes the collection's title, photo count, last update date, curator information,
 * and description. It also shows a lock icon if the collection is private and the current
 * user is the owner.
 *
 * @param collection The [Collection] data object to display. Can be null if data is not yet loaded.
 * @param date A formatted string representing the last update date of the collection.
 * @param userClicked A lambda that is invoked when the curator's profile image or name is clicked,
 * passing the curator's username.
 * @param isCollectionOwner A boolean indicating if the current user is the owner of the collection.
 * This is used to determine whether to show the privacy status icon.
 */
@Composable
fun CollectionDetailsContent(
    collection: Collection?,
    date: String?,
    userClicked: (String) -> Unit = {},
    isCollectionOwner: Boolean = false,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = collection?.title ?: "",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isCollectionOwner) {
                    Image(
                        imageVector = if (collection?.private == true) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    )
                }
                Text(
                    text = stringResource(
                        R.string.collection_photo_count,
                        NumberFormat.getInstance().format(collection?.totalPhotos) ?: "",
                        date ?: "",
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val curatorProfileImage = collection?.user?.profileImage?.medium
                if (curatorProfileImage != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (collection.user.username != null) {
                                    userClicked(collection.user.username)
                                }
                            },
                        model = curatorProfileImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = stringResource(R.string.collection_curator_profile_image),
                    )
                }
                Text(
                    text = stringResource(R.string.curated_by_user, collection?.user?.name ?: stringResource(R.string.curated_by)),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (collection?.description != null) {
            Text(
                text = collection.description.trim(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * A custom [TopAppBar] for the collection details screen.
 *
 * This app bar includes a back navigation button, a title that appears on scroll,
 * and actions to share the collection or open it on the web. The actions are only
 * visible when the collection data has loaded successfully.
 *
 * @param collectionUiState The current state of the collection details UI, used to access collection data.
 * @param scrollBehavior The scroll behavior that connects the app bar's appearance to the scroll state of the content.
 * @param showTitleInAppBar A boolean indicating whether to display the collection title in the app bar,
 * typically when the main title in the content has scrolled out of view.
 * @param onBackClicked A lambda to be invoked when the back navigation icon is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsAppBar(
    collectionUiState: CollectionUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    showTitleInAppBar: Boolean,
    onBackClicked: () -> Unit = {},
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val title = if (collectionUiState is CollectionUiState.Success && showTitleInAppBar) {
        collectionUiState.collection.title
    } else {
        ""
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_cd),
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        modifier = Modifier.wrapContentHeight(),
        actions = {
            if (collectionUiState is CollectionUiState.Success) {
                IconButton(
                    onClick = {
                        uriHandler.openUri(uri = collectionUiState.collection.sharedLink)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Public,
                        contentDescription = stringResource(R.string.open_on_browser),
                    )
                }
                IconButton(
                    onClick = {
                        shareLinkIntent(
                            linkToShare = collectionUiState.collection.sharedLink,
                            context = context,
                        )
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = stringResource(R.string.share_button_cd),
                    )
                }
            }
        },
    )
}

/**
 * A composable function that displays a shimmer loading effect for the Topic Details content.
 *
 * This function is used to provide a visual cue to the user that content is loading. It displays
 * placeholder boxes and rows that shimmer to indicate ongoing data fetching.
 *
 */
@Composable
fun CollectionDetailsContentShimmer() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column {
            Column(
                modifier = Modifier.shimmer(),
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
                text = stringResource(R.string.photos),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
