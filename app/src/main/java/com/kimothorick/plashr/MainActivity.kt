package com.kimothorick.plashr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.kimothorick.plashr.navgraphs.MainNavigation
import com.kimothorick.plashr.profile.domain.ProfileViewModel
import com.kimothorick.plashr.settings.domain.SettingsViewModel
import com.kimothorick.plashr.ui.theme.PlashrTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 *
 * This activity is responsible for setting up the main navigation and handling the app theme.
 * It utilizes Hilt for dependency injection and Jetpack Compose for UI.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            var selectedTheme by remember {
                mutableStateOf("")
            }
            // Collect the app theme setting and update the selectedTheme state.
            LaunchedEffect(Unit) {
                settingsViewModel.appTheme.collect {theme ->
                    selectedTheme = theme
                }
            }
            // Apply the selected theme to the PlashrTheme composable
            PlashrTheme(
                darkTheme = when (selectedTheme) {
                    "Light" -> false
                    "Dark" -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                // Set up the main navigation graph.
                MainNavigation(
                    navController = navController,
                    settingsViewModel = settingsViewModel,
                    profileViewModel = profileViewModel,
                    viewModel = mainViewModel,
                    context = this
                )

            }
        }

    }
}