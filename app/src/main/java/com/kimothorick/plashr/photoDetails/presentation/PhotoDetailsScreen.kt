package com.kimothorick.plashr.photoDetails.presentation

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.core.layout.WindowSizeClass
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.kimothorick.data.PreviewPhoto
import com.kimothorick.data.PreviewUserCollection
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.collections.presentation.components.AddPhotoToCollectionCard
import com.kimothorick.plashr.collections.presentation.components.AddPhotoToCollectionCardShimmer
import com.kimothorick.plashr.collections.presentation.components.CreateCollectionBottomSheet
import com.kimothorick.plashr.collections.presentation.components.CreateCollectionState
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.CurrentUserCollection
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.photo.sharedLink
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.navgraphs.PhotoFullPreview
import com.kimothorick.plashr.photoDetails.domain.PhotoUiState
import com.kimothorick.plashr.profile.presentation.ProfileViewModel
import com.kimothorick.plashr.profile.presentation.components.LoginState
import com.kimothorick.plashr.profile.presentation.components.LoginState.IDLE
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import com.kimothorick.plashr.ui.common.ShimmeringMorePhotoInfoContainer
import com.kimothorick.plashr.ui.common.ShimmeringPhotoContainer
import com.kimothorick.plashr.ui.components.ErrorView
import com.kimothorick.plashr.ui.components.InlineErrorView
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.utils.WindowWidthClass
import com.kimothorick.plashr.utils.formatLocation
import com.kimothorick.plashr.utils.shareLinkIntent
import com.kimothorick.plashr.utils.toFormattedDate
import com.kimothorick.plashr.utils.tooling.ComponentPreviews
import com.kimothorick.plashr.utils.windowWidthSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PhotoDetailsScreen(
    photoId: String,
    photoDetailsViewModel: PhotoDetailsViewModel,
    photoActionsViewModel: PhotoActionsViewModel,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onBackClicked: () -> Unit,
    navController: NavController,
    onUserClicked: (String) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val photoUiState by photoDetailsViewModel.photoUiState.collectAsStateWithLifecycle()
    val downloadStatus by photoActionsViewModel.downloadStatus.collectAsStateWithLifecycle()
    val lastState = remember { mutableStateOf<String?>(null) }
    val currentState = downloadStatus.javaClass.simpleName
    val likePhotoStatus by photoActionsViewModel.likeStatus.collectAsStateWithLifecycle()
    val photoCollectionState by photoActionsViewModel.photoCollectionState.collectAsStateWithLifecycle()
    val loginStatus by profileViewModel.isAppAuthorized.collectAsStateWithLifecycle()
    val isLiked by photoDetailsViewModel.isLikedByCurrentUser.collectAsStateWithLifecycle()
    val currentUserCollections by photoDetailsViewModel.currentUserCollections.collectAsState()
    val isPullToRefreshActive by photoDetailsViewModel.isRefreshingAction.collectAsStateWithLifecycle()
    val showPullIndicator = isPullToRefreshActive && photoUiState is PhotoUiState.Success

    val showLoginBottomSheet by mainViewModel.showLoginBottomSheet.collectAsStateWithLifecycle()
    val loginState by profileViewModel.loginState.collectAsStateWithLifecycle()
    val showCollectPhotoBottomSheet by photoActionsViewModel.showCollectPhotoBottomSheet.collectAsStateWithLifecycle()
    val loggedUsername by profileViewModel.username.collectAsStateWithLifecycle(initialValue = "")
    var showProfileDialog by remember { mutableStateOf(false) }
    var selectedProfileImageUrl by remember { mutableStateOf<String?>(null) }

    // Handle different download states
    when (val status = downloadStatus) {
        is DownloadStatus.InProgress -> {
            if (lastState.value != "InProgress") {
                Toast.makeText(context, stringResource(R.string.download_started), Toast.LENGTH_SHORT).show()
            }
        }

        is DownloadStatus.Complete -> {
            if (status.message?.contains(stringResource(R.string.file_already_exists)) == true) {
                Toast.makeText(context, stringResource(R.string.file_already_exists), Toast.LENGTH_SHORT).show()
            }
            photoActionsViewModel.resetDownloadState()
        }

        else -> {
            Log.i("PhotoDetailsScreen", status.toString())
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    lastState.value = currentState

    LaunchedEffect(key1 = photoId) {
        photoDetailsViewModel.setPhotoId(photoId = photoId)
    }

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { activityResult ->
        profileViewModel.handleUnsplashAuthResult(activityResult)
    }
    val hapticFeedback = LocalHapticFeedback.current
    val pullToRefreshState = rememberPullToRefreshState()

    val unsplashAuthIntent by profileViewModel.unsplashAuthIntent.collectAsStateWithLifecycle()

    LaunchedEffect(unsplashAuthIntent) {
        unsplashAuthIntent?.let { intent ->
            authLauncher.launch(intent)
            profileViewModel.consumeUnsplashAuthIntent()
        }
    }

    LaunchedEffect(key1 = likePhotoStatus) {
        if (likePhotoStatus is LikeStatus.Success) {
            val isDislike = (likePhotoStatus as LikeStatus.Success).isDislike
            val intendedNewIsLikedValue = !isDislike
            photoDetailsViewModel.setIsLikedByCurrentUser(intendedNewIsLikedValue)
        }
    }

    LaunchedEffect(key1 = photoCollectionState) {
        when (photoCollectionState) {
            is PhotoCollectionState.Success -> {
                val collectionType = (photoCollectionState as PhotoCollectionState.Success).collectPhotoType

                when (collectionType) {
                    CollectPhotoType.Add -> {
                        val collectionResponse = (photoCollectionState as PhotoCollectionState.Success).photoCollectionResult
                        photoDetailsViewModel.setCurrentUserCollections(collectionResponse?.photo?.currentUserCollections)
                    }

                    CollectPhotoType.Remove -> {
                        val collectionResponse = (photoCollectionState as PhotoCollectionState.Success).photoCollectionResult
                        photoDetailsViewModel.setCurrentUserCollections(collectionResponse?.photo?.currentUserCollections)
                    }
                }
            }

            is PhotoCollectionState.Error -> {
                val collectionType = (photoCollectionState as PhotoCollectionState.Error).collectPhotoType
                val messageString = if (collectionType == CollectPhotoType.Add) {
                    R.string.failed_to_add_photo_to_collection
                } else {
                    R.string.failed_to_remove_photo_from_collection
                }

                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(messageString),
                        withDismissAction = true,
                        duration = SnackbarDuration.Short,
                    )
                }
            }

            else -> {}
        }
    }

    if (showLoginBottomSheet) {
        ProfileComponents().LoginBottomSheet(
            isVisible = true,
            onDismiss = {
                if (profileViewModel.loginState.value != LoginState.SUCCESS) {
                    profileViewModel.setLoginState(IDLE)
                }
                mainViewModel.setShowLoginBottomSheet(false)
            },
            onLoginWithUnsplash = {
                profileViewModel.initiateUnsplashLogin()
            },
            loginState = loginState,
        )
    }

    if (showCollectPhotoBottomSheet) {
        when (photoUiState) {
            is PhotoUiState.Success -> {
                val collections = photoActionsViewModel.userCollectionsFlow.collectAsLazyPagingItems()

                CollectPhotoBottomSheet(
                    showBottomSheet = true,
                    onDismiss = {
                        photoActionsViewModel.setShowCollectPhotoBottomSheet(false)
                    },
                    collections = collections,
                    currentUserCollections = currentUserCollections,
                    photoCollectionState = photoCollectionState,
                    collectionClicked = { collectionID ->
                        if (loginStatus) {
                            if (photoCollectionState !is PhotoCollectionState.InProgress) {
                                val isAlreadyCollectedInThisCollection = currentUserCollections?.any { it?.id == collectionID }

                                if (isAlreadyCollectedInThisCollection == true) {
                                    photoActionsViewModel.removePhotoFromCollection(photoId, collectionID)
                                } else {
                                    photoActionsViewModel.addPhotoToCollection(photoId, collectionID)
                                }
                            }
                        } else {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.you_re_not_logged_in),
                                    actionLabel = context.getString(R.string.login),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short,
                                )

                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        mainViewModel.checkAndShowLoginBottomSheet(
                                            false,
                                        )
                                    }

                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        }
                    },
                    onCreateNewCollectionClicked = {
                        photoActionsViewModel.onShowCreateCollectionSheet()
                    },
                )
            }

            else -> {}
        }
    }

    val showCreateCollectionSheet by photoActionsViewModel.showCreateCollectionBottomSheet.collectAsStateWithLifecycle()
    val createCollectionState by photoActionsViewModel.createCollectionState.collectAsStateWithLifecycle()

    LaunchedEffect(createCollectionState) {
        when (createCollectionState) {
            is CreateCollectionState.Success -> {
                delay(1000)
                photoActionsViewModel.finishCreateCollectionFlow()
            }

            is CreateCollectionState.Failed -> {
                delay(2000)
                photoActionsViewModel.finishCreateCollectionFlow()
            }

            else -> {
            }
        }
    }
    if (showCreateCollectionSheet) {
        val createSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
        CreateCollectionBottomSheet(
            sheetState = createSheetState,
            createState = createCollectionState,
            onDismiss = { photoActionsViewModel.onDismissCreateCollectionSheet() },
            onCreateCollection = { name, description, isPrivate ->
                photoActionsViewModel.createCollection(name, description, isPrivate, photoId)
            },
            onReset = {
            },
        )
    }

    if (showProfileDialog) {
        selectedProfileImageUrl?.let { originalUrl ->
            val baseUrl = originalUrl.substringBefore("?")
            ProfilePreviewPopup(
                profileImageUrl = baseUrl,
                onDismiss = { showProfileDialog = false },
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            PhotoDetailsAppbar(
                onBackClicked = onBackClicked,
                photoUiState = photoUiState,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = showPullIndicator,
            onRefresh = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                photoDetailsViewModel.refreshPhotoDetails()
            },
            state = pullToRefreshState,
            indicator = {
                LoadingIndicator(
                    state = pullToRefreshState,
                    isRefreshing = showPullIndicator,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            },
            modifier = Modifier.padding(innerPadding),
        ) {
            when (photoUiState) {
                is PhotoUiState.Success -> {
                    (photoUiState as PhotoUiState.Success).photo.let { photo ->
                        photoActionsViewModel.setUsername(username = loggedUsername)

                        val notLoggedInMessage = stringResource(R.string.you_re_not_logged_in)
                        val loginActionLabel = stringResource(R.string.login)

                        when (windowSizeClass.windowWidthSize()) {
                            WindowWidthClass.COMPACT, WindowWidthClass.MEDIUM -> {
                                Column(
                                    modifier = Modifier
                                        .verticalScroll(
                                            rememberScrollState(),
                                        ),
                                ) {
                                    PhotoContainer(
                                        photo = photo,
                                        modifier = Modifier.fillMaxWidth(),
                                        onPhotoFullPreviewClicked = {
                                            navController.navigate(
                                                route = PhotoFullPreview(
                                                    photoUrl = photo.urls!!.regular!!,
                                                    photo = photo,
                                                ),
                                            )
                                        },
                                    )

                                    MorePhotoInfoContainer(
                                        photo = photo,
                                        likePhotoClicked = {
                                            if (loginStatus) {
                                                if (isLiked != true) {
                                                    photoActionsViewModel.likePhoto(photo.id)
                                                } else {
                                                    photoActionsViewModel.dislikePhoto(photo.id)
                                                }
                                            } else {
                                                scope.launch {
                                                    loginSnackbar(
                                                        snackbarHostState,
                                                        mainViewModel,
                                                        message = notLoggedInMessage,
                                                        actionLabel = loginActionLabel,
                                                    )
                                                }
                                            }
                                        },
                                        collectPhotoClicked = {
                                            if (loginStatus) {
                                                photoActionsViewModel.setShowCollectPhotoBottomSheet(true)
                                            } else {
                                                scope.launch {
                                                    loginSnackbar(
                                                        snackbarHostState,
                                                        mainViewModel,
                                                        message = notLoggedInMessage,
                                                        actionLabel = loginActionLabel,
                                                    )
                                                }
                                            }
                                        },
                                        likeStatus = likePhotoStatus,
                                        isPhotoLiked = isLiked,
                                        isPhotoCollected = when (currentUserCollections?.size) {
                                            0 -> false
                                            else -> true
                                        },
                                        downloadPhotoClicked = {
                                            downloadPhoto(
                                                context = context,
                                                photo = photo,
                                                photoActionsViewModel = photoActionsViewModel,
                                            )
                                        },
                                        onUserClicked = { onUserClicked(photo.user!!.username!!) },
                                    )
                                }
                            }

                            WindowWidthClass.EXPANDED -> {
                                Row {
                                    PhotoContainer(
                                        photo = photo,
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .padding(start = 16.dp),
                                        onPhotoFullPreviewClicked = {
                                            navController.navigate(
                                                route = PhotoFullPreview(
                                                    photoUrl = photo.urls!!.regular!!,
                                                    photo = photo,
                                                ),
                                            )
                                        },
                                    )
                                    MorePhotoInfoContainer(
                                        photo = photo,
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .verticalScroll(
                                                rememberScrollState(),
                                            )
                                            .padding(start = 16.dp),
                                        likePhotoClicked = {
                                            if (loginStatus) {
                                                if (isLiked != true) {
                                                    photoActionsViewModel.likePhoto(photo.id)
                                                } else {
                                                    photoActionsViewModel.dislikePhoto(photo.id)
                                                }
                                            } else {
                                                scope.launch {
                                                    loginSnackbar(
                                                        snackbarHostState,
                                                        mainViewModel,
                                                        message = notLoggedInMessage,
                                                        actionLabel = loginActionLabel,
                                                    )
                                                }
                                            }
                                        },
                                        likeStatus = likePhotoStatus,
                                        isPhotoLiked = isLiked,
                                        isPhotoCollected = when (currentUserCollections?.size) {
                                            0 -> false
                                            else -> true
                                        },
                                        collectPhotoClicked = {
                                            if (loginStatus) {
                                                photoActionsViewModel.setShowCollectPhotoBottomSheet(true)
                                            } else {
                                                scope.launch {
                                                    loginSnackbar(
                                                        snackbarHostState,
                                                        mainViewModel,
                                                        message = notLoggedInMessage,
                                                        actionLabel = loginActionLabel,
                                                    )
                                                }
                                            }
                                        },
                                        downloadPhotoClicked = {
                                            downloadPhoto(
                                                context = context,
                                                photo = photo,
                                                photoActionsViewModel = photoActionsViewModel,
                                            )
                                        },
                                        onUserClicked = { onUserClicked(photo.user!!.username!!) },
                                    )
                                }
                            }
                        }
                    }
                }

                is PhotoUiState.Error -> {
                    val error = photoUiState as PhotoUiState.Error
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ErrorView(
                            errorTitle = null,
                            errorMessage = error.errorMessage,
                            onRetry = { photoDetailsViewModel.fetchPhoto(photoId = photoId) },
                        )
                    }
                }

                is PhotoUiState.Loading -> {
                    LoadingShimmer(
                        windowSizeClass = windowSizeClass,
                    )
                }
            }
        }
    }
}

