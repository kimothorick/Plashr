package com.kimothorick.plashr.settings.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for accessing and modifying user settings.
 *
 * This ViewModel utilizes [SettingsDataStore] to persist the data.
 *It is annotated with `@HiltViewModel` for dependency injection with Hilt.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {
    /**
     * Flow representing the current app theme setting.
     */
    val appTheme = settingsDataStore.appThemeFlow

    /**
     * Sets the app theme setting.
     *
     * @param theme The new app theme to set.
     */
    fun setAppTheme(theme: String) {
        viewModelScope.launch {
            settingsDataStore.setAppTheme(theme)
        }
    }

    /**
     * Flow representing the current photo layout setting.
     */
    val photoLayout = settingsDataStore.photoLayoutFlow

    /**
     * Sets the photo layout setting.
     *
     * @param layout The new photo layout to set.
     */
    fun setPhotoLayout(layout: String) {
        viewModelScope.launch {
            settingsDataStore.setPhotoLayout(layout)
        }
    }

    /**
     * Flow representing the current download quality setting.
     */
    val downloadQuality = settingsDataStore.downloadQualityFlow

    /**
     * Sets the download quality setting.
     *
     * @param quality The new download quality to set.
     */
    fun setDownloadQuality(quality: String) {
        viewModelScope.launch {
            settingsDataStore.setDownloadQuality(quality)
        }
    }

}