package com.kimothorick.plashr.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.kimothorick.plashr.AppConfig
import com.kimothorick.plashr.GridSpacing
import com.kimothorick.plashr.LayoutConfig
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.user.User
import com.kimothorick.plashr.data.models.user.profile_share_url
import com.kimothorick.plashr.profile.presentation.components.ManageAccountState
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import com.kimothorick.plashr.profile.presentation.tabs.UserCollectionsScreen
import com.kimothorick.plashr.profile.presentation.tabs.UserLikesScreen
import com.kimothorick.plashr.profile.presentation.tabs.UserPhotosScreen
import com.kimothorick.plashr.settings.presentation.PhotoLayoutType
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.ui.common.CollapsingLayout
import com.kimothorick.plashr.ui.common.PhotoLayoutItemShimmer
import com.kimothorick.plashr.ui.components.ConnectionStatus
import com.kimothorick.plashr.ui.components.ErrorView
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.utils.shareLinkIntent
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import me.saket.cascade.CascadeDropdownMenu

/**
 * A composable that displays a user's profile screen.
 *
 * This screen shows the user's information (profile picture, name, bio, location)
 * and a tabbed layout to browse their photos, liked photos, and collections.
 * It handles loading, error, and success states for the user profile data.
 * It also includes features like pull-to-refresh, a collapsing top app bar,
 * and options to manage the account if the profile belongs to the currently logged-in user.
 *
 * @param modifier The modifier to be applied to the screen.
 * @param username The username of the profile to display.
 * @param mainViewModel The [MainViewModel] instance for managing global UI state, like bottom sheets.
 * @param profileViewModel The [ProfileViewModel] for managing user session state (e.g., login status, current user's username).
 * @param userProfileScreenViewModel The [UserProfileScreenViewModel] for fetching and managing the state of the specific user profile being viewed.
 * @param settingsViewModel The [SettingsViewModel] to get user preferences like photo layout type.
 * @param onBackClicked A callback invoked when the back button is pressed.
 * @param onPhotoSelected A callback invoked with the photo ID when a photo is selected.
 * @param onCollectionSelected A callback invoked with the collection ID when a collection is selected.
 * @param onEditProfileClicked A callback invoked when the "Edit Profile" option is clicked.
 * @param navController The [NavHostController] for navigation and passing data between screens.
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalFoundationApi::class,
)
@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    username: String,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    userProfileScreenViewModel: UserProfileScreenViewModel,
    settingsViewModel: SettingsViewModel,
    onBackClicked: () -> Unit = {},
    onPhotoSelected: (String) -> Unit = {},
    onCollectionSelected: (String) -> Unit = {},
    onEditProfileClicked: () -> Unit = {},
    navController: NavHostController,
) {
    val photoLayoutType = settingsViewModel.photoLayout.collectAsStateWithLifecycle()
    val appConfig by mainViewModel.appConfig.collectAsStateWithLifecycle()
    val userProfileUiState by userProfileScreenViewModel.userProfileUiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState {
        Destination.entries.size
    }
    val scope = rememberCoroutineScope()
    val layoutConfig = appConfig.layoutConfig
    val photosContentPadding = layoutConfig.photoContentPadding * 2
    val userPhotosPagingItems = userProfileScreenViewModel.userPhotosFlow.collectAsLazyPagingItems()
    val likedPhotosPagingItems = userProfileScreenViewModel.userLikesFlow.collectAsLazyPagingItems()
    val userCollectionsPagingItems = userProfileScreenViewModel.userCollectionsFlow.collectAsLazyPagingItems()
    val userPhotosGridState = rememberLazyStaggeredGridState()
    val userCollectionsGridState = rememberLazyStaggeredGridState()
    val likedPhotosGridState = rememberLazyStaggeredGridState()
    val hapticFeedback = LocalHapticFeedback.current
    val pullRefreshState = rememberPullToRefreshState()
    val showRefreshIndicator = userProfileUiState !is UserProfileState.Loading
    val isOwnProfile by profileViewModel.isAppAuthorized.collectAsStateWithLifecycle()
    val currentUsername by profileViewModel.username.collectAsStateWithLifecycle("")
    val manageAccountState by profileViewModel.manageAccountState.collectAsStateWithLifecycle()
    val showManageAccountBottomSheet by mainViewModel.showManageAccountBottomSheet.collectAsStateWithLifecycle()
    var collapsingLayoutProgress by remember { mutableFloatStateOf(0f) }
    val isNetworkConnected by mainViewModel.isConnected.collectAsStateWithLifecycle()

    val showTitle by remember {
        derivedStateOf {
            collapsingLayoutProgress > 0.3f
        }
    }

    val onRefresh: () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
        userProfileScreenViewModel.refreshUserProfile()
        userPhotosPagingItems.refresh()
        likedPhotosPagingItems.refresh()
        userCollectionsPagingItems.refresh()
    }

    val currentBackStackEntry = navController.currentBackStackEntry

    LaunchedEffect(key1 = username) {
        userProfileScreenViewModel.setUsername(username)
    }
    LaunchedEffect(currentBackStackEntry) {
        val shouldRefreshCollections = currentBackStackEntry?.savedStateHandle?.get<Boolean>("shouldRefreshCollections")
        if (shouldRefreshCollections == true) {
            userCollectionsPagingItems.refresh()
            currentBackStackEntry.savedStateHandle.remove<Boolean>("shouldRefreshCollections")
        }
    }

    if (showManageAccountBottomSheet) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        profileViewModel.setManageAccountState(ManageAccountState.LOGGING_OUT)
        profileViewModel.logout()
        ProfileComponents().ManageAccountBottomSheet(
            isVisible = true,
            onDismiss = {
                mainViewModel.setShowManageAccountBottomSheet(false)
            },
            onLogout = {
                profileViewModel.logout()
            },
            onEditProfile = {
                mainViewModel.setShowManageAccountBottomSheet(false)
                onEditProfileClicked()
            },
            profilePictureUrl = null,
            firstName = null,
            lastName = null,
            username = username,
            manageAccountState = manageAccountState,
        )
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            UserProfileTopAppBar(
                userProfileUiState = userProfileUiState,
                showTitle = showTitle,
                onBackClicked = onBackClicked,
                scrollBehavior = scrollBehavior,
                isUserLoggedIn = isOwnProfile,
                loggedUsername = currentUsername,
                editProfile = {
                    onEditProfileClicked()
                },
                logout = {
                    mainViewModel.setShowManageAccountBottomSheet(true)
                },
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            ConnectionStatus(isConnected = isNetworkConnected, stringResource(R.string.offline_status_prompt))
            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = {
                    onRefresh()
                },
                state = pullRefreshState,
                indicator = {
                    LoadingIndicator(
                        state = pullRefreshState,
                        isRefreshing = showRefreshIndicator,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                },
            ) {
                when (val userProfileState = userProfileUiState) {
                    is UserProfileState.Loading -> {
                        UserProfileScreenShimmer(
                            appConfig = appConfig,
                            photoLayout = photoLayoutType.value,
                        )
                    }

                    is UserProfileState.Error -> {
                        UserProfileErrorContent(
                            state = userProfileState,
                            onTryAgain = {
                                onRefresh()
                            },
                        )
                    }

                    is UserProfileState.Success -> {
                        CollapsingLayout(
                            modifier = Modifier.fillMaxSize(),
                            header = {
                                UserProfileInfoContent(
                                    user = userProfileState.user,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            },
                            body = {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    PrimaryTabRow(
                                        selectedTabIndex = pagerState.currentPage,
                                        indicator = {
                                            TabRowDefaults.PrimaryIndicator(
                                                modifier = Modifier.tabIndicatorOffset(pagerState.currentPage, matchContentSize = true),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                width = Dp.Unspecified,
                                            )
                                        },
                                    ) {
                                        Destination.entries.forEachIndexed { index, tabDestination ->
                                            Tab(
                                                selected = pagerState.currentPage == index,
                                                onClick = {
                                                    scope.launch {
                                                        pagerState.animateScrollToPage(index)
                                                    }
                                                },
                                                text = {
                                                    Text(
                                                        text = tabDestination.label,
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Ellipsis,
                                                    )
                                                },
                                                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                                                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                    }

                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        key = { destinationIndex -> Destination.entries[destinationIndex].route },
                                    ) { destinationIndex ->
                                        when (Destination.entries[destinationIndex]) {
                                            Destination.PHOTOS -> UserPhotosScreen(
                                                modifier = Modifier.fillMaxSize(),
                                                photos = userPhotosPagingItems,
                                                photoLayout = photoLayoutType.value,
                                                onPhotoClicked = { selectedPhotoId ->
                                                    onPhotoSelected(selectedPhotoId)
                                                },
                                                appConfig = appConfig,
                                                padding = photosContentPadding,
                                                layoutConfig = layoutConfig,
                                                lazyStaggeredGridState = userPhotosGridState,
                                                getErrorMessage = {
                                                    userProfileScreenViewModel.errorHandler.getErrorMessage(it)
                                                },
                                                onRetry = userPhotosPagingItems::retry,
                                            )

                                            Destination.LIKES -> UserLikesScreen(
                                                modifier = Modifier.fillMaxSize(),
                                                userLikedPhotos = likedPhotosPagingItems,
                                                photoLayoutType = photoLayoutType.value,
                                                layoutConfig = layoutConfig,
                                                appConfig = appConfig,
                                                padding = photosContentPadding,
                                                onPhotoClicked = { selectedPhotoId ->
                                                    onPhotoSelected(selectedPhotoId)
                                                },
                                                gridState = likedPhotosGridState,
                                                getErrorMessage = {
                                                    userProfileScreenViewModel.errorHandler.getErrorMessage(it)
                                                },
                                                onRetry = likedPhotosPagingItems::retry,
                                            )

                                            Destination.COLLECTIONS -> UserCollectionsScreen(
                                                modifier = Modifier.fillMaxSize(),
                                                onCollectionSelected = { selectedPhotoId ->
                                                    onCollectionSelected(selectedPhotoId)
                                                },
                                                collections = userCollectionsPagingItems,
                                                lazyStaggeredGridState = userCollectionsGridState,
                                                getErrorMessage = {
                                                    userProfileScreenViewModel.errorHandler.getErrorMessage(it)
                                                },
                                                onRetry = userCollectionsPagingItems::retry,
                                            )
                                        }
                                    }
                                }
                            },
                            onProgress = { collapsingLayoutProgress = it },
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable that displays an error message when the user profile fails to load.
 * It provides a "Try Again" button to allow the user to re-attempt the data fetch.
 *
 * @param state The error state, containing the message to be displayed.
 * @param onTryAgain A lambda function to be invoked when the "Try Again" button is clicked.
 */
