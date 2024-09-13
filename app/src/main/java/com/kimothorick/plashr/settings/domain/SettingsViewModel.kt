package com.kimothorick.plashr.settings.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
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

    /**
     * Calculates the size of a directory (and its contents) in bytes.
     *
     * This function recursively traverses the directory structureand sums up the sizes of all files within it.
     *
     * @param dir The directory to calculate the size of.
     * @return The total size of the directory in bytes.
     */
    private fun getCacheSize(dir: File): Long {
        var size = 0L
        if (dir.isDirectory) {
            for (file in dir.listFiles()!!) {
                size +=if (file.isFile) {
                    file.length()
                } else {
                    getCacheSize(file)
                }
            }
        } else if (dir.isFile) {
            size = dir.length()
        }
        return size
    }

    /**
     * Calculates the size of a directory (and its contents) in megabytes, rounded to two decimal places.
     *
     * This function uses the device's default locale for number formatting.
     *
     * @param dir The directory to calculate the size of.
     * @return The total size of the directory in megabytes.
     */
    fun getCacheSizeInMB(dir: File): Double {
        val sizeInBytes = getCacheSize(dir)
        val sizeInMB = sizeInBytes.toDouble() / (1024 * 1024)
        return String.format(Locale.getDefault(), "%.2f", sizeInMB).toDouble()
    }

    /**
     * Clears the contents of a directory.
     *
     * This function recursively deletes all files and subdirectories within the specified directory.
     * @param dir The directory to clear.
     * @return `true` if the directory was cleared successfully, `false` otherwise.
     */
    fun clearCache(dir: File): Boolean {
        if (dir.isDirectory) {for (child in dir.listFiles()!!) {
            if (!clearCache(child)) {
                return false
            }
        }
        }
        return dir.delete()
    }
}