/**
 * A custom [TopAppBar] for the photo details screen.
 *
 * It includes a back navigation icon and action icons for opening the photo on the web
 * and sharing the photo link. These actions are only visible when the [photoUiState]
 * is [PhotoUiState.Success].
 *
 * @param onBackClicked A lambda function invoked when the back navigation icon is clicked.
 * @param photoUiState The current state of the photo details UI, used to determine
 * whether to show action icons.
 * @param scrollBehavior A [TopAppBarScrollBehavior] that defines how the app bar
 * interacts with scrolling content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailsAppbar(
    onBackClicked: () -> Unit,
    photoUiState: PhotoUiState,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    TopAppBar(
        title = { null },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClicked()
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
        actions = {
            if (photoUiState is PhotoUiState.Success) {
                IconButton(
                    onClick = {
                        photoUiState.photo.sharedLink?.let { shareLink ->
                            uriHandler.openUri(uri = shareLink)
                        }
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
                            linkToShare = photoUiState.let { photoUiState ->
                                photoUiState.photo.links!!.html!!
                            },
                            context = context,
                        )
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = stringResource(R.string.share_photo),
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier.wrapContentHeight(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

/**
 * Initiates the download of a photo.
 *
 * This function checks for a valid download URL from the provided [photo] object.
 * If a URL is found, it constructs a file name based on the photographer's name and photo ID,
 * then calls the [PhotoActionsViewModel] to start the download process.
 * If no download URL is available, it displays a toast message to the user.
 *
 * @param context The [Context] required to show toast messages.
 * @param photo The [Photo] object containing the details and URLs for the image to be downloaded.
 * @param photoActionsViewModel The [PhotoActionsViewModel] instance responsible for handling the download logic.
 */