@Composable
fun UserProfileErrorContent(
    state: UserProfileState.Error,
    onTryAgain: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState(),
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ErrorView(
            errorTitle = null,
            errorMessage = state.message,
            onRetry = {
                onTryAgain()
            },
        )
    }
}

/**
 * A composable function for the top app bar of the user profile screen.
 *
 * This top app bar includes a back button, the user's username (which animates in as the user scrolls),
 * and action buttons for sharing the profile and opening it in a browser.
 * If the profile belongs to the currently logged-in user, it also displays a "more" menu
 * with options to edit the profile and log out.
 *
 * @param modifier The modifier to be applied to the TopAppBar.
 * @param userProfileUiState The current state of the user profile data, used to display user information.
 * @param showTitle A boolean that controls the visibility of the username in the title, typically based on scroll position.
 * @param scrollBehavior The scroll behavior for the TopAppBar, which handles its appearance during scrolling.
 * @param isUserLoggedIn A boolean indicating if the current user is logged in.
 * @param loggedUsername The username of the currently logged-in user, used to determine if the viewed profile is their own.
 * @param onBackClicked A lambda function to be invoked when the back navigation icon is clicked.
 * @param editProfile A lambda function to be invoked when the "Edit Profile" menu item is clicked.
 * @param logout A lambda function to be invoked when the "Logout" menu item is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileTopAppBar(
    modifier: Modifier = Modifier,
    userProfileUiState: UserProfileState,
    showTitle: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    isUserLoggedIn: Boolean,
    loggedUsername: String,
    onBackClicked: () -> Unit,
    editProfile: () -> Unit,
    logout: () -> Unit,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    var isMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            if (userProfileUiState is UserProfileState.Success && userProfileUiState.user.username != null) {
                AnimatedVisibility(
                    visible = showTitle,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                ) {
                    Text(
                        text = "@${userProfileUiState.user.username}",
                        fontSize = 20.sp,
                    )
                }
            }
        },
        modifier = modifier.wrapContentHeight(),
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button_cd),
                )
            }
        },
        actions = {
            if (userProfileUiState is UserProfileState.Success) {
                val user = userProfileUiState.user
                IconButton(
                    onClick = {
                        uriHandler.openUri(uri = user.profile_share_url)
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
                            linkToShare = user.profile_share_url,
                            context = context,
                        )
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = stringResource(R.string.share_profile_button_cd),
                    )
                }
                if (isUserLoggedIn && loggedUsername == user.username) {
                    IconButton(
                        onClick = {
                            isMenuVisible = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(R.string.more_options_button_cd),
                        )
                    }
                    CascadeDropdownMenu(
                        expanded = isMenuVisible,
                        onDismissRequest = { isMenuVisible = false },
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(R.string.edit_profile), style = MaterialTheme.typography.bodyMedium)
                            },
                            onClick = {
                                editProfile()
                                isMenuVisible = false
                            },
                        )

                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(R.string.logout), style = MaterialTheme.typography.bodyMedium)
                            },
                            onClick = {
                                logout()
                                isMenuVisible = false
                            },
                        )
                    }
                }
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
    )
}

/**
 * A composable that displays the main information content of a user's profile.
 *
 * This includes the user's circular profile picture, full name, username, an expandable biography,
 * and location if available. The biography is initially collapsed to 3 lines and can be expanded
 * by clicking on it.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param user The [User] object containing the profile data to display.
 */
