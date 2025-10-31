package com.kimothorick.plashr.ui.mainscreen.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.profile.presentation.ProfileViewModel
import com.kimothorick.plashr.profile.presentation.components.LoginState
import com.kimothorick.plashr.profile.presentation.components.LoginState.IDLE
import com.kimothorick.plashr.profile.presentation.components.ManageAccountState
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import com.kimothorick.plashr.search.presentation.components.FilterBottomSheet
import com.kimothorick.plashr.search.presentation.search.SearchScreenViewModel
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.ui.mainscreen.PlashrNavigationWrapperUI
import kotlinx.coroutines.launch

/**
 * The main screen composable, which acts as a central hub for the application's UI.
 * It coordinates various UI states and interactions, including user authentication,
 * profile management, search functionality, and navigation to different parts of the app.
 *
 * This composable manages the display of several modal bottom sheets for logging in,
 * managing the user account, and filtering search results. It also handles the
 * Unsplash authentication flow using `rememberLauncherForActivityResult`.
 *
 * @param mainViewModel The [MainViewModel] responsible for managing the main screen's UI state,
 * such as the visibility of bottom sheets and the current search query.
 * @param profileViewModel The [ProfileViewModel] responsible for managing user profile data,
 * login state, and authentication logic.
 * @param settingsViewModel The [SettingsViewModel] for handling user settings.
 * @param searchScreenViewModel The [SearchScreenViewModel] for managing search-related logic and state.
 * @param onSettingsClicked A lambda function to be invoked when the settings icon is clicked.
 * @param onPhotoClicked A lambda function that is invoked when a photo is selected, providing the photo's ID.
 * @param onTopicClicked A lambda function that is invoked when a topic is selected, providing the topic's ID.
 * @param onUserClicked A lambda function that is invoked when a user's profile is selected, providing the username.
 * @param onViewProfileClicked A lambda function to navigate to the current user's profile view.
 * @param onEditProfileClicked A lambda function to navigate to the profile editing screen.
 * @param onCollectionClicked A lambda function that is invoked when a collection is selected, providing the collection's ID.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    settingsViewModel: SettingsViewModel,
    searchScreenViewModel: SearchScreenViewModel,
    onSettingsClicked: () -> Unit,
    onPhotoClicked: (String) -> Unit,
    onTopicClicked: (String) -> Unit,
    onUserClicked: (String) -> Unit,
    onViewProfileClicked: (String) -> Unit,
    onEditProfileClicked: () -> Unit,
    onCollectionClicked: (String) -> Unit,
) {
    val loginState by profileViewModel.loginState.collectAsStateWithLifecycle()
    val manageAccountState by profileViewModel.manageAccountState.collectAsStateWithLifecycle()
    val showManageAccountBottomSheet by mainViewModel.showManageAccountBottomSheet.collectAsStateWithLifecycle()
    val showLoginBottomSheet by mainViewModel.showLoginBottomSheet.collectAsStateWithLifecycle()
    val profilePictureUrl by profileViewModel.profilePictureUrl.collectAsStateWithLifecycle(
        initialValue = null,
    )
    val firstName by profileViewModel.firstName.collectAsStateWithLifecycle(
        initialValue = null,
    )
    val lastName by profileViewModel.lastName.collectAsStateWithLifecycle(
        initialValue = null,
    )
    val selectedUsername by profileViewModel.username.collectAsStateWithLifecycle(
        initialValue = null,
    )
    val searchQuery by mainViewModel.searchQuery.collectAsStateWithLifecycle()

    //region Unsplash Auth Refactoring
    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { activityResult ->
        profileViewModel.handleUnsplashAuthResult(activityResult)
    }

    val unsplashAuthIntent by profileViewModel.unsplashAuthIntent.collectAsStateWithLifecycle()

    LaunchedEffect(unsplashAuthIntent) {
        unsplashAuthIntent?.let { intent ->
            authLauncher.launch(intent)
            profileViewModel.consumeUnsplashAuthIntent() // Consume after launching
        }
    }
    //endregion

    //region New state for Filter Bottom Sheet
    val showFilterBottomSheet by mainViewModel.showFilterBottomSheet.collectAsStateWithLifecycle()
    val filterOptions by mainViewModel.filterOptions.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    //endregion

    PlashrNavigationWrapperUI(
        profilePictureUrl = profilePictureUrl,
        onSettingsClicked = {
            onSettingsClicked()
        },
        onPhotoSelected = { selectedPhotoUrl ->
            onPhotoClicked(selectedPhotoUrl)
        },
        onTopicSelected = { selectedTopicId ->
            onTopicClicked(selectedTopicId)
        },
        mainViewModel = mainViewModel,
        userProfileViewModel = profileViewModel,
        settingsViewModel = settingsViewModel,
        searchScreenViewModel = searchScreenViewModel,
        onCollectionSelected = { selectedCollectionId ->
            onCollectionClicked(selectedCollectionId)
        },
        onUserSelected = { selectedUsername ->
            onUserClicked(selectedUsername)
        },
        onViewProfileClicked = { selectedUsername ->
            onViewProfileClicked(selectedUsername)
        },
        onFilterClicked = {
            mainViewModel.setShowFilterBottomSheet(true)
        },
        onSearchQueryChanged = { query ->
            mainViewModel.setSearchQuery(query)
        },
    )

    //region Bottom Sheets
    //region Login Bottom Sheet
    if (showLoginBottomSheet) {
        ProfileComponents().LoginBottomSheet(
            isVisible = true,
            onDismiss = {
                if (profileViewModel.loginState.value != LoginState.SUCCESS &&
                    profileViewModel.loginState.value != LoginState.IN_PROGRESS
                ) {
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
    //endregion

    //region Manage Account Bottom Sheet
    if (showManageAccountBottomSheet) {
        val hapticFeedback = LocalHapticFeedback.current
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        ProfileComponents().ManageAccountBottomSheet(
            isVisible = true,
            onDismiss = {
                mainViewModel.setShowManageAccountBottomSheet(false)
            },
            onLogout = {
                profileViewModel.setManageAccountState(ManageAccountState.LOGGING_OUT)
                profileViewModel.logout()
            },
            onEditProfile = {
                mainViewModel.setShowManageAccountBottomSheet(false)
                onEditProfileClicked()
            },
            profilePictureUrl = profilePictureUrl,
            firstName = firstName,
            lastName = lastName,
            username = selectedUsername,
            manageAccountState = manageAccountState,
        )
    }
    //endregion
    //region Filter Bottom Sheet
    if (showFilterBottomSheet) {
        FilterBottomSheet(
            sheetState = sheetState,
            initialFilters = filterOptions,
            onDismiss = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        mainViewModel.setShowFilterBottomSheet(false)
                    }
                }
            },
            onApplyFilters = { filtersToApply ->
                mainViewModel.applyFilters(filtersToApply)
                searchScreenViewModel.executeSearch(searchQuery, filtersToApply)
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        mainViewModel.setShowFilterBottomSheet(false)
                    }
                }
            },
        )
    }
    //endregion
    //endregion
}
