package com.kimothorick.plashr.ui.mainscreen

import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.Companion.NavigationBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType.Companion.NavigationRail
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.collections.presentation.CollectionsScreen
import com.kimothorick.plashr.collections.presentation.CollectionsScreenViewModel
import com.kimothorick.plashr.home.presentation.HomeScreen
import com.kimothorick.plashr.home.presentation.HomeViewModel
import com.kimothorick.plashr.profile.presentation.ProfileViewModel
import com.kimothorick.plashr.search.presentation.search.SearchDestination
import com.kimothorick.plashr.search.presentation.search.SearchScreen
import com.kimothorick.plashr.search.presentation.search.SearchScreenViewModel
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.ui.components.ConnectionStatus
import com.kimothorick.plashr.ui.mainscreen.components.PlashrMainScreenLargeTopAppBar
import com.kimothorick.plashr.ui.mainscreen.components.PlashrSearchBar

/**
 * A wrapper composable that sets up the main navigation and screen layout for the Plashr app.
 * It uses [NavigationSuiteScaffoldLayout] to adapt the navigation UI (`NavigationBar`, `NavigationRail`)
 * based on the screen size. It also manages the top app bar, switching between a standard
 * top bar and a search bar depending on the current navigation destination.
 *
 * This composable orchestrates the main app screens (`HomeScreen`, `SearchScreen`, `CollectionsScreen`)
 * within a [NavHost] and handles the display of a connection status banner.
 *
 * @param profilePictureUrl The URL for the user's profile picture, used in the top app bar.
 * @param onSettingsClicked Lambda triggered when the settings icon is clicked.
 * @param onPhotoSelected Lambda triggered when a photo is selected, passing the photo's ID.
 * @param onTopicSelected Lambda triggered when a topic is selected, passing the topic's slug.
 * @param onCollectionSelected Lambda triggered when a collection is selected, passing the collection's ID.
 * @param onUserSelected Lambda triggered when a user's profile is selected, passing the username.
 * @param onViewProfileClicked Lambda triggered to view the current user's profile, passing the username.
 * @param onFilterClicked Lambda triggered when the search filter button is clicked.
 * @param onSearchQueryChanged Lambda triggered when the text in the search bar changes.
 * @param mainViewModel The main ViewModel providing shared state like connectivity and search query.
 * @param userProfileViewModel The ViewModel for user-related data like authorization status and username.
 * @param settingsViewModel The ViewModel for settings-related data.
 * @param searchScreenViewModel The ViewModel specifically for handling search logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlashrNavigationWrapperUI(
    profilePictureUrl: String?,
    onSettingsClicked: () -> Unit,
    onPhotoSelected: (String) -> Unit,
    onTopicSelected: (String) -> Unit,
    onCollectionSelected: (String) -> Unit,
    onUserSelected: (String) -> Unit,
    onViewProfileClicked: (String) -> Unit,
    onFilterClicked: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    mainViewModel: MainViewModel,
    userProfileViewModel: ProfileViewModel,
    settingsViewModel: SettingsViewModel,
    searchScreenViewModel: SearchScreenViewModel,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val adaptiveInfo = currentWindowAdaptiveInfo()
    var navigationSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo = adaptiveInfo)
    val isAppAuthorized by userProfileViewModel.isAppAuthorized.collectAsState()
    val isConnected by mainViewModel.isConnected.collectAsStateWithLifecycle()
    val searchQuery by mainViewModel.searchQuery.collectAsStateWithLifecycle()
    val username by userProfileViewModel.username.collectAsStateWithLifecycle("")
    val pagerState = rememberPagerState { SearchDestination.entries.size }

    LaunchedEffect(adaptiveInfo) {
        navigationSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    }

    NavigationSuiteScaffoldLayout(
        navigationSuite = {
            PlashrNavigation(
                navController = navController,
                currentDestination = currentDestination,
                navigationSuiteType = navigationSuiteType,
            )
        },
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        val isNavigationBarVisible = navigationSuiteType == NavigationBar
        val currentScaffoldInsets = if (isNavigationBarVisible) {
            WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
        } else {
            WindowInsets.safeDrawing
        }

        Scaffold(
            contentWindowInsets = currentScaffoldInsets,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                when (currentDestination?.route) {
                    AppDestinations.SEARCH.screenRoute -> {
                        val showFilterButton = pagerState.currentPage == SearchDestination.PHOTOS.ordinal
                        val filterOptions by mainViewModel.filterOptions.collectAsStateWithLifecycle()

                        PlashrSearchBar(
                            query = searchQuery,
                            onSearchTriggered = { searchQuery ->
                                searchScreenViewModel.executeSearch(searchQuery, filters = filterOptions)
                            },
                            onQueryChanged = onSearchQueryChanged,
                            showFilterButton = showFilterButton,
                            onFilterClick = {
                                if (showFilterButton) onFilterClicked()
                            },
                            modifier = Modifier.windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
                            ),
                        )
                    }

                    else -> {
                        PlashrMainScreenLargeTopAppBar(
                            onSettingsClicked = { onSettingsClicked() },
                            username = username,
                            scrollBehavior = scrollBehavior,
                            showManageAccountBottomSheet = {
                                mainViewModel.setShowManageAccountBottomSheet(true)
                            },
                            onLoginClicked = {
                                mainViewModel.setShowLoginBottomSheet(true)
                            },
                            onViewProfileClicked = { username ->
                                onViewProfileClicked(username)
                            },
                            profilePictureUrl = profilePictureUrl,
                            isAppAuthorized = isAppAuthorized,
                            isAppIconHidden = isNavigationBarVisible,
                        )
                    }
                }
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        contentPadding,
                    ),
            ) {
                ConnectionStatus(isConnected = isConnected, stringResource(R.string.offline_status_prompt))

                NavHost(
                    navController = navController,
                    startDestination = AppDestinations.HOME.screenRoute,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None },
                ) {
                    composable(AppDestinations.HOME.screenRoute) {
                        val homeViewModel = hiltViewModel<HomeViewModel>()
                        HomeScreen(
                            mainViewModel = mainViewModel,
                            homeViewModel = homeViewModel,
                            settingsViewModel = settingsViewModel,
                            onPhotoClicked = onPhotoSelected,
                            onTopicClicked = onTopicSelected,
                            onUserClicked = onUserSelected,
                        )
                    }
                    composable(AppDestinations.SEARCH.screenRoute) {
                        SearchScreen(
                            searchPagerState = pagerState,
                            mainViewModel = mainViewModel,
                            searchScreenViewModel = searchScreenViewModel,
                            settingsViewModel = settingsViewModel,
                            onPhotoSelected = onPhotoSelected,
                            onCollectionSelected = onCollectionSelected,
                            onUserSelected = onUserSelected,
                        )
                    }
                    composable(AppDestinations.COLLECTIONS.screenRoute) {
                        val collectionsViewModel = hiltViewModel<CollectionsScreenViewModel>()
                        CollectionsScreen(
                            collectionsViewModel = collectionsViewModel,
                            onCollectionClicked = onCollectionSelected,
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable that provides the primary navigation UI for the app.
 *
 * It adapts its layout based on the provided [navigationSuiteType], displaying a
 * [NavigationRail] for larger screens, a [NavigationBar] for smaller screens (like phones),
 * or a default [NavigationSuite] for other configurations.
 *
 * The navigation items are sourced from the [AppDestinations] enum.
 * This component handles the navigation logic, ensuring that state is saved and restored
 * correctly when switching between top-level destinations and preventing re-selection of the
 * currently active destination.
 *
 * @param navController The [NavHostController] used to navigate between screens.
 * @param currentDestination The current [NavDestination] in the navigation graph, used to highlight the active item.
 * @param navigationSuiteType The type of navigation suite to display, determined by the window size class.
 * @param modifier The [Modifier] to be applied to the navigation component.
 */
