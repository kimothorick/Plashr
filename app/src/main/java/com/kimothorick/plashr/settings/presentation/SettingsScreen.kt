package com.kimothorick.plashr.settings.presentation

import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonColors
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.kimothorick.plashr.BuildConfig
import com.kimothorick.plashr.R
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.settings.data.SettingAction
import com.kimothorick.plashr.settings.data.SettingDialogData
import com.kimothorick.plashr.settings.data.SettingOption
import com.kimothorick.plashr.settings.domain.SettingsDataStore
import com.kimothorick.plashr.ui.theme.PlashrTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A Composable function that displays the settings screen of the application.
 * This screen provides options for users to customize their experience, such as
 * changing the theme, layout, clearing cache, and viewing app information.
 *
 * It is structured into several sections: Appearance, Photos, Support & Feedback, and About.
 * User preferences are observed from and updated via the [settingsViewModel].
 * Dialogs and bottom sheets are used for interactive settings like theme selection or viewing developer credits.
 *
 * @param settingsViewModel The ViewModel responsible for handling the business logic and
 * state of the settings. It provides flows for observing settings changes and functions
 * to update them.
 * @param onBackClicked A lambda function to be invoked when the user clicks the back button
 * in the top app bar, typically used for navigating back to the previous screen.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current
    var activeDialog by remember { mutableStateOf<SettingDialogData?>(null) }
    var selectedLayout by remember { mutableStateOf(PhotoLayoutType.LIST) }
    val photoLayoutOptions = PhotoLayoutType.entries.toList()
    val photoLayoutLabels = SettingsDataStore.layoutOptions
    val selectedLayoutIndex = photoLayoutOptions.indexOf(selectedLayout)
    val selectedLayoutLabel = photoLayoutLabels[selectedLayoutIndex]

    var selectedDownloadQuality by remember { mutableStateOf("") }

    var cacheSize by remember { mutableDoubleStateOf(settingsViewModel.getCacheSizeInMB(context.cacheDir)) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val navigator = rememberListDetailPaneScaffoldNavigator<SettingOption>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }
    var selectedTheme by remember { mutableStateOf("") }
    var dynamicThemeEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        settingsViewModel.appTheme.collect { appTheme ->
            selectedTheme = appTheme
        }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.appDynamicTheme.collect {
            dynamicThemeEnabled = it
        }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.photoLayout.collect { photoLayout ->
            selectedLayout = photoLayout
        }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.downloadQuality.collect { downloadQuality ->
            selectedDownloadQuality = downloadQuality
        }
    }
    // Centralized function to handle setting clicks
    val handleSettingClick: (SettingAction) -> Unit = { settingAction ->
        when (settingAction) {
            SettingAction.OpenLayoutDialog -> {
                activeDialog = SettingDialogData(
                    title = context.getString(R.string.photo_layout),
                    options = photoLayoutLabels,
                    initialSelectedIndex = selectedLayoutIndex,
                    onOptionSelected = { selectedIndex ->
                        if (selectedIndex >= 0 && selectedIndex < photoLayoutOptions.size) {
                            val chosenLayoutEnum = photoLayoutOptions[selectedIndex]
                            settingsViewModel.setPhotoLayout(chosenLayoutEnum)
                        }
                    },
                )
            }

            SettingAction.OpenDownloadQualityDialog -> {
                val downloadQualityOptions = SettingsDataStore.downloadQualityOptions
                val currentDownloadQualityIndex = downloadQualityOptions.indexOf(selectedDownloadQuality)
                activeDialog = SettingDialogData(
                    title = context.getString(R.string.download_quality),
                    options = downloadQualityOptions,
                    initialSelectedIndex = currentDownloadQualityIndex,
                    onOptionSelected = { selectedIndex ->
                        settingsViewModel.setDownloadQuality(downloadQualityOptions[selectedIndex])
                    },
                )
            }

            SettingAction.ClearCache -> {
                settingsViewModel.clearCache(context.cacheDir)
                cacheSize = settingsViewModel.getCacheSizeInMB(context.cacheDir)
                scope.launch {
                    snackbarHostState.showSnackbar(context.getString(R.string.cache_cleared))
                }
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back arrow",
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier.padding(scaffoldPadding).nestedScroll(scrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            //region Appearance Settings
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    SubHeader(
                        text = stringResource(R.string.appearance),
                        modifier = Modifier.padding(
                            start = 8.dp,
                        ),
                    )
                    Column(
                        modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        ThemeOptionsSettingsItem(
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 14.dp, vertical = 14.dp),
                            selectedTheme = selectedTheme,
                            dynamicThemeEnabled = dynamicThemeEnabled,
                            setTheme = { themeOption ->
                                settingsViewModel.setAppTheme(themeOption)
                            },
                        )
                        DynamicThemeSettingItem(
                            dynamicThemeEnabled = dynamicThemeEnabled,
                            setDynamicTheme = { dynamicThemeEnabled ->
                                settingsViewModel.setDynamicThemePreference(dynamicThemeEnabled)
                            },
                        )
                    }
                }
            }
            //endregion

            //region Photos Settings
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    SubHeader(
                        text = stringResource(R.string.photos),
                        modifier = Modifier.padding(
                            start = 8.dp,
                        ),
                    )

                    Column(
                        modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        LayoutSettingItem(
                            selectedLayout = selectedLayoutLabel,
                            onLayoutSelected = {
                                handleSettingClick(SettingAction.OpenLayoutDialog)
                            },
                        )
                        ClearCacheSettingItem(
                            cacheSize = cacheSize,
                            onClearCache = {
                                handleSettingClick(SettingAction.ClearCache)
                            },
                        )
                    }
                }
            }
            //endregion

            //region Support & Feedback Settings
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    SubHeader(
                        text = stringResource(R.string.support_feedback),
                        modifier = Modifier.padding(
                            start = 8.dp,
                        ),
                    )
                    Column(
                        modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        FeedbackSettingItem(
                            onFeedback = {
                                val intent = Intent(Intent.ACTION_VIEW, Constants.PLASHR_GITHUB_ISSUES_URL.toUri())
                                context.startActivity(intent)
                            },
                        )
                        RateAppSettingItem(
                            onRateApp = {},
                        )
                    }
                }
            }
            //endregion

            //region About Plashr Settings
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    SubHeader(
                        text = stringResource(R.string.about_plashr),
                        modifier = Modifier.padding(
                            start = 8.dp,
                        ),
                    )
                    Column(
                        modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        PrivacyPolicySettingItem(
                            onPrivacyPolicy = {
                                val intent = Intent(Intent.ACTION_VIEW, Constants.PLASHR_GITHUB_PRIVACY_POLICY_URL.toUri())
                                context.startActivity(intent)
                            },
                        )
                        AppVersionSettingItem()
                        DeveloperCreditsSettingItem(
                            onDeveloperCredits = {
                                showBottomSheet = true
                            },
                        )
                    }
                }
            }
            //endregion
        }
        if (showBottomSheet) {
            AboutDeveloperBottomSheet(
                sheetState = sheetState,
                coroutineScope = coroutineScope,
                onDismiss = { showBottomSheet = false },
            )
        }
    }

    // Show option dialog if showDialog state is not null
    activeDialog?.let { activeDialogData ->
        OptionsDialog(
            title = activeDialogData.title,
            options = activeDialogData.options,
            onOptionSelected = { selectedIndex ->
                activeDialogData.onOptionSelected(selectedIndex)
            },
            initialSelectedIndex = activeDialogData.initialSelectedIndex,
            onConfirmation = {
                activeDialog = null
            },
            onDismissRequest = { activeDialog = null },
        )
    }
}

