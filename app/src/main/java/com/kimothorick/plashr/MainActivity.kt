package com.kimothorick.plashr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.kimothorick.plashr.navgraphs.MainNavigation
import com.kimothorick.plashr.profile.presentation.ProfileViewModel
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import com.kimothorick.plashr.ui.theme.PlashrTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main and single activity of the Plashr application.
 *
 * This activity serves as the entry point for the user interface. It is responsible for:
 * - Setting up the main Compose content view.
 * - Initializing and providing ViewModels ([MainViewModel], [com.kimothorick.plashr.settings.presentation.SettingsViewModel], [com.kimothorick.plashr.profile.presentation.ProfileViewModel])
 *   to the composable hierarchy.
 * - Managing the application's theme (light/dark mode and dynamic color) based on user settings.
 * - Handling the splash screen, keeping it visible until the initial application configuration is ready.
 * - Determining the window size class to support adaptive layouts.
 * - Setting up the main navigation graph ([com.kimothorick.plashr.navgraphs.MainNavigation]) once the app is ready.
 *
 * It uses Hilt for dependency injection, indicated by the `@AndroidEntryPoint` annotation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var windowSizeClass: WindowSizeClass
    private var isAppConfigReady by mutableStateOf(false)

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        installSplashScreen().setKeepOnScreenCondition { !isAppConfigReady }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val activeTheme by settingsViewModel.appTheme.collectAsState(initial = "System default")
            val useDynamicColor by settingsViewModel.appDynamicTheme.collectAsState(false)
            windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
            val selectedPhotoLayout = settingsViewModel.photoLayout.collectAsStateWithLifecycle()
            val applicationConfiguration by mainViewModel.appConfig.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = windowSizeClass, key2 = selectedPhotoLayout.value) {
                mainViewModel.updateConfig(windowSizeClass, selectedPhotoLayout.value)
            }

            LaunchedEffect(applicationConfiguration) {
                isAppConfigReady = true
            }

            PlashrTheme(
                darkTheme = when (activeTheme) {
                    "Light" -> false
                    "Dark" -> true
                    else -> isSystemInDarkTheme()
                },
                dynamicColor = useDynamicColor,
            ) {
                if (isAppConfigReady) {
                    MainNavigation(
                        mainViewModel = mainViewModel,
                        profileViewModel = profileViewModel,
                        settingsViewModel = settingsViewModel,
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