fun downloadPhoto(
    context: Context,
    photo: Photo,
    photoActionsViewModel: PhotoActionsViewModel,
) {
    photo.urls?.regular.let { downloadUrl ->
        if (downloadUrl != null) {
            val fileName = "${photo.user?.firstName?.lowercase()}-${photo.user?.lastName?.lowercase()}-${photo.id}.jpg"
            photoActionsViewModel.startDownloadPhoto(
                downloadUrl,
                fileName = fileName,
                photoId = photo.id,
            )
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.download_url_not_found),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}

/**
 * Shows a snackbar to prompt the user to log in. If the user clicks the "Login" action
 * on the snackbar, it triggers the login bottom sheet.
 *
 * @param snackbarHostState The [SnackbarHostState] to manage the snackbar.
 * @param mainViewModel The [MainViewModel] to control UI state, like showing the login bottom sheet.
 * @param message The text to display in the snackbar.
 * @param actionLabel The label for the action button on the snackbar (e.g., "Login").
 */
suspend fun loginSnackbar(
    snackbarHostState: SnackbarHostState,
    mainViewModel: MainViewModel,
    message: String,
    actionLabel: String,
) {
    val result = snackbarHostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        withDismissAction = true,
        duration = SnackbarDuration.Short,
    )

    if (result == SnackbarResult.ActionPerformed) {
        mainViewModel.checkAndShowLoginBottomSheet(false)
    }
}