/**
 * A composable function that displays a dialog with a list of radio button options.
 * It allows the user to select one option from the list and confirm their choice.
 *
 * @param title The title of the dialog.
 * @param options A list of strings representing the options to be displayed.
 * @param initialSelectedIndex The index of the initially selected option. Can be null if no option is pre-selected.
 * @param onOptionSelected A callback that is invoked with the index of the selected option when the user confirms their choice.
 * @param onConfirmation A callback that is invoked when the confirmation button is clicked.
 * @param onDismissRequest A callback that is invoked when the dialog is dismissed, either by clicking the dismiss button or by an external event.
 * @param modifier The [Modifier] to be applied to this composable.
 */
@Composable
fun OptionsDialog(
    title: String,
    options: List<String>,
    initialSelectedIndex: Int?,
    onOptionSelected: (Int) -> Unit,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedIndex by remember { mutableStateOf(initialSelectedIndex) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
        },
        text = {
            Column {
                options.forEachIndexed { index, option ->
                    Row(
                        modifier = modifier.fillMaxWidth().clickable {
                            selectedIndex = index
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedIndex?.let {
                        onOptionSelected(it)
                        onConfirmation()
                    }
                },
            ) {
                Text(stringResource(R.string.apply))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

/**
 * A composable that provides options for the user to select the app's theme.
 * It displays a group of toggle buttons for "Auto", "Light", and "Dark" theme settings.
 *
 * @param modifier The modifier to be applied to the `Column` that contains the setting item.
 * @param selectedTheme The currently active theme option's string representation (e.g., "Auto", "Light", "Dark").
 * @param dynamicThemeEnabled A boolean indicating if dynamic theming is currently active, used to adjust toggle button colors.
 * @param setTheme A lambda function that is invoked with the selected theme's string representation when a user clicks a theme option.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ThemeOptionsSettingsItem(
    modifier: Modifier = Modifier,
    selectedTheme: String,
    dynamicThemeEnabled: Boolean,
    setTheme: (String) -> Unit,
) {
    val themeOptions = SettingsDataStore.themeOptions
    val themeOptionsLabels = listOf(
        stringResource(R.string.auto),
        stringResource(R.string.light),
        stringResource(R.string.dark),
    )
    val selectedIndex = themeOptions.indexOf(selectedTheme)

    val toggleButtonColors: ToggleButtonColors = when {
        (isSystemInDarkTheme() && !dynamicThemeEnabled) -> {
            ToggleButtonDefaults.toggleButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        else -> {
            ToggleButtonDefaults.toggleButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.theme),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth().border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.medium,
            ).height(56.dp).padding(all = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            themeOptions.forEachIndexed { index, _ ->
                ToggleButton(
                    checked = selectedIndex == index,
                    onCheckedChange = {
                        setTheme(themeOptions[index])
                    },
                    modifier = Modifier.weight(1f).fillMaxHeight().semantics { role = Role.RadioButton },
                    shapes = ToggleButtonDefaults.shapes(MaterialTheme.shapes.medium),
                    border = null,
                    colors = toggleButtonColors,
                ) {
                    Text(themeOptionsLabels[index])
                }
            }
        }
    }
}

/**
 * A composable that provides a setting item to enable or disable dynamic theming (Material You).
 * It displays a descriptive text and a switch to toggle the feature.
 *
 * @param dynamicThemeEnabled A boolean indicating whether dynamic theming is currently enabled.
 * @param setDynamicTheme A lambda function that is invoked with a boolean value when the user
 * toggles the switch or clicks the item. `true` to enable, `false` to disable.
 */
@Composable
fun DynamicThemeSettingItem(
    dynamicThemeEnabled: Boolean,
    setDynamicTheme: (Boolean) -> Unit,
) {
    SettingsListItem(
        headlineContentText = stringResource(R.string.dynamic_color),
        supportingContentText = stringResource(R.string.enable_dynamic_colors_description),
        trailingContent = {
            Column {
                Switch(
                    checked = dynamicThemeEnabled,
                    onCheckedChange = {
                        if (it) {
                            setDynamicTheme(true)
                        } else {
                            setDynamicTheme(false)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                    ),
                )
            }
        },
        onClick = {
            setDynamicTheme(!dynamicThemeEnabled)
        },
    )
}

/**
 * A composable that displays a setting item for selecting the photo layout.
 * It shows the currently selected layout and triggers an action when clicked,
 * typically opening a dialog for layout selection.
 *
 * @param selectedLayout A string representing the name of the currently selected layout.
 * @param onLayoutSelected A lambda function to be invoked when the user clicks on this setting item.
 */
@Composable
fun LayoutSettingItem(
    selectedLayout: String,
    onLayoutSelected: () -> Unit,
) {
    SettingsListItem(
        headlineContentText = stringResource(R.string.layout),
        supportingContentText = selectedLayout,
        onClick = { onLayoutSelected() },
    )
}

/**
 * A composable that displays a setting item for clearing the application's cache.
 * It shows the current cache size and triggers a callback when clicked.
 *
 * @param cacheSize The current size of the cache in megabytes (MB).
 * @param onClearCache A lambda function to be invoked when the user clicks this setting item.
 */
@Composable
fun ClearCacheSettingItem(
    cacheSize: Double,
    onClearCache: () -> Unit,
) {
    SettingsListItem(
        headlineContentText = stringResource(R.string.clear_cache),
        supportingContentText = stringResource(R.string.cache_size_mb, cacheSize),
        onClick = { onClearCache() },
    )
}

/**
 * A composable that displays a setting item for providing feedback.
 * It shows descriptive text and triggers a callback when clicked,
 * which should handle the action of collecting user feedback.
 *
 * @param onFeedback A lambda function to be invoked when the user clicks this setting item.
 */
@Composable
fun FeedbackSettingItem(
    onFeedback: () -> Unit,
) {
    SettingsListItem(
        headlineContentText = stringResource(R.string.feedback),
        supportingContentText = stringResource(R.string.feedback_description),
        onClick = { onFeedback() },
    )
}

/**
 * A composable that displays a setting item for rating the application.
 * It shows descriptive text and triggers a callback when clicked,
 * which should handle navigating the user to the app's store page.
 *
 * @param onRateApp A lambda function to be invoked when the user clicks this setting item.
 */
@Composable
fun RateAppSettingItem(
    onRateApp: () -> Unit,
) {
    SettingsListItem(
        headlineContentText = stringResource(R.string.rate_app),
        supportingContentText = stringResource(R.string.rate_app_description),
        onClick = { onRateApp() },
    )
}

/**
 * A composable that displays a setting item for viewing the application's privacy policy.
 * It shows the title "Privacy Policy" and triggers a callback when clicked,
 * which should handle navigating the user to the privacy policy screen or webpage.
 *
 * @param onPrivacyPolicy A lambda function to be invoked when the user clicks this setting item.
 */
@Composable
fun PrivacyPolicySettingItem(
    onPrivacyPolicy: () -> Unit,
) {
    SettingsListItem(
        modifier = Modifier.padding(vertical = 12.dp),
        headlineContentText = stringResource(R.string.privacy_policy),
        supportingContentText = null,
        onClick = { onPrivacyPolicy() },
    )
}

/**
 * A composable that displays a setting item showing the application's current version.
 * The version name is retrieved from the `BuildConfig`. This item is non-interactive.
 */
@Composable
fun AppVersionSettingItem() {
    SettingsListItem(
        headlineContentText = stringResource(R.string.app_version),
        supportingContentText = "v ${BuildConfig.VERSION_NAME}",
    )
}

/**
 * A composable that displays a setting item for viewing developer credits.
 * It shows descriptive text and the developer's profile picture as a trailing icon.
 * Clicking this item triggers a callback, which should handle showing more
 * detailed information about the developer, such as in a bottom sheet or a new screen.
 *
 * @param onDeveloperCredits A lambda function to be invoked when the user clicks this setting item.
 */
@Composable
fun DeveloperCreditsSettingItem(
    onDeveloperCredits: () -> Unit,
) {
    SettingsListItem(
        headlineContentText = stringResource(R.string.developer_credits),
        supportingContentText = stringResource(R.string.developer_credits_description),
        trailingContent = {
            AsyncImage(
                model = Constants.Profile.RICK_PROFILE_PICTURE_URL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.requiredSize(36.dp).clip(MaterialTheme.shapes.extraLarge),
            )
        },
        onClick = { onDeveloperCredits() },
    )
}

/**
 * A composable that displays a modal bottom sheet with information about the developer.
 * This sheet is designed to be shown when the user interacts with the "Developer Credits" setting.
 * It uses a [ModalBottomSheet] to present the content, which is defined in
 * [AboutDeveloperBottomSheetContent]. The sheet can be dismissed by swiping down, pressing the
 * back button, or clicking outside the sheet.
 *
 * @param onDismiss A lambda function that is invoked when the bottom sheet is requested to be dismissed.
 *                  This should typically handle updating the state that controls the visibility of the sheet.
 * @param coroutineScope A [CoroutineScope] used to launch the coroutine for hiding the sheet,
 *                       ensuring smooth animations.
 * @param sheetState The [SheetState] that controls the state of the [ModalBottomSheet].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperBottomSheet(
    onDismiss: () -> Unit,
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
) {
    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                onDismiss()
            }
        },
        sheetState = sheetState,
        modifier = Modifier.padding(end = 8.dp, start = 8.dp).windowInsetsPadding(WindowInsets.systemBars).background(Color.Transparent),
        shape = MaterialTheme.shapes.extraLarge,
        sheetMaxWidth = 420.dp,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
            shouldDismissOnClickOutside = true,
        ),
    ) {
        AboutDeveloperBottomSheetContent()
    }
}

/**
 * A composable function that defines the content displayed inside the "About Developer"
 * modal bottom sheet. It includes the developer's profile picture, name, occupation,
 * a short biography, and a section with buttons linking to their social media profiles.
 *
 * This content is vertically scrollable to accommodate various screen sizes and
 * content lengths.
 */
@Composable
fun AboutDeveloperBottomSheetContent() {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 16.dp).padding(bottom = 16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            AsyncImage(
                model = Constants.Profile.RICK_PROFILE_PICTURE_URL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.requiredSize(64.dp).clip(CircleShape),
            )
            Text(Constants.Profile.RICK_NAME, style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(R.string.rick_occupation), style = MaterialTheme.typography.titleSmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.rick_bio), style = MaterialTheme.typography.bodyMedium)
        Divider()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Socials", style = MaterialTheme.typography.titleMedium)
            SocialsButtonsSection()
        }
    }
}

/**
 * A composable function that displays a single social media link button.
 * The button consists of an icon and a text label for the platform name.
 * When clicked, it opens the corresponding social media URL in an external browser.
 *
 * @param socialPlatform A [SocialPlatform] data object containing the platform's name and URL.
 * @param icon The [ImageVector] to be displayed as the button's icon.
 */
@Composable
fun SocialsButton(
    socialPlatform: SocialPlatform,
    icon: ImageVector,
) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            modifier = Modifier.clip(MaterialTheme.shapes.extraLarge).heightIn(36.dp).background(MaterialTheme.colorScheme.surfaceVariant),
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, socialPlatform.url.toUri())
                context.startActivity(intent)
            },
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = socialPlatform.platform, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * A composable that displays a horizontal row of social media buttons.
 * This section is typically used to link to various social profiles, such as in a developer
 * credits or "About" screen. It uses a `LazyRow` to efficiently display the buttons.
 *
 * @param modifier The [Modifier] to be applied to the `LazyRow` that contains the buttons.
 */
@Composable
fun SocialsButtonsSection(
    modifier: Modifier = Modifier,
) {
    val list = listOf(
        SocialPlatform(
            platform = stringResource(R.string.github),
            icon = R.drawable.ic_github,
            url = Constants.Profile.RICK_GITHUB,
        ),
        SocialPlatform(
            platform = stringResource(R.string.instagram),
            icon = R.drawable.ic_instagram,
            url = Constants.Profile.RICK_INSTAGRAM,
        ),
        SocialPlatform(
            platform = stringResource(R.string.threads),
            icon = R.drawable.ic_threads,
            url = Constants.Profile.RICK_THREADS,
        ),
        SocialPlatform(
            platform = stringResource(R.string.bluesky),
            icon = R.drawable.ic_bluesky,
            url = Constants.Profile.RICK_BLUESKY,
        ),
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier.fillMaxWidth()) {
        items(list) { socialPlatform ->
            SocialsButton(
                socialPlatform = socialPlatform,
                icon = ImageVector.vectorResource(id = socialPlatform.icon),
            )
        }
    }
}

/**
 * A composable that displays a styled sub-header text, typically used to label a
 * group of settings. It uses the `titleMedium` typography style and a specific
 * color from the Material Theme to distinguish it from other text elements.
 *
 * @param text The string to be displayed as the sub-header.
 * @param modifier The [Modifier] to be applied to the text component.
 */
@Composable
internal fun SubHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

/**
 * A private composable that displays a horizontal line used as a separator.
 * It is a styled wrapper around [HorizontalDivider] with default padding.
 *
 * @param horizontalInsets The horizontal padding to apply to the divider, allowing it to be inset from the screen edges. Defaults to `0.dp`.
 */
@Composable
private fun Divider(
    horizontalInsets: Dp = 0.dp,
) {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = horizontalInsets),
        color = MaterialTheme.colorScheme.surfaceVariant,
    )
}

