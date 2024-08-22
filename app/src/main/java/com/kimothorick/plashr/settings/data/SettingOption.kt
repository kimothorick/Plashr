package com.kimothorick.plashr.settings.data

/**
 * Represents the possible actions that can be triggered when a setting option is clicked.
 */
sealed class SettingAction {
    /**Action to open a dialog for selecting the app theme. */
    object OpenThemeDialog : SettingAction()

    /** Action to open a dialog for selecting the photo layout. */
    object OpenLayoutDialog : SettingAction()

    /** Action to open a dialog for selecting the download quality. */
    object OpenDownloadQualityDialog : SettingAction()

    /** Action to clear the application cache. */
    object ClearCache : SettingAction()

    /** Action to open the Instagram profile. */
    object OpenInstagram : SettingAction()

    /** Action to navigate to another screen within the app. */
    object NavigateToOtherScreen : SettingAction()
}

/**
 * Data class representing a single setting option in the Settings screen.
 *
 * @param title The title of the setting option.
 * @param description Optional description providing more details about the setting.
 * @param icon Optional icon to be displayed alongside the setting title.
 * @param iconDescription Optional content description for the icon, for accessibility.
 * @param action Optional action to be triggered when the setting option is clicked.
 */
data class SettingOption(
    val title: String,
    val description: String? = null,
    val icon: Any? = null,
    val iconDescription: String? = null,
    val action: SettingAction? = null
)

/**
 * Data class holding information for displaying an options dialog.
 *
 * @param title The title of the dialog.
 * @param options The list of options to display in the dialog.
 * @param initialSelectedIndex Optional index of the initially selected option.
 * @param onOptionSelected Callback invoked when an option is selected, providing the index of the selected option.
 */
data class SettingDialogData(
    val title: String,
    val options: List<String>,
    val initialSelectedIndex: Int? = null,
    val onOptionSelected: (Int) -> Unit
)