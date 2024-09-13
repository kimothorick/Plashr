package com.kimothorick.plashr.navgraphs

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kimothorick.plashr.MainActivity
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.profile.domain.ProfileViewModel
import com.kimothorick.plashr.settings.domain.SettingsViewModel
import com.kimothorick.plashr.settings.presentation.AppInfoScreen
import com.kimothorick.plashr.settings.presentation.SettingsScreen
import com.kimothorick.plashr.ui.MainScreen
import kotlinx.serialization.Serializable

@Serializable
object MainScreen

@Serializable
object Settings

@Serializable
object AppInfo

@Serializable
object LoggedUserProfile

/**
 * Defines the main navigation graph for the application.
 *
 * This composable function sets up the navigation structure using a [NavHost] and handles navigation
 * between different screens: [MainScreen], [SettingsScreen], and [AppInfoScreen]. It also defines
 * the transitions between these screens using slide animations.
 *
 * @param navController The [NavHostController] used to manage navigation within the graph.
 *@param viewModel The [MainViewModel] providing data and functionality for the [MainScreen].
 * @param profileViewModel The [ProfileViewModel] providing profile-related data.
 * @param context The application context.
 * @param settingsViewModel The [SettingsViewModel] providing data and functionality for the [SettingsScreen].
 * @param mainActivity The [MainActivity] instance, used for context and interactions with the Activity.
 */
@Composable
fun MainNavigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    context: Context,
    settingsViewModel: SettingsViewModel,
    mainActivity: MainActivity
) {
    NavHost(navController = navController, startDestination = MainScreen) {

        // Main Screen Route
        composable<MainScreen>(popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, tween(300)
            )
        }) {
            MainScreen(mainViewModel = viewModel,
                profileViewModel = profileViewModel,
                context = context,
                onSettingsClicked = {
                    navController.navigate(route = Settings)
                },mainActivity = mainActivity)
        }

        // Settings Screen Route
        composable<Settings>(enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, tween(300)
            )
        }) {SettingsScreen(navController, settingsViewModel,context)}

        // App Info Screen Route
        composable<AppInfo>(enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, tween(300)
            )
        }) {AppInfoScreen(navController, context = context)}
    }
}