/**
 * A versatile and reusable composable that represents a single item in a settings list.
 * It follows a common list item pattern with optional leading and trailing content,
 * a main headline, and optional supporting text. The entire item is clickable.
 *
 * This composable is designed to be a building block for creating consistent-looking
 * settings screens.
 *
 * @param modifier The [Modifier] to be applied to the root `Row` of the list item.
 * @param headlineContentText The primary text to be displayed in the list item.
 * @param supportingContentText Optional secondary text displayed below the headline. If null, this part is omitted.
 * @param leadingContent An optional composable lambda for content to be displayed at the start of the item (e.g., an icon).
 * @param trailingContent An optional composable lambda for content to be displayed at the end of the item (e.g., a switch or a chevron).
 * @param onClick A lambda function to be invoked when the list item is clicked.
 * @param itemPadding The padding to be applied inside the clickable area of the item, around its content.
 */
@Composable
internal fun SettingsListItem(
    modifier: Modifier = Modifier,
    headlineContentText: String,
    supportingContentText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    itemPadding: PaddingValues = PaddingValues(16.dp, 12.dp, 12.dp, 12.dp),
) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.extraSmall).background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                onClick()
            }.padding(itemPadding).then(
                modifier,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //region Leading Content
        if (leadingContent != null) {
            Box(modifier = Modifier.padding(end = if (headlineContentText.isNotEmpty() || supportingContentText != null) 16.dp else 0.dp)) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    leadingContent.invoke()
                }
            }
        }
        //endregion

        //region Main Content (Headline and Supporting)
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = headlineContentText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (supportingContentText != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = supportingContentText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        //endregion

        //region Trailing Content
        if (trailingContent != null) {
            Box(modifier = Modifier.padding(start = 16.dp)) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    trailingContent()
                }
            }
        }
        //endregion
    }
}

