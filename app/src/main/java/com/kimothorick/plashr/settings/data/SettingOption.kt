package com.kimothorick.plashr.settings.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents the different actions that can be triggered from a setting option in the UI.
 * This sealed class is used to model the various events that can occur when a user interacts
 * with a setting, allowing the UI to react accordingly (e.g., opening a dialog, navigating,
 * or performing a background task).
 *
 * It is `Parcelable` to allow it to be passed between different Android components, such as
 * through navigation arguments.
 */
@Parcelize
sealed class SettingAction : Parcelable {
    /**
     * Represents an action to open the theme selection dialog.
     * This allows the user to choose between light, dark, or system default themes.
     */
    @Parcelize
    data object OpenThemeDialog : SettingAction()

    /**
     * Represents an action to open the layout selection dialog. This dialog allows the user
     * to choose between different layout options for displaying content, such as list or grid view.
     */
    @Parcelize
    data object OpenLayoutDialog : SettingAction()

    /**
     * Represents an action to open the dialog for selecting the download quality of images.
     */
    @Parcelize
    data object OpenDownloadQualityDialog : SettingAction()

    /**
     * Represents the action to clear the application's cache.
     * This action is typically triggered from a settings screen option.
     */
    @Parcelize
    data object ClearCache : SettingAction()

    /**
     * Represents an action to open the developer's Instagram profile.
     */
    @Parcelize
    data object OpenInstagram : SettingAction()

    /**
     * Represents an action to navigate to a different screen within the app,
     * typically used for sections like "About" or "Licenses".
     */
    @Parcelize
    data object NavigateToOtherScreen : SettingAction()
}

/**
 * Represents a single configurable option within a settings screen.
 *
 * This data class holds all the necessary information to display a setting item,
 * including its visual representation and the action to be performed when it is interacted with.
 * It is `Parcelable` to allow it to be passed between different components, such as activities or fragments.
 *
 * @property title The primary text displayed for the setting option. This is a mandatory field.
 * @property description Optional secondary text displayed below the title, providing more context or the current value.
 * @property icon Optional drawable resource ID for an icon to be displayed alongside the text.
 * @property iconDescription Optional content description for the icon, used for accessibility purposes.
 * @property action The [SettingAction] to be triggered when the user interacts with this option.
 *                  A null value indicates that the option is purely informational and not interactive.
 */
@Parcelize
data class SettingOption(
    val title: String,
    val description: String? = null,
    val icon: Int? = null,
    val iconDescription: String? = null,
    val action: SettingAction? = null,
) : Parcelable

/**
 * Represents the data required to display a single-choice selection dialog.
 * This is typically used for settings where the user needs to choose one option from a list.
 *
 * @property title The title to be displayed at the top of the dialog.
 * @property options A list of string options for the user to choose from.
 * @property initialSelectedIndex The index of the option that should be pre-selected when the dialog is first displayed.
 *                                If null, no option is pre-selected.
 * @property onOptionSelected A callback function that is invoked when the user selects an option.
 *                            The function receives the index of the selected option.
 */
data class SettingDialogData(
    val title: String,
    val options: List<String>,
    val initialSelectedIndex: Int? = null,
    val onOptionSelected: (Int) -> Unit,
)
