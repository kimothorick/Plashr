package com.kimothorick.plashr.settings.data

/**
 * Represents a category of settings in the application.
 *
 * @param title The title of the settings category (optional).
 * @param settings A list of [SettingOption] objects representing the individual settings within this category.
 */
data class SettingCategory(
    val title: String? = null,
    val settings: List<SettingOption>
)