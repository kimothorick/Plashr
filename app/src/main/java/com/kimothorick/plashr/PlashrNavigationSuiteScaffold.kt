package com.kimothorick.plashr

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PermMedia
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.kimothorick.plashr.collections.presentation.CollectionsScreen
import com.kimothorick.plashr.home.presentation.HomeScreen
import com.kimothorick.plashr.profile.domain.ProfileViewModel
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import com.kimothorick.plashr.search.presentation.SearchScreen
import com.kimothorick.plashr.ui.PlashrMainScreenLargeTopAppBar

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
    private val context: Context,
    private val mainViewModel: MainViewModel,
    private val isAppAuthorized: Boolean,
    private val profileViewModel: ProfileViewModel,
    private val onSettingsClicked: () -> Unit
) {

    /**
     * Composable function that creates the main navigation UI for the Plashr app.
     *
     * @param navController The NavHostController to manage navigation.
     */
    @Composable
    fun PlashrNavigationWrapperUI(
        navController: NavHostController, profilePictureUrl: String?
    ) {
        var currentDestination by rememberSaveable {mutableStateOf(AppDestinations.HOME)}
        val navSuiteType =
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
        var hideToolbarProfilePicture = navSuiteType == NavigationSuiteType.NavigationBar
        var showManageAccountBottomSheet by remember {mutableStateOf(false)}

        LaunchedEffect(navController) {
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
           }

        NavigationSuiteScaffoldLayout(navigationSuite = {
            when (navSuiteType) {
                NavigationSuiteType.NavigationRail -> {
                    hideToolbarProfilePicture = true
                    NavigationRail(header = {
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
                            NavigationRailItem(icon = {
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
                            NavigationBarItem(icon = {
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
        }) {
            Scaffold(topBar = {
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
                    profileViewModel = profileViewModel
                )
            }) {contentPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    when (currentDestination) {
                        AppDestinations.HOME -> {
                            HomeScreen()
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
    val screenRoute: String
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

