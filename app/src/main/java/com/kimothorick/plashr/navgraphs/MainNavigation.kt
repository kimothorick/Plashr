package com.kimothorick.plashr.navgraphs

import android.net.Uri
import android.os.Bundle
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.collectionDetails.presentation.CollectionDetailsScreen
import com.kimothorick.plashr.collectionDetails.presentation.CollectionDetailsViewModel
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.photoDetails.presentation.PhotoActionsViewModel
import com.kimothorick.plashr.photoDetails.presentation.PhotoDetailsScreen
import com.kimothorick.plashr.photoDetails.presentation.PhotoDetailsViewModel
import com.kimothorick.plashr.photoDetails.presentation.PhotoFullPreviewScreen
import com.kimothorick.plashr.profile.presentation.ProfileViewModel
import com.kimothorick.plashr.profile.presentation.UserProfileScreen
import com.kimothorick.plashr.profile.presentation.UserProfileScreenViewModel
import com.kimothorick.plashr.profile.presentation.editprofile.EditProfileScreen
import com.kimothorick.plashr.profile.presentation.editprofile.EditProfileScreenViewModel
import com.kimothorick.plashr.search.presentation.search.SearchScreenViewModel
import com.kimothorick.plashr.settings.presentation.SettingsScreen
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.topics.presentation.TopicDetailsScreenContent
import com.kimothorick.plashr.topics.presentation.TopicDetailsViewModel
import com.kimothorick.plashr.ui.mainscreen.presentation.MainScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
object MainScreen

@Serializable
object Settings

@Serializable
object EditProfileScreen

@Serializable
data class TopicDetails(
    val id: String,
)

@Serializable
data class PhotoDetails(
    val id: String,
)

@Serializable
data class CollectionDetails(
    val id: String,
)

@Serializable
data class PhotoFullPreview(
    val photoUrl: String,
    val photo: Photo,
)

@Serializable
data class UserProfileDetails(
    val username: String,
)

val photoPreviewParametersType = object : NavType<Photo>(
    isNullableAllowed = false,
) {
    override fun put(
        bundle: Bundle,
        key: String,
        value: Photo,
    ) {
        bundle.putParcelable(key, value)
    }

    @Suppress("DEPRECATION")
    override fun get(
        bundle: Bundle,
        key: String,
    ): Photo {
        return bundle.getParcelable(key)!!
    }

    override fun serializeAsValue(
        value: Photo,
    ): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun parseValue(
        value: String,
    ): Photo {
        return Json.decodeFromString<Photo>(value)
    }
}

// --- Transition Constants and Definitions ---
private const val DEFAULT_TRANSITION_DURATION_MS = 300

// Slide-in from left transition
private val slideInFromLeft: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        tween(DEFAULT_TRANSITION_DURATION_MS),
    )
}

// Slide-out to left transition
private val slideOutToLeft: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        tween(DEFAULT_TRANSITION_DURATION_MS),
    )
}

// Slide-in from right transition (for pop actions)
private val slideInFromRight: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        tween(DEFAULT_TRANSITION_DURATION_MS),
    )
}

// Slide-out to right transition (for pop actions)
private val slideOutToRight: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        tween(DEFAULT_TRANSITION_DURATION_MS),
    )
}

/**
 * Defines the main navigation graph for the application.
 *
 * This composable function sets up a [NavHost] with all the possible destinations
 * the user can navigate to from the main screen and its subsequent screens. It handles
 * navigation between various features like photo details, topics, collections, user profiles,
 * and settings. It also defines custom transitions for screen navigation and handles deep linking.
 *
 * The navigation is managed by a `rememberNavController()`, and Hilt is used to provide
 * ViewModels to the different screen composables.
 *
 * @param mainViewModel The shared [MainViewModel] used across several screens for global state.
 * @param profileViewModel The shared [ProfileViewModel] for managing user profile data.
 * @param settingsViewModel The shared [SettingsViewModel] for handling application settings.
 */
