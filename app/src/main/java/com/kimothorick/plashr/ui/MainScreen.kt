package com.kimothorick.plashr.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.PlashrNavigationSuiteScaffold
import com.kimothorick.plashr.profile.domain.ProfileViewModel
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import me.saket.cascade.CascadeDropdownMenu

/**
 * The main screen of the application.
 *
 * This composable displays the main content of the app, including navigation and alogin bottom sheet if necessary.
 *
 * @param mainViewModel The [MainViewModel] providing data and functionality for the screen.
 * @param profileViewModel The [ProfileViewModel] providing profile-related data and functionality.
 * @param context The application context.
 * @param onSettingsClicked A lambdafunction to be called when the settings icon is clicked.
 */
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    context: Context,
    onSettingsClicked: () -> Unit
) {
    val navController = rememberNavController()
    val isAppAuthorized by profileViewModel.isAppAuthorized.collectAsState()
    val showLoginBottomSheet = mainViewModel.showLoginBottomSheet.collectAsState()

    LaunchedEffect(isAppAuthorized) {
        if (isAppAuthorized) {
            Log.i("Login", "User is logged in")
        } else {
            Log.i("Login", "User is not logged in")
        }
    }

    // Display the main navigation scaffold
    PlashrNavigationSuiteScaffold(
        context, mainViewModel = mainViewModel, isAppAuthorized, onSettingsClicked
    ).PlashrNavigationWrapperUI(
        navController = navController
    )

    // Show the login bottom sheet if necessary
    if (showLoginBottomSheet.value) {
        ProfileComponents().LoginBottomSheet(showBottomSheet = showLoginBottomSheet.value,
            onDismiss = {mainViewModel._showLoginBottomSheet.value = false})
    }

}

/**
 * A large top app bar with a title, navigation icon, and actions.
 *
 * This composable provides a customizable top app bar with scrolling behavior and support for profile pictures and settings.
 *
 * @param titleResId The resource ID of the title string.
 * @param navController The [NavHostController] used for navigation.
 * @param mainViewModel The [MainViewModel] providing data and functionality.
 * @param isAppAuthorizedA boolean flag indicating whether the user is authorized.
 * @param hideToolbarProfilePicture A boolean flag indicating whether to hide the profile picture in the toolbar.
 * @param onSettingsClicked A lambda function to be called when the settings icon is clicked.
 * @param modifier Modifier used to adjust the layout or appearance of the app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlashrMainScreenLargeTopAppBar(
    titleResId: Int,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    isAppAuthorized: Boolean,
    hideToolbarProfilePicture: Boolean,
    onSettingsClicked: () -> Unit,
    modifier: Modifier
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val toolbarBackgroundColor = MaterialTheme.colorScheme.surface
    var collapsedFraction by remember {mutableFloatStateOf(0f)}
    var isMenuVisible by rememberSaveable {mutableStateOf(false)}

    LaunchedEffect(scrollBehavior.state.collapsedFraction) {
        collapsedFraction = scrollBehavior.state.collapsedFraction
    }

    Surface(
        color = toolbarBackgroundColor,
        modifier = Modifier
            .wrapContentHeight() // Wrap the height of the content
            .nestedScroll(scrollBehavior.nestedScrollConnection) // Add nested scroll behavior
    ) {
        LargeTopAppBar(title = {
            Text(
                stringResource(id = titleResId),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alpha(1f - collapsedFraction)
            )
        }, navigationIcon = {
            if (!hideToolbarProfilePicture) {
                IconButton(onClick = {
                    mainViewModel.checkAndShowLoginBottomSheet(isAppAuthorized)
                }) {
                    if (isAppAuthorized) {
                        Log.i("Login", "No profile picture")
                    } else {
                        ProfileComponents().ProfilePictureIcon(
                            modifier = Modifier.size(30.dp),
                            24
                        )
                    }
                }
            }

        }, actions = {
            IconButton(onClick = {
                isMenuVisible = true
            }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "Localized description"
                )
            }
            CascadeDropdownMenu(expanded = isMenuVisible,
                onDismissRequest = {isMenuVisible = false}) {
                DropdownMenuItem(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow),
                    text = {Text(text = "Filter")},
                    onClick = {
                        Log.i("MainScreen", "LargeTopAppBar: Filter clicked")
                    })
                DropdownMenuItem(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow),
                    text = {Text("Settings")},
                    onClick = {
                        onSettingsClicked()
                        Log.i("MainScreen", "LargeTopAppBar: Settings clicked")
                        isMenuVisible = false
                    })
            }
        }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ), modifier = modifier
            .wrapContentHeight() // Wrap the height of the content
            .nestedScroll(scrollBehavior.nestedScrollConnection) // Add nested scroll behavior directly toLargeTopAppBar
        )

    }

    // Set the NavHostController in the MainViewModel
    LaunchedEffect(navController) {
        mainViewModel.setNavController(navController)
    }
}