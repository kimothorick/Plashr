package com.kimothorick.plashr.search.presentation.search

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.search.presentation.CollectionsScreen
import com.kimothorick.plashr.search.presentation.PhotosScreen
import com.kimothorick.plashr.search.presentation.UsersScreen
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import kotlinx.coroutines.launch

/**
 * A Composable function that displays the main search interface.
 *
 * This screen features a tabbed layout using a `HorizontalPager` to switch between
 * searching for photos, collections, and users. It collects data from various ViewModels
 * to display paginated search results and handles user interactions like selecting an item
 * or retrying a failed search.
 *
 * @param searchPagerState The state object to control and observe the `HorizontalPager`.
 * @param mainViewModel The [MainViewModel] instance for accessing global app configurations.
 * @param searchScreenViewModel The [SearchScreenViewModel] instance for fetching search results
 * and handling search-related state.
 * @param settingsViewModel The [SettingsViewModel] instance for accessing user settings,
 * like the photo layout preference.
 * @param onPhotoSelected A callback lambda that is invoked when a photo is selected,
 * providing the photo's ID.
 * @param onCollectionSelected A callback lambda that is invoked when a collection is selected,
 * providing the collection's ID.
 * @param onUserSelected A callback lambda that is invoked when a user is selected,
 * providing the user's username.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchPagerState: PagerState,
    mainViewModel: MainViewModel,
    searchScreenViewModel: SearchScreenViewModel,
    settingsViewModel: SettingsViewModel,
    onPhotoSelected: (String) -> Unit,
    onCollectionSelected: (String) -> Unit,
    onUserSelected: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val photoPagingItems = searchScreenViewModel.photos.collectAsLazyPagingItems()
    val collectionPagingItems = searchScreenViewModel.collections.collectAsLazyPagingItems()
    val userPagingItems = searchScreenViewModel.users.collectAsLazyPagingItems()
    val photoLayoutType = settingsViewModel.photoLayout.collectAsStateWithLifecycle()
    val appConfig by mainViewModel.appConfig.collectAsStateWithLifecycle()
    val layoutConfig = appConfig.layoutConfig
    val totalPhotoContentPadding = layoutConfig.photoContentPadding * 2
    val photoGridState = rememberLazyStaggeredGridState()
    val collectionGridState = rememberLazyGridState()
    val userGridState = rememberLazyGridState()

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = searchPagerState.currentPage,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(searchPagerState.currentPage, matchContentSize = true),
                    color = MaterialTheme.colorScheme.onSurface,
                    width = Dp.Unspecified,
                )
            },
        ) {
            SearchDestination.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = searchPagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            searchPagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(destination.label),
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
            state = searchPagerState,
            modifier = Modifier.weight(1f),
            key = { index -> SearchDestination.entries[index].route },
        ) { pageIndex ->
            when (SearchDestination.entries[pageIndex]) {
                SearchDestination.PHOTOS -> PhotosScreen(
                    photos = photoPagingItems,
                    layoutType = photoLayoutType.value,
                    layoutConfig = layoutConfig,
                    appConfig = appConfig,
                    padding = totalPhotoContentPadding,
                    onPhotoSelected = { photoId ->
                        onPhotoSelected(photoId)
                    },
                    lazyStaggeredGridState = photoGridState,
                    formatErrorMessage = {
                        searchScreenViewModel.errorHandler.getErrorMessage(it)
                    },
                    onRetry = {
                        photoPagingItems.retry()
                    },
                )

                SearchDestination.COLLECTIONS -> CollectionsScreen(
                    collections = collectionPagingItems,
                    gridState = collectionGridState,
                    formatErrorMessage = {
                        searchScreenViewModel.errorHandler.getErrorMessage(it)
                    },
                    onCollectionClicked = { collectionId ->
                        onCollectionSelected(collectionId)
                    },
                    onRetry = {
                        collectionPagingItems.retry()
                    },
                )

                SearchDestination.USERS -> UsersScreen(
                    users = userPagingItems,
                    usersGridState = userGridState,
                    formatErrorMessage = {
                        searchScreenViewModel.errorHandler.getErrorMessage(it)
                    },
                    onUserSelected = { username ->
                        onUserSelected(username)
                    },
                    onRetry = {
                        userPagingItems.retry()
                    },
                )
            }
        }
    }
}

/**
 * Represents the different destinations (tabs) available on the search screen.
 *
 * Each destination has properties for navigation and UI representation.
 *
 * @param route The navigation route string for the destination.
 * @param label The string resource ID for the tab's visible label.
 * @param contentDescription The string resource ID for the tab's content description, used for accessibility.
 */
enum class SearchDestination(
    val route: String,
    @StringRes val label: Int,
    @StringRes val contentDescription: Int,
) {
    PHOTOS("photos", R.string.photos, R.string.photos_tab_description),
    COLLECTIONS(
        "collections",
        R.string.collections,
        R.string.collections_tab_description,
    ),
    USERS("users", R.string.users, R.string.users_tab_description),
}