@Composable
fun MainNavigation(
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainScreen,
    ) {
        composable<MainScreen>(
            popEnterTransition = slideInFromRight,
            exitTransition = slideOutToLeft,
        ) {
            val searchScreenViewModel = hiltViewModel<SearchScreenViewModel>()
            MainScreen(
                onSettingsClicked = {
                    navController.navigate(route = Settings)
                },
                mainViewModel = mainViewModel,
                profileViewModel = profileViewModel,
                settingsViewModel = settingsViewModel,
                searchScreenViewModel = searchScreenViewModel,
                onPhotoClicked = { photoId ->
                    navController.navigate(route = PhotoDetails(id = photoId))
                },
                onTopicClicked = { topicId ->
                    navController.navigate(route = TopicDetails(id = topicId))
                },
                onCollectionClicked = { collectionId ->
                    navController.navigate(route = CollectionDetails(id = collectionId))
                },
                onUserClicked = { username ->
                    navController.navigate(route = UserProfileDetails(username = username))
                },
                onViewProfileClicked = { username ->
                    navController.navigate(route = UserProfileDetails(username = username))
                },
                onEditProfileClicked = {
                    navController.navigate(route = EditProfileScreen)
                },
            )
        }

        composable<Settings>(
            enterTransition = slideInFromLeft,
            exitTransition = slideOutToLeft,
            popEnterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
        ) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onBackClicked = {
                    navController.navigateUp()
                },
            )
        }

        composable<TopicDetails>(
            deepLinks = listOf(
                navDeepLink<TopicDetails>(basePath = Constants.UnsplashLinks.TOPIC_DEEP_LINK_BASE_URL),
            ),
            enterTransition = slideInFromLeft,
            exitTransition = slideOutToLeft,
            popEnterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<TopicDetails>()
            val topicId = args.id
            val topicDetailsViewModel = hiltViewModel<TopicDetailsViewModel>()

            TopicDetailsScreenContent(
                topicId = topicId,
                mainViewModel = mainViewModel,
                settingsViewModel = settingsViewModel,
                topicDetailsViewModel = topicDetailsViewModel,
                onPhotoTapped = { photoId ->
                    navController.navigate(route = PhotoDetails(id = photoId))
                },
                onUserTapped = { username ->
                    navController.navigate(route = UserProfileDetails(username = username))
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
            )
        }

        composable<PhotoDetails>(
            deepLinks = listOf(
                navDeepLink<PhotoDetails>(basePath = Constants.UnsplashLinks.PHOTOS_DEEP_LINK_BASE_URL),
            ),
            enterTransition = slideInFromLeft,
            exitTransition = slideOutToLeft,
            popEnterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<PhotoDetails>()
            val photoId = args.id

            val photoDetailsViewModel = hiltViewModel<PhotoDetailsViewModel>()
            val photoActionsViewModel = hiltViewModel<PhotoActionsViewModel>()

            PhotoDetailsScreen(
                photoId = photoId,
                photoDetailsViewModel = photoDetailsViewModel,
                photoActionsViewModel = photoActionsViewModel,
                mainViewModel = mainViewModel,
                profileViewModel = profileViewModel,
                onBackClicked = {
                    navController.navigateUp()
                },
                navController = navController,
                onUserClicked = { username ->
                    navController.navigate(route = UserProfileDetails(username))
                },
            )
        }

        composable<PhotoFullPreview>(
            enterTransition = slideInFromLeft,
            exitTransition = slideOutToLeft,
            popEnterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
            typeMap = mapOf(
                typeOf<Photo>() to photoPreviewParametersType,
            ),
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<PhotoFullPreview>()
            val photoURL = args.photoUrl

            PhotoFullPreviewScreen(
                photoURL = photoURL,
                onBackClicked = {
                    navController.navigateUp()
                },
            )
        }

        composable<CollectionDetails>(
            deepLinks = listOf(
                navDeepLink<CollectionDetails>(basePath = Constants.UnsplashLinks.COLLECTION_DEEP_LINK_BASE_URL),
            ),
            enterTransition = slideInFromLeft,
            exitTransition = slideOutToLeft,
            popEnterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<CollectionDetails>()
            val collectionId = args.id
            val collectionTopicDetailsViewModel = hiltViewModel<CollectionDetailsViewModel>()

            CollectionDetailsScreen(
                collectionId = collectionId,
                mainViewModel = mainViewModel,
                settingsViewModel = settingsViewModel,
                collectionDetailsViewModel = collectionTopicDetailsViewModel,
                onBackClicked = { shouldRefresh ->
                    if (shouldRefresh) {
                        navController.previousBackStackEntry?.savedStateHandle?.set("shouldRefreshCollections", true)
                    }
                    navController.navigateUp()
                },
                onPhotoClicked = { photoId ->
                    navController.navigate(route = PhotoDetails(id = photoId))
                },
                onUserClicked = { username ->
                    navController.navigate(route = UserProfileDetails(username = username))
                },
                profileViewModel = profileViewModel,
            )
        }

        composable<UserProfileDetails>(
            deepLinks = listOf(
                navDeepLink<UserProfileDetails>(basePath = Constants.UnsplashLinks.USER_PROFILE_DEEP_LINK_BASE_URL),
            ),
            enterTransition = slideInFromLeft,
            exitTransition = slideOutToLeft,
            popEnterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<UserProfileDetails>()
            val username = args.username

            val userProfileScreenViewModel = hiltViewModel<UserProfileScreenViewModel>()

            UserProfileScreen(
                username = username,
                mainViewModel = mainViewModel,
                settingsViewModel = settingsViewModel,
                profileViewModel = profileViewModel,
                userProfileScreenViewModel = userProfileScreenViewModel,
                onBackClicked = {
                    navController.navigateUp()
                },
                onPhotoSelected = { photoId ->
                    navController.navigate(route = PhotoDetails(id = photoId))
                },
                onCollectionSelected = { collectionId ->
                    navController.navigate(route = CollectionDetails(id = collectionId))
                },
                onEditProfileClicked = {
                    navController.navigate(route = EditProfileScreen)
                },
                navController = navController,
            )
        }

        composable<EditProfileScreen>(
            enterTransition = slideInFromLeft,
            exitTransition = slideOutToLeft,
            popEnterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
        ) {
            val editProfileScreenViewModel = hiltViewModel<EditProfileScreenViewModel>()

            EditProfileScreen(
                onBackClicked = {
                    navController.navigateUp()
                },
                viewModel = editProfileScreenViewModel,
            )
        }
    }
}