/**
 * A composable that displays a photo, handling loading, success, and error states.
 *
 * It shows a circular progress indicator while the image is loading. On success, it displays
 * the image, which can be clicked for a full preview. If the image fails to load, it shows an
 * error message with a retry option. The aspect ratio of the container is determined by the
 * photo's dimensions, with a placeholder color derived from the photo's metadata.
 *
 * @param photo The [Photo] object containing the details of the image to be displayed, including URLs and dimensions.
 * @param modifier The [Modifier] to be applied to the container.
 * @param onPhotoFullPreviewClicked A lambda function to be invoked when the photo is clicked, typically for showing a full-screen preview.
 */
@Composable
fun PhotoContainer(
    photo: Photo,
    modifier: Modifier = Modifier,
    onPhotoFullPreviewClicked: () -> Unit = {},
) {
    val actualColor = photo.color?.let { Color(it.toColorInt()) }
    val aspectRatio = if (photo.width != null && photo.height != null) {
        photo.width.toFloat() / photo.height.toFloat()
    } else {
        1f
    }

    val painter = rememberAsyncImagePainter(photo.urls!!.regular)
    val state by painter.state.collectAsState()

    when (state) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading,
        -> {
            Box(
                modifier = modifier
                    .aspectRatio(aspectRatio)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        is AsyncImagePainter.State.Success -> {
            AsyncImage(
                model = photo.urls.regular,
                contentDescription = null,
                modifier = modifier.aspectRatio(aspectRatio).fillMaxWidth()
                    .clickable {
                        onPhotoFullPreviewClicked()
                    },
                placeholder = remember(actualColor) {
                    ColorPainter(actualColor!!)
                },
            )
        }

        is AsyncImagePainter.State.Error -> {
            Box(
                modifier = modifier
                    .aspectRatio(aspectRatio)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .fillMaxWidth()
                    .clickable {
                        painter.restart()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.image_load_error),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

/**
 * A composable that displays detailed information about a [Photo].
 * This includes the photographer's information, photo metadata (EXIF data), location,
 * publication date, description, and tags. It also provides action buttons for liking,
 * collecting, and downloading the photo.
 *
 * @param photo The [Photo] object containing the details to be displayed.
 * @param modifier The [Modifier] to be applied to the container.
 * @param likePhotoClicked A lambda function to be invoked when the like/favorite button is clicked.
 * @param likeStatus The current status of the like action (e.g., loading, success, error).
 * @param isPhotoLiked A boolean indicating whether the current user has liked the photo.
 * @param isPhotoCollected A boolean indicating whether the current user has collected the photo in any of their collections.
 * @param collectPhotoClicked A lambda function to be invoked when the collect/bookmark button is clicked.
 * @param downloadPhotoClicked A lambda function to be invoked when the download button is clicked.
 * @param onUserClicked A lambda function to be invoked when the user's profile picture or name is clicked, passing the username.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MorePhotoInfoContainer(
    photo: Photo,
    modifier: Modifier = Modifier,
    likePhotoClicked: () -> Unit = {},
    likeStatus: LikeStatus,
    isPhotoLiked: Boolean?,
    isPhotoCollected: Boolean?,
    collectPhotoClicked: () -> Unit = {},
    downloadPhotoClicked: () -> Unit = {},
    onUserClicked: (String?) -> Unit = {},
) {
    val hapticFeedback = LocalHapticFeedback.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.more_info),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            IconButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                    likePhotoClicked()
                },
                enabled = likeStatus !is LikeStatus.Loading,
            ) {
                Icon(
                    imageVector = if (isPhotoLiked == true) {
                        Icons.Rounded.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
                    },
                    contentDescription = if (isPhotoLiked ==
                        true
                    ) {
                        stringResource(R.string.unlike_photo)
                    } else {
                        stringResource(R.string.like_photo)
                    },
                    tint = if (isPhotoLiked == true) {
                        colorResource(id = R.color.like_color)
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                )
            }
            IconButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                    collectPhotoClicked()
                },
            ) {
                Icon(
                    imageVector = if (isPhotoCollected == true) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = if (isPhotoCollected ==
                        true
                    ) {
                        stringResource(R.string.remove_from_collection)
                    } else {
                        stringResource(R.string.add_to_collection)
                    },
                    tint = if (isPhotoCollected == true) {
                        colorResource(id = R.color.collect_green)
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                )
            }
            IconButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                    downloadPhotoClicked()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.download_24px),
                    contentDescription = stringResource(R.string.download_photo),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = CircleShape)
                    .combinedClickable(
                        onClick = { onUserClicked(photo.user?.username) },
                        onLongClick = {
                            /* hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                             onUserLongClick(photo.user?.profileImage?.large ?: photo.user?.profileImage?.medium)*/
                        },
                    ),
                model = photo.user!!.profileImage!!.medium,
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(R.string.photo_owner_profile_picture),
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable(enabled = false, onClick = {}),
            ) {
                photo.user.name?.let { userFullName ->
                    Text(
                        text = userFullName,
                        modifier = Modifier
                            .clickable {
                                onUserClicked(photo.user.username)
                            },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                photo.user.username?.let { username ->
                    Text(
                        text = "@$username",
                        modifier = Modifier.clickable {
                            onUserClicked(photo.user.username)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        if (photo.location?.city != null || photo.location?.country != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = stringResource(R.string.location_icon),
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = formatLocation(photo.location.city, photo.location.country),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Text(
            "Published on ${photo.createdAt?.toFormattedDate()}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        photo.description?.let { photoDescription ->
            Text(
                text = photoDescription.trim(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.camera),
                style = MaterialTheme.typography.titleMedium,
            )

            val exifItems = listOfNotNull(
                ExifData(R.string.make, photo.exif?.make),
                ExifData(R.string.model, photo.exif?.model),
                ExifData(R.string.focal_length, photo.exif?.focalLength),
                ExifData(R.string.aperture, photo.exif?.aperture),
                ExifData(R.string.shutter_speed, photo.exif?.exposureTime),
                ExifData(R.string.iso, photo.exif?.iso?.toString() ?: "__"),
                ExifData(R.string.dimensions, "${photo.width} x ${photo.height}"),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    exifItems.forEachIndexed { index, exifData ->
                        if (index % 2 == 0) {
                            ExifRow(
                                exifTitle = stringResource(id = exifData.titleResId),
                                exifDescription = exifData.description,
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    exifItems.forEachIndexed { index, exifData ->
                        if (index % 2 != 0) {
                            ExifRow(
                                exifTitle = stringResource(id = exifData.titleResId),
                                exifDescription = exifData.description,
                            )
                        }
                    }
                }
            }
        }

        if (photo.tags != null) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        PhotoTags(photo = photo)
    }
}

/**
 * A data class to hold a single piece of EXIF (Exchangeable Image File Format) data.
 * It pairs a title, represented by a string resource ID, with its corresponding value.
 *
 * @param titleResId The string resource ID for the EXIF data's title (e.g., R.string.make, R.string.model).
 * @param description The value of the EXIF data (e.g., "Canon", "EOS R5"). Can be null if the data is not available.
 */
data class ExifData(
    val titleResId: Int,
    val description: String?,
)

/**
 * A composable that displays a single piece of EXIF data in a title-description format.
 * It's arranged vertically with the title on top and the description below.
 *
 * @param exifTitle The title or label for the EXIF data (e.g., "Make", "Model").
 * @param exifDescription The value of the EXIF data. If it's null or empty, a placeholder "_" is displayed.
 */
@Composable
private fun ExifRow(
    exifTitle: String,
    exifDescription: String?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(end = 8.dp),
    ) {
        Text(
            exifTitle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            exifDescription?.trim() ?: "_",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

/**
 * A composable that displays a horizontal list of tags associated with a [Photo].
 *
 * This function renders a [LazyRow] of [SuggestionChip]s, each representing a tag.
 * The list is only displayed if the `photo.tags` list is not null. The chips are
 * currently not interactive (onClick is empty).
 *
 * @param photo The [Photo] object whose tags are to be displayed.
 */
@Composable
private fun PhotoTags(
    photo: Photo,
) {
    if (photo.tags != null) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                count = photo.tags.size,
            ) { index ->
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            text = photo.tags[index]?.title ?: "",
                        )
                    },
                    border = BorderStroke(
                        width = 0.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                )
            }
        }
    }
}

/**
 * Displays a shimmer loading placeholder for the photo details screen.
 *
 * This composable adapts its layout based on the available screen width,
 * showing a single-column layout for `COMPACT` and `MEDIUM` widths, and a
 * two-pane row layout for `EXPANDED` widths. It uses `ShimmeringPhotoContainer`
 * and `ShimmeringMorePhotoInfoContainer` to create the placeholder effect.
 *
 * @param windowSizeClass The [WindowSizeClass] used to determine the appropriate layout
 * for the current window size.
 */
@Composable
fun LoadingShimmer(
    windowSizeClass: WindowSizeClass,
) {
    when (windowSizeClass.windowWidthSize()) {
        WindowWidthClass.COMPACT, WindowWidthClass.MEDIUM -> {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
            ) {
                ShimmeringPhotoContainer()
                ShimmeringMorePhotoInfoContainer(modifier = Modifier.padding(16.dp))
            }
        }

        WindowWidthClass.EXPANDED -> {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.background),
            ) {
                ShimmeringPhotoContainer(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                )
                ShimmeringMorePhotoInfoContainer(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

/**
 * A modal bottom sheet that allows a user to add a photo to one of their collections.
 * It displays a list of the user's existing collections, indicating which ones already
 * contain the current photo. Users can tap a collection to add or remove the photo.
 * The sheet also provides options to create a new collection or dismiss the sheet.
 *
 * @param showBottomSheet A boolean to control the visibility of the bottom sheet.
 * @param onDismiss A lambda function to be invoked when the bottom sheet is dismissed.
 * @param modifier The [Modifier] to be applied to the bottom sheet.
 * @param photoCollectionState The current state of adding/removing the photo from a collection, used to show loading indicators on collection items.
 * @param collections A [LazyPagingItems] of [UserCollection] representing the user's collections, fetched with pagination.
 * @param currentUserCollections A list of collections that the current photo already belongs to. This is used to determine the initial state of each collection item.
 * @param collectionClicked A lambda function invoked when a user taps on a collection card. It provides the ID of the clicked collection.
 * @param onCreateNewCollectionClicked A lambda function invoked when the "Create new collection" button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectPhotoBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    photoCollectionState: PhotoCollectionState,
    collections: LazyPagingItems<UserCollection>,
    currentUserCollections: List<CurrentUserCollection?>?,
    collectionClicked: (String) -> Unit,
    onCreateNewCollectionClicked: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val collectionsRefreshState = collections.loadState.refresh

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    onDismiss()
                }
            },
            sheetState = sheetState,
            modifier = modifier
                .padding(bottom = 8.dp, end = 8.dp, start = 8.dp)
                .navigationBarsPadding()
                .statusBarsPadding(),
            shape = MaterialTheme.shapes.extraLarge,
            sheetMaxWidth = Constants.LayoutValues.BOTTOM_SHEET_MAX_WIDTH,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = contentColorFor(
                MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
            ) {
                // TITLE
                Text(
                    text = stringResource(R.string.add_to_collection),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )

                // COLLECTIONS
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    when (collectionsRefreshState) {
                        is LoadState.Loading -> {
                            items(3) {
                                AddPhotoToCollectionCardShimmer()
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                val errorMessage = collectionsRefreshState.error.message ?: ""
                                InlineErrorView(
                                    errorMessage = errorMessage,
                                    onRetry = {
                                        collections.retry()
                                    },
                                )
                            }
                        }

                        is LoadState.NotLoading -> {
                            items(
                                count = collections.itemCount,
                            ) { index ->

                                val collection = collections[index]
                                if (collection != null) {
                                    val isPhotoInThisSpecificCollection = remember(currentUserCollections, collection) {
                                        currentUserCollections?.any { it?.id == collection.id } ?: false
                                    }

                                    val totalPhotoCount = if (isPhotoInThisSpecificCollection) {
                                        currentUserCollections?.find { it?.id == collection.id }?.let { matchingCollection ->
                                            matchingCollection.totalPhotos ?: 0
                                        } ?: 0
                                    } else {
                                        collection.totalPhotos
                                    }

                                    val lastUpdated = if (isPhotoInThisSpecificCollection) {
                                        currentUserCollections?.find { it?.id == collection.id }?.let { matchingCollection ->
                                            matchingCollection.updatedAt ?: ""
                                        } ?: ""
                                    } else {
                                        collection.updatedAt
                                    }

                                    AddPhotoToCollectionCard(
                                        photoCollectionState = photoCollectionState,
                                        collection = collections[index]!!,
                                        photoCollected = isPhotoInThisSpecificCollection,
                                        onCollectionClicked = { collectionClicked(collection.id!!) },
                                        totalPhotoCount = totalPhotoCount,
                                        lastUpdated = lastUpdated,
                                    )
                                }
                            }
                        }
                    }
                }

                // ACTION BUTTONS
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 24.dp,
                        ),
                ) {
                    OutlinedButton(
                        onClick = {
                            onCreateNewCollectionClicked()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(text = stringResource(R.string.create_new_collection))
                    }
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(text = stringResource(R.string.done))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfilePreviewPopup(
    profileImageUrl: String,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        val animatedBlur by animateDpAsState(10.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
                    .blur(animatedBlur, BlurredEdgeTreatment.Unbounded),
            ) {}
            AsyncImage(
                model = profileImageUrl,
                contentDescription = stringResource(R.string.profile_picture),
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(Color.DarkGray),
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(64.dp)
                    .fillMaxWidth()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

//region PREVIEWS
@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreviews
@Composable
fun CollectPhotoBottomSheetPreview() {
    PlashrTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Add photo to collection",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(
                        count = 5,
                    ) { index ->
                        when (index) {
                            3 -> AddPhotoToCollectionCard(
                                collection = PreviewUserCollection,
                                photoCollectionState = PhotoCollectionState.Idle,
                                photoCollected = true,
                                onCollectionClicked = {},
                                totalPhotoCount = 10,
                                lastUpdated = "",
                            )

                            else -> AddPhotoToCollectionCard(
                                collection = PreviewUserCollection,
                                photoCollectionState = PhotoCollectionState.Idle,
                                photoCollected = false,
                                onCollectionClicked = {},
                                totalPhotoCount = 10,
                                lastUpdated = "",
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(text = "Create new collection")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(text = "Done")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreviews
@Composable
fun CollectPhotoBottomSheetContentPreview() {
    PlashrTheme {
        Surface {
            val collections = listOf(
                PreviewUserCollection, // Assumes PreviewUserCollection is a UserCollection instance
                PreviewUserCollection.copy(id = "2", title = "Another Collection"),
                PreviewUserCollection.copy(id = "3", title = "Summer Vibes"),
            )
            val fakePagingDataFlow = flowOf(PagingData.from(collections))
            val lazyPagingItems = fakePagingDataFlow.collectAsLazyPagingItems()
            val currentUserCollections = listOf(
                CurrentUserCollection(
                    id = "2",
                    title = "Another Collection",
                    description = null,
                    publishedAt = "",
                    lastCollectedAt = "",
                    updatedAt = "",
                    coverPhoto = null,
                    totalPhotos = 11,
                    user = null,
                ),
            )
            CollectPhotoBottomSheet(
                showBottomSheet = true,
                onDismiss = {},
                photoCollectionState = PhotoCollectionState.Idle,
                collections = lazyPagingItems,
                currentUserCollections = currentUserCollections,
                collectionClicked = {},
                onCreateNewCollectionClicked = {},
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PhotoMoreInfoPreview() {
    PlashrTheme {
        Surface {
            MorePhotoInfoContainer(
                photo = PreviewPhoto,
                likeStatus = LikeStatus.Idle,
                isPhotoLiked = PreviewPhoto.likedByUser,
                isPhotoCollected = PreviewPhoto.currentUserCollections?.any { userCollection ->
                    userCollection?.id == PreviewUserCollection.id
                },
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoadingImageErrorPreview() {
    val photo = PreviewPhoto
    val aspectRatio = if (photo.width != null && photo.height != null) {
        photo.width.toFloat() / photo.height.toFloat()
    } else {
        1f
    }

    PlashrTheme {
        Box(
            modifier = Modifier
                .aspectRatio(aspectRatio)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .fillMaxWidth()
                .clickable {},
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.image_load_error),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(showBackground = true, name = "Profile Popup Light")
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Profile Popup Dark")
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, device = TABLET, name = "Profile Popup Dark")
@Composable
fun ProfilePreviewPopupPreview() {
    PlashrTheme {
        ProfilePreviewPopup(
            profileImageUrl = "https://images.unsplash.com/profile-1600086438720-bce251341c90image", // Example base URL
            onDismiss = {},
        )
    }
}
//endregion
