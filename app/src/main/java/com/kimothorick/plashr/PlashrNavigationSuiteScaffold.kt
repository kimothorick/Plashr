package com.kimothorick.plashr

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PermMedia
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kimothorick.plashr.collections.presentation.CollectionsScreen
import com.kimothorick.plashr.home.presentation.HomeScreen
import com.kimothorick.plashr.home.presentation.HomeViewModel
import com.kimothorick.plashr.navgraphs.Settings
import com.kimothorick.plashr.profile.presentation.ProfileViewModel
import com.kimothorick.plashr.search.presentation.SearchScreen
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import me.saket.cascade.CascadeDropdownMenu

/**
 * A class that provides scaffolding for the Plashr navigation suite.
 *
 * This class encapsulates the navigation UI components and provides a wrapper function to create the main navigation UI.
 *
 * @param context The application context.
 * @param mainViewModel The MainViewModel providing data and functionality.
 * @param isAppAuthorized A boolean flag indicating whether the user is authorized.
 * @param onSettingsClicked A lambda function to be called when the settings icon is clicked.
 */
class PlashrNavigationSuiteScaffold(
    private val mainViewModel: MainViewModel,
    private val isAppAuthorized: Boolean,
    private val profileViewModel: ProfileViewModel,
    private val onSettingsClicked: () -> Unit,
) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PlashrNavigationWrapperUI(
        navController: NavHostController,
        profilePictureUrl: String?,
        homeViewModel: HomeViewModel = viewModel(),
        settingsViewModel: SettingsViewModel,
    ) {
        var currentDestination by rememberSaveable {mutableStateOf(AppDestinations.HOME)}
        val adaptiveInfo = currentWindowAdaptiveInfo()
        val navSuiteType =
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo = adaptiveInfo)
        val context = LocalContext.current

        /* LaunchedEffect(key1 = navController) {
             navController.currentBackStackEntryFlow.collect {entry ->
                 currentDestination =
                     AppDestinations.entries.find {it.screenRoute == entry.destination.route}
                         ?: AppDestinations.HOME
             }
         }
 */

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                it.icon,
                                contentDescription = stringResource(it.contentDescription)
                            )
                        },
                        label = {Text(stringResource(it.label))},
                        selected = it == currentDestination,
                        onClick = {currentDestination = it}
                    )
                }
            }
        ) {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            var isMenuVisible by rememberSaveable {mutableStateOf(false)}

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(title = {


                    }, actions = {
                        IconButton(onClick = {
                            isMenuVisible = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Localized description"
                            )
                        }
                        CascadeDropdownMenu(
                            expanded = isMenuVisible,
                            onDismissRequest = {isMenuVisible = false}) {
                            DropdownMenuItem(modifier = Modifier.background(
                                MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                                text = {Text(text = "Filter")},
                                onClick = {})
                            DropdownMenuItem(modifier = Modifier.background(
                                MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                                text = {Text("Settings")},
                                onClick = {
                                    navController.navigate(route = Settings)
                                    isMenuVisible = false
                                })
                        }
                    }, scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                            scrolledContainerColor = MaterialTheme.colorScheme.background
                        ), modifier = Modifier.wrapContentHeight() // Wrap the height of the content
                    )

                }) {contentPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {


                    when (currentDestination) {
                        AppDestinations.HOME -> {
                            HomeScreen(

                            )
                        }

                        AppDestinations.SEARCH -> {
                            SearchScreen()
                        }

                        AppDestinations.COLLECTIONS -> {
                            CollectionsScreen()
                        }
                    }

                }
            }

        }
    }


    /**
     * Composable function that creates the main navigation UI for the Plashr app.
     *
     * @param navController The NavHostController to manage navigation.
     */
    /*@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PlashrNavigationWrapperUI(
        navController: NavHostController,
        profilePictureUrl: String?,
        homeViewModel: HomeViewModel,
        settingsViewModel: SettingsViewModel,
    ) {
        var currentDestination by rememberSaveable {mutableStateOf(AppDestinations.HOME)}
        val adaptiveInfo = currentWindowAdaptiveInfo()
        val navSuiteType =
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo = adaptiveInfo)
        var hideToolbarProfilePicture = navSuiteType == NavigationSuiteType.NavigationBar
        var showManageAccountBottomSheet by remember {mutableStateOf(false)}

        *//*  LaunchedEffect(key1 = navController) {
              navController.currentBackStackEntryFlow.collect {entry ->
                  currentDestination =
                      AppDestinations.entries.find {it.screenRoute == entry.destination.route}
                          ?: AppDestinations.HOME
              }
          }



          if (showManageAccountBottomSheet) {
              ProfileComponents().ManageAccountBottomSheet(
                  showBottomSheet = showManageAccountBottomSheet,
                  onDismiss = {showManageAccountBottomSheet = false},
                  logout = {profileViewModel.logout()},
                  mainViewModel = mainViewModel,
                  profileViewModel = profileViewModel
              )
          }*//*

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                it.icon,
                                contentDescription = stringResource(it.contentDescription)
                            )
                        },
                        label = {Text(stringResource(it.label))},
                        selected = it == currentDestination,
                        onClick = {currentDestination = it}
                    )
                }
            }
        ) {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            var isMenuVisible by rememberSaveable {mutableStateOf(false)}

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(title = {


                    }, actions = {
                        IconButton(onClick = {
                            isMenuVisible = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Localized description"
                            )
                        }
                        CascadeDropdownMenu(
                            expanded = isMenuVisible,
                            onDismissRequest = {isMenuVisible = false}) {
                            androidx.compose.material3.DropdownMenuItem(modifier = Modifier.background(
                                MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                                text = {Text(text = "Filter")},
                                onClick = {})
                            androidx.compose.material3.DropdownMenuItem(modifier = Modifier.background(
                                MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                                text = {Text("Settings")},
                                onClick = {
                                    onSettingsClicked()
                                    isMenuVisible = false
                                })
                        }
                    }, scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                            scrolledContainerColor = MaterialTheme.colorScheme.background
                        ), modifier = Modifier.wrapContentHeight() // Wrap the height of the content
                    )


                    *//*PlashrMainScreenLargeTopAppBar(
                        titleResId = currentDestination.label,
                        navController = navController,
                        mainViewModel = mainViewModel,
                        isAppAuthorized = isAppAuthorized,
                        onSettingsClicked = onSettingsClicked,
                        hideToolbarProfilePicture = hideToolbarProfilePicture,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 0.dp),
                        profilePicURL = profilePictureUrl,
                        profileViewModel = profileViewModel,
                        scrollBehavior = scrollBehavior
                    )*//*
                }) {contentPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    when (currentDestination) {
                        AppDestinations.HOME -> {
                            HomeScreen(
                                homeViewModel = homeViewModel, settingsViewModel = settingsViewModel
                            )
                        }

                        AppDestinations.SEARCH -> {
                            SearchScreen()
                        }

                        AppDestinations.COLLECTIONS -> {
                            CollectionsScreen()
                        }
                    }
                }
            }

        }


        *//* NavigationSuiteScaffoldLayout(navigationSuite = {
             when (navSuiteType) {
                NavigationSuiteType.NavigationRail -> {
                    hideToolbarProfilePicture = true
                    NavigationRail(
                        header = {
                            if (isAppAuthorized) {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(MaterialTheme.shapes.extraLarge)
                                        .pointerInput(Unit) {
                                            detectTapGestures(onLongPress = {
                                                showManageAccountBottomSheet = true
                                            })
                                        },
                                    model = profilePictureUrl,
                                    contentDescription = null,
                                )

                            } else {
                                ProfileComponents().ProfilePictureIcon(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .clickable {
                                            mainViewModel.checkAndShowLoginBottomSheet(
                                                false
                                            )
                                        }, profilePictureSize = 36
                                )
                            }
                        }) {
                        Spacer(Modifier.weight(1f))
                        AppDestinations.entries.forEach {destination ->
                            NavigationRailItem(
                                icon = {
                                    Icon(
                                        destination.icon,
                                        contentDescription = stringResource(destination.contentDescription)
                                    )
                                },
                                label = {Text(stringResource(destination.label))},
                                selected = currentDestination == destination,
                                onClick = {
                                    currentDestination = destination
                                })
                        }
                        Spacer(Modifier.weight(1f))
                    }
                }

                NavigationSuiteType.NavigationBar -> {
                    hideToolbarProfilePicture = false
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        AppDestinations.entries.forEach {destination ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        destination.icon,
                                        contentDescription = stringResource(destination.contentDescription)
                                    )
                                },
                                label = {Text(stringResource(destination.label))},
                                selected = currentDestination == destination,
                                onClick = {
                                    currentDestination = destination
                                })
                        }
                    }
                }

                else -> {
                    NavigationSuite(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                        hideToolbarProfilePicture = true
                        AppDestinations.entries.forEach {destination ->
                            item(icon = {
                                Icon(
                                    destination.icon,
                                    contentDescription = stringResource(destination.contentDescription)
                                )
                            },
                                label = {Text(stringResource(destination.label))},
                                selected = currentDestination == destination,
                                onClick = {
                                    currentDestination = destination
                                })
                        }
                    }
                }
            }
        }
        ) {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    PlashrMainScreenLargeTopAppBar(
                        titleResId = currentDestination.label,
                        navController = navController,
                        mainViewModel = mainViewModel,
                        isAppAuthorized = isAppAuthorized,
                        onSettingsClicked = onSettingsClicked,
                        hideToolbarProfilePicture = hideToolbarProfilePicture,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 0.dp),
                        profilePicURL = profilePictureUrl,
                        profileViewModel = profileViewModel,
                        scrollBehavior = scrollBehavior
                    )
                }) {contentPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    when (currentDestination) {
                        AppDestinations.HOME -> {
                            HomeScreen(
                                homeViewModel = homeViewModel, settingsViewModel = settingsViewModel
                            )
                        }

                        AppDestinations.SEARCH -> {
                            SearchScreen()
                        }

                        AppDestinations.COLLECTIONS -> {
                            CollectionsScreen()
                        }
                    }
                }
            }
        }*//*

    }*/
}

/**
 * Enum representing the possible destinations in the navigation bar, rail, and drawer.
 *
 * @param label Resource ID for the label of the destination.
 * @param icon ImageVector representing the icon for the destination.
 * @param contentDescription Resource ID for the content description of the icon.
 * @param screenRoute The route used for navigation to this destination.
 */
enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int,
    val screenRoute: String,
) {
    HOME(
        R.string.home, Icons.Rounded.Home, R.string.home_content_description, "home"
    ),
    SEARCH(
        R.string.search, Icons.Rounded.Search, R.string.search_content_description, "search"
    ),
    COLLECTIONS(
        R.string.collections,
        Icons.Rounded.PermMedia,
        R.string.collections_content_description,
        "collections"
    )
}