@Composable
private fun PlashrNavigation(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationSuiteType: NavigationSuiteType,
    modifier: Modifier = Modifier,
) {
    when (navigationSuiteType) {
        NavigationRail -> {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.background,
                header = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = stringResource(R.string.app_icon),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .size(24.dp),
                    )
                },
            ) {
                Spacer(modifier.weight(1f))
                AppDestinations.entries.forEach { destination ->
                    Spacer(modifier.height(2.dp))
                    NavigationRailItem(
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (currentDestination?.route == destination.screenRoute) {
                                        destination.unselectedIcon
                                    } else {
                                        destination.selectedIcon
                                    },
                                ),
                                contentDescription = destination.contentDescription.toString(),
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == destination.screenRoute
                        } == true,
                        onClick = {
                            if (navController.currentBackStackEntry?.destination?.route != destination.screenRoute) {
                                navController.navigate(destination.screenRoute) {
                                    popUpTo(findStartDestination(graph = navController.graph).id) {
                                        saveState = true
                                    }
                                    restoreState = true
                                }
                            }
                        },
                    )
                }
                Spacer(modifier.weight(1f))
            }
        }

        // Display NavigationBar
        NavigationBar -> {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                windowInsets = WindowInsets.navigationBars,
            ) {
                // NEW NAVIGATION
                AppDestinations.entries.forEach { destination ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (currentDestination?.route == destination.screenRoute) {
                                        destination.unselectedIcon
                                    } else {
                                        destination.selectedIcon
                                    },
                                ),
                                contentDescription = destination.contentDescription.toString(),
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == destination.screenRoute
                        } == true,
                        onClick = {
                            if (navController.currentBackStackEntry?.destination?.route != destination.screenRoute) {
                                navController.navigate(destination.screenRoute) {
                                    popUpTo(findStartDestination(graph = navController.graph).id) {
                                        saveState = true
                                    }
                                    restoreState = true
                                }
                            }
                        },
                    )
                }
            }
        }

        // Display NavigationSuite
        else -> {
            NavigationSuite(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
                AppDestinations.entries.forEach { destination ->
                    item(
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (currentDestination?.route == destination.screenRoute) {
                                        destination.unselectedIcon
                                    } else {
                                        destination.selectedIcon
                                    },
                                ),
                                contentDescription = destination.contentDescription.toString(),
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == destination.screenRoute
                        } == true,
                        onClick = {
                            if (navController.currentBackStackEntry?.destination?.route != destination.screenRoute) {
                                navController.navigate(destination.screenRoute) {
                                    popUpTo(findStartDestination(graph = navController.graph).id) {
                                        saveState = true
                                    }

                                    restoreState = true
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

/**
 * Represents the primary navigation destinations within the application.
 *
 * Each enum constant holds metadata required to display it in various navigation components,
 * such as a navigation bar or navigation rail. This includes labels, icons, and navigation routes.
 *
 * @param label The string resource ID for the destination's label, used in navigation items.
 * @param title A simple string title for the destination, potentially for use in top app bars.
 * @param selectedIcon The drawable resource ID for the icon when the destination is not selected.
 * @param unselectedIcon The drawable resource ID for the icon when the destination is selected.
 * @param contentDescription The string resource ID for the icon's content description, for accessibility.
 * @param screenRoute The unique route string used by the NavController to navigate to this destination's screen.
 */
enum class AppDestinations(
    @param:StringRes val label: Int,
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    @param:StringRes val contentDescription: Int,
    val screenRoute: String,
) {
    HOME(
        label = R.string.home,
        title = "Home",
        selectedIcon = R.drawable.rounded_home_24,
        unselectedIcon = R.drawable.rounded_home_filled_24,
        contentDescription = R.string.home_content_description,
        screenRoute = "home",
    ),
    SEARCH(
        label = R.string.search,
        title = "Search",
        selectedIcon = R.drawable.rounded_search_24,
        unselectedIcon = R.drawable.rounded_search_24,
        contentDescription = R.string.search_content_description,
        screenRoute = "search",
    ),
    COLLECTIONS(
        label = R.string.collections,
        title = "Collections",
        selectedIcon = R.drawable.rounded_perm_media_24,
        unselectedIcon = R.drawable.rounded_perm_media_filled_24,
        contentDescription = R.string.collections_content_description,
        screenRoute = "collections",
    ),
}

/**
 * Finds the start destination of a navigation graph.
 *
 * This function is used to determine the root destination for the navigation stack. When navigating
 * between bottom bar items, it helps to pop the back stack up to this start destination,
 * ensuring a clean navigation history and preventing a deep stack of top-level destinations.
 *
 * @param graph The [NavGraph] to search within.
 * @return The start [NavDestination] if found, otherwise returns the [NavGraph] itself as a fallback.
 */
private fun findStartDestination(
    graph: NavGraph,
): NavDestination = graph.findNode(graph.startDestinationId) ?: graph
