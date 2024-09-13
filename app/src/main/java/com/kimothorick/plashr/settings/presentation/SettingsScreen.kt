package com.kimothorick.plashr.settings.presentation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.GppMaybe
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kimothorick.plashr.navgraphs.AppInfo
import com.kimothorick.plashr.settings.data.SettingAction
import com.kimothorick.plashr.settings.data.SettingCategory
import com.kimothorick.plashr.settings.data.SettingDialogData
import com.kimothorick.plashr.settings.data.SettingOption
import com.kimothorick.plashr.settings.domain.SettingsDataStore
import com.kimothorick.plashr.settings.domain.SettingsViewModel
import com.kimothorick.plashr.settings.presentation.components.SettingsCategory
import kotlinx.coroutines.launch

/**
 * Composable function that displays the Settings screen of the application.
 * It provides a list of configurable settings categorized into groups. Each setting option can
 * trigger different actions, such as opening dialogs for choosing themes, layouts, or download
 * quality, clearing cache, or navigating to other screens like the App Info screen.
 *
 * @param navController Navigation controller used for navigating between screens.
 * @param settingsViewModel ViewModel responsible for managing settings data.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController, settingsViewModel: SettingsViewModel, context: Context
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var showDialog by remember {mutableStateOf<SettingDialogData?>(null)}
    var selectedTheme by remember {
        mutableStateOf("")
    }
    var selectedLayout by remember {
        mutableStateOf("")
    }
    var selectedDownloadQuality by remember {
        mutableStateOf("")
    }

    var cacheSize by remember {
        mutableDoubleStateOf(settingsViewModel.getCacheSizeInMB(context.cacheDir))
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {SnackbarHostState()}

    LaunchedEffect(Unit) {
        settingsViewModel.appTheme.collect {theme ->
            selectedTheme = theme
        }
    }
    LaunchedEffect(Unit) {
        settingsViewModel.photoLayout.collect {layout ->
            selectedLayout = layout
        }
    }
    LaunchedEffect(Unit) {
        settingsViewModel.downloadQuality.collect {downloadQuality ->
            selectedDownloadQuality = downloadQuality
        }
    }
    // Centralized function to handle setting clicks
    val handleSettingClick: (SettingOption) -> Unit = {option ->
        when (option.action) {
            SettingAction.OpenThemeDialog -> {
                val themeOptions = SettingsDataStore.themeOptions
                val currentThemeIndex = themeOptions.indexOf(selectedTheme)
                showDialog = SettingDialogData(title = "Choose app theme",
                    options = themeOptions,
                    initialSelectedIndex = currentThemeIndex,
                    onOptionSelected = {selectedIndex ->
                        settingsViewModel.setAppTheme(themeOptions[selectedIndex])
                    })
            }

            SettingAction.OpenLayoutDialog -> {
                val layoutOptions = SettingsDataStore.layoutOptions
                val currentLayoutIndex = layoutOptions.indexOf(selectedLayout)
                showDialog = SettingDialogData(title = "Photo layout",
                    options = layoutOptions,
                    initialSelectedIndex = currentLayoutIndex,
                    onOptionSelected = {selectedIndex ->
                        settingsViewModel.setPhotoLayout(layoutOptions[selectedIndex])
                    })
            }

            SettingAction.OpenDownloadQualityDialog -> {
                val downloadQualityOptions = SettingsDataStore.downloadQualityOptions
                val currentDownloadQualityIndex =
                    downloadQualityOptions.indexOf(selectedDownloadQuality)
                showDialog = SettingDialogData(title = "Download quality",
                    options = downloadQualityOptions,
                    initialSelectedIndex = currentDownloadQualityIndex,
                    onOptionSelected = {selectedIndex ->
                        settingsViewModel.setDownloadQuality(downloadQualityOptions[selectedIndex])
                    })
            }

            SettingAction.ClearCache -> {
                settingsViewModel.clearCache(context.cacheDir)
                cacheSize = settingsViewModel.getCacheSizeInMB(context.cacheDir)
                scope.launch {
                    snackbarHostState.showSnackbar("Cache cleared!")
                }
            }

            SettingAction.NavigateToOtherScreen -> {
                navController.navigate(route = AppInfo)
            }

            else -> {}
        }
    }

    val settingCategories = listOf(
        SettingCategory(
            "General", listOf(
                // Appearance setting with theme selection dialog
                SettingOption(
                    "Appearance",
                    selectedTheme,
                    Icons.Outlined.DarkMode,
                    "Change app theme icon",
                    SettingAction.OpenThemeDialog
                ), SettingOption(
                    // Layout setting with layout selection dialog
                    "Layout",
                    selectedLayout,
                    Icons.Outlined.ViewAgenda,
                    "Change photo layout icon",
                    action = SettingAction.OpenLayoutDialog
                )
            )
        ), SettingCategory(
            "Photos", listOf(
                // Download quality setting with quality selection dialog
                SettingOption(
                    "Download Quality",
                    selectedDownloadQuality,
                    action = SettingAction.OpenDownloadQualityDialog
                ), SettingOption(
                    // Clear cache setting
                    "Clear cache", "Cache size: ${cacheSize}MB", action = SettingAction.ClearCache
                )
            )
        ), SettingCategory(
            "Other", listOf(
                SettingOption(
                    // Privacy setting (placeholder)
                    "Privacy", null, Icons.Outlined.GppMaybe, "Privacy policy icon"
                ), SettingOption(
                    // App info setting with navigation to another screen
                    "App Info",
                    "App version • Developer",
                    Icons.Outlined.Info,
                    "App info icon",
                    action = SettingAction.NavigateToOtherScreen
                )
            )
        )
    )
    // Scaffold for basic app layout
    Scaffold(snackbarHost = {SnackbarHost(hostState = snackbarHostState)}, topBar = {
        // Top app bar with title and back navigation
        LargeTopAppBar(title = {
            Text(text = "Settings")
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back arrow"
                )
            }
        }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        )
        )
    }) {innerPadding ->
        // LazyColumn to display setting categories
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(settingCategories) {category ->
                SettingsCategory(category = category, handleSettingClick)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    // Show option dialog if showDialog state is not null
    showDialog?.let {dialogData ->
        OptionsDialog(title = dialogData.title,
            options = dialogData.options,
            onOptionSelected = {selectedIndex ->
                dialogData.onOptionSelected(selectedIndex)
            },
            initialSelectedIndex = dialogData.initialSelectedIndex,

            onConfirmation = {
                showDialog = null
            },
            onDismissRequest = {showDialog = null} // Close on dismiss
        )
    }
}

/**
 * Displays an options dialog with a list of choices.
 *
 * @param title The title of the dialog.
 * @param options The list of options to display.
 * @param initialSelectedIndex The index of the initially selected option.
 * @param onOptionSelected Callback invoked when an option is selected, providing the index of the selected option.
 * @param onConfirmation Callback invoked when the "Apply" button is clicked.
 * @param onDismissRequest Callback invoked when the dialog is dismissed.
 */
@Composable
fun OptionsDialog(
    title: String,
    options: List<String>,
    initialSelectedIndex: Int?,
    onOptionSelected: (Int) -> Unit,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedIndex by remember {mutableStateOf(initialSelectedIndex)}
    AlertDialog(onDismissRequest = onDismissRequest, title = {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(), // Occupy full width
            textAlign = TextAlign.Start // Align text to the start (left)
        )
    }, text = {
        Column {
            options.forEachIndexed {index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedIndex = index
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedIndex == index,
                        onClick = {selectedIndex = index})
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface // Reduced weight
                    )
                }
            }
        }
    }, confirmButton = {
        TextButton(onClick = {
            selectedIndex?.let {
                onOptionSelected(it)
                onConfirmation()
            }
        }) {
            Text("Apply")
        }
    }, dismissButton = {
        TextButton(onClick = onDismissRequest) {
            Text("Cancel")
        }
    })
}