@Composable
fun UserProfileInfoContent(
    modifier: Modifier = Modifier,
    user: User,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "${user.name}",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "@${user.username}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                if (user.location != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = user.location,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = user.profileImage?.large,
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant),
                contentScale = ContentScale.Crop,
            )
        }
        var isExpanded by remember { mutableStateOf(false) }
        if (user.bio != null) {
            Text(
                text = user.bio,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .fillMaxWidth(),
            )
        }
    }
}

/**
 * Represents the different destinations (tabs) available on the user profile screen.
 * Each destination corresponds to a specific type of content the user can view,
 * such as their own photos, liked photos, or collections.
 *
 * @param route A unique string identifier used for navigation routing, particularly within the [HorizontalPager].
 * @param label The human-readable text displayed on the tab in the UI.
 * @param contentDescription A description for accessibility services to announce the purpose of the tab.
 */
enum class Destination(
    val route: String,
    val label: String,
    val contentDescription: String,
) {
    PHOTOS("photos_tab", "Photos", "Photos"),
    LIKES("likes_tab", "Likes", "Likes"),
    COLLECTIONS(
        "collections_tab",
        "Collections",
        "Collections",
    ),
}

/**
 * A composable that displays a shimmer loading placeholder for the user profile screen.
 * This is shown while the user's profile data is being fetched.
 *
 * It mimics the layout of the actual profile screen, including placeholders for the
 * user info section, the tab row, and a grid of photos.
 *
 * @param photoLayout The layout type for the photo items (e.g., staggered grid, grid), which affects the shimmer item's appearance.
 * @param appConfig The application configuration, which provides settings for grid spacing and padding.
 * @param modifier The modifier to be applied to the layout.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserProfileScreenShimmer(
    photoLayout: PhotoLayoutType,
    appConfig: AppConfig,
    modifier: Modifier = Modifier,
) {
    val gridSpacingConfig = appConfig.gridSpacing
    val horizontalPhotoContentPadding = appConfig.layoutConfig.photoContentPadding * 2
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = Constants.LayoutValues.MIN_ADAPTIVE_SIZE),
        verticalItemSpacing = appConfig.gridSpacing.verticalSpacing,
        horizontalArrangement = Arrangement.spacedBy(gridSpacingConfig.horizontalSpacing - horizontalPhotoContentPadding),
        userScrollEnabled = false,
        modifier = modifier.fillMaxSize(),
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            UserProfileInfoShimmer()
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            TabRowShimmer(numberOfTabs = 3)
        }
        items(6) {
            PhotoLayoutItemShimmer(
                photoLayoutType = photoLayout,
                appConfig = appConfig,
            )
        }
    }
}

/**
 * A composable that displays a shimmer loading placeholder for the user profile information section.
 *
 * This is shown while the user's profile data is being fetched from the network. It mimics the
 * layout of the [UserProfileInfoContent] composable, providing visual placeholders for the
 * profile picture, name, username, bio, and location, all with a shimmering animation.
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun UserProfileInfoShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 12.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 12.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .height(25.dp)
                            .fillMaxWidth(0.6f)
                            .shimmer()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                    )

                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth(0.4f)
                            .shimmer()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .shimmer()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                    )
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth(0.3f)
                            .shimmer()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .shimmer()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(2) { index ->

                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(if (index == 1) 0.7f else 1f)
                        .shimmer()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                )
            }
        }
    }
}

/**
 * A composable that displays a shimmer loading placeholder for a tab row.
 *
 * This is used as part of the loading state for the user profile screen to mimic
 * the appearance of the `PrimaryTabRow` before its content is loaded. It displays a series
 * of shimmering rectangles representing the tabs, along with a faint line underneath
 * to simulate the tab row's divider.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param numberOfTabs The number of tab placeholders to display. Defaults to 3.
 */
@Composable
fun TabRowShimmer(
    modifier: Modifier = Modifier,
    numberOfTabs: Int = 3,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            repeat(numberOfTabs) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .padding(horizontal = 28.dp)
                        .clip(MaterialTheme.shapes.small)
                        .shimmer()
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
        )
    }
}

@Preview
@Composable
fun UserProfileInfoShimmerPreview() {
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
    PlashrTheme {
        Scaffold { contentPadding ->
            UserProfileScreenShimmer(
                appConfig = appConfig,
                photoLayout = PhotoLayoutType.STAGGERED_GRID,
                modifier = Modifier.padding(contentPadding),
            )
        }
    }
}