/**
 * Represents a social media platform with its associated information.
 * This data class is used to model the details required to display a link
 * to a social media profile, such as in the "About Developer" section.
 *
 * @property platform The name of the social media platform (e.g., "GitHub", "Instagram").
 * @property icon The resource ID of the drawable icon for the platform.
 * @property url The URL string pointing to the social media profile or page.
 */
data class SocialPlatform(
    val platform: String,
    val icon: Int,
    val url: String,
)

//region Previews
@Preview(showBackground = true, name = "SettingsListItem Default")
@Composable
fun SettingsListItemPreview() {
    PlashrTheme {
        Surface {
            Column {
                SettingsListItem(
                    headlineContentText = "Notifications",
                    supportingContentText = "Manage your notification preferences",
                )
                Divider()
                SettingsListItem(
                    headlineContentText = "Privacy",
                    supportingContentText = "View our privacy policy",
                    leadingContent = {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
                    },
                )
                Divider()
                SettingsListItem(
                    headlineContentText = "Dark Mode",
                    supportingContentText = "Enable or disable dark mode",
                    trailingContent = {
                        Switch(checked = true, onCheckedChange = {})
                    },
                )
                Divider()
                SettingsListItem(
                    headlineContentText = "Account",
                    leadingContent = {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = "Info Icon")
                    },
                    trailingContent = {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = "Check Icon")
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SocialsButtonPreview() {
    PlashrTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            SocialsButtonsSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    PlashrTheme(dynamicColor = true) {
        Surface {
            Scaffold(
                snackbarHost = { },
                topBar = {
                    MediumFlexibleTopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.settings),
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = "Back arrow",
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                            titleContentColor = MaterialTheme.colorScheme.onBackground,
                            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                    )
                },
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier.padding(innerPadding).nestedScroll(scrollBehavior.nestedScrollConnection),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Appearance",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                ),
                            )
                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                ThemeOptionsSettingsItem(
                                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 14.dp, vertical = 14.dp),
                                    selectedTheme = "Dark",
                                    dynamicThemeEnabled = true,
                                    setTheme = { themeOption ->
                                    },
                                )
                                DynamicThemeSettingItem(
                                    dynamicThemeEnabled = true,
                                    setDynamicTheme = { dynamicThemeEnabled ->
                                    },
                                )
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            SubHeader(
                                text = stringResource(R.string.photos),
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                ),
                            )

                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                LayoutSettingItem(
                                    selectedLayout = "List",
                                    onLayoutSelected = {},
                                )
                                ClearCacheSettingItem(
                                    cacheSize = 23.62,
                                    onClearCache = {},
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            SubHeader(
                                text = stringResource(R.string.support_feedback),
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                ),
                            )
                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                FeedbackSettingItem(
                                    onFeedback = {},
                                )
                                RateAppSettingItem(
                                    onRateApp = {},
                                )
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            SubHeader(
                                text = stringResource(R.string.about_plashr),
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                ),
                            )
                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                PrivacyPolicySettingItem(
                                    onPrivacyPolicy = {},
                                )
                                AppVersionSettingItem()
                                DeveloperCreditsSettingItem(
                                    onDeveloperCredits = {},
                                )
                            }
                        }
                    }
                    item {}

                    item {}

                    item {}
                }
            }
        }
    }
}
// endregion
