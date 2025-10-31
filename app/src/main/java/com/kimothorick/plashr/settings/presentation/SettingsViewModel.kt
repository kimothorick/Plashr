package com.kimothorick.plashr.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.settings.domain.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import javax.inject.Inject

/**
 * Represents the different layout options for displaying photos.
 * These types are used in the settings to allow the user to customize
 * how photo lists are rendered throughout the application.
 */
enum class PhotoLayoutType {
    LIST,
    CARDS,
    STAGGERED_GRID,
}

/**
 * ViewModel for the settings screen.
 *
 * This ViewModel is responsible for managing and exposing user preferences
 * from the [SettingsDataStore]. It provides flows to observe changes in settings
 * and methods to update them. It also includes utility functions for cache management.
 *
 * @property settingsDataStore The data store for persisting user settings.
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val settingsDataStore: SettingsDataStore,
        private val crashlytics: FirebaseCrashlytics,
    ) : ViewModel() {
        val appTheme = settingsDataStore.appThemeFlow

        /**
         * Sets the application's theme and persists it to the data store.
         *
         * This function launches a coroutine in the `viewModelScope` to asynchronously
         * update the theme preference in the `SettingsDataStore`.
         *
         * @param theme The name of the theme to set (e.g., "Light", "Dark", "System").
         */
        fun setAppTheme(
            theme: String,
        ) {
            viewModelScope.launch {
                settingsDataStore.setAppTheme(theme)
            }
        }

        val appDynamicTheme = settingsDataStore.appDynamicThemeFlow

        /**
         * Sets the user's preference for using a dynamic theme.
         *
         * This function launches a coroutine in the `viewModelScope` to persist
         * the user's choice. When `useDynamicTheme` is true, the app will use
         * Material You dynamic theming on supported devices (Android 12+).
         * When false, it will fall back to the default app theme.
         *
         * @param useDynamicTheme A boolean indicating whether to enable dynamic theming.
         */
        fun setDynamicThemePreference(
            useDynamicTheme: Boolean,
        ) {
            viewModelScope.launch {
                settingsDataStore.setDynamicThemePreference(useDynamicTheme)
            }
        }

        val photoLayout: StateFlow<PhotoLayoutType> =
            settingsDataStore.photoLayoutFlow
                .map { layoutString ->
                    PhotoLayoutType.entries.firstOrNull { it.name == layoutString } ?: PhotoLayoutType.LIST
                }
                .stateIn(viewModelScope, SharingStarted.Eagerly, PhotoLayoutType.LIST)

        /**
         * Sets the layout for displaying photos in the app.
         *
         * This function persists the user's layout choice by calling the [SettingsDataStore].
         * The change is performed asynchronously within a coroutine launched in the `viewModelScope`.
         *
         * @param layout The [PhotoLayoutType] to be set, which determines how photo lists are rendered.
         */
        fun setPhotoLayout(
            layout: PhotoLayoutType,
        ) {
            viewModelScope.launch {
                settingsDataStore.setPhotoLayout(layout.name)
            }
        }

        val downloadQuality = settingsDataStore.downloadQualityFlow

        /**
         * Sets the preferred download quality for photos.
         *
         * This function asynchronously saves the user's selected download quality preference
         * to the `SettingsDataStore`.
         *
         * @param quality A string representing the chosen quality (e.g., "RAW", "FULL", "REGULAR").
         */
        fun setDownloadQuality(
            quality: String,
        ) {
            viewModelScope.launch {
                settingsDataStore.setDownloadQuality(quality)
            }
        }

        /**
         * Recursively calculates the total size of a directory or a single file in bytes.
         *
         * If the provided [File] is a directory, it iterates through all its contents
         * (files and subdirectories) and sums up their sizes. If it's a file, it returns
         * the file's length.
         *
         * @param dir The file or directory to measure.
         * @return The total size in bytes.
         */
        private fun getCacheSize(
            dir: File,
        ): Long {
            var sizeInBytes = 0L
            try {
                if (dir.isDirectory) {
                    for (file in dir.listFiles()!!) {
                        sizeInBytes +=
                            if (file.isFile) {
                                file.length()
                            } else {
                                getCacheSize(file)
                            }
                    }
                } else if (dir.isFile) {
                    sizeInBytes = dir.length()
                }
            } catch (e: NullPointerException) {
                crashlytics.recordException(e)
            }
            return sizeInBytes
        }

        /**
         * Calculates the size of a given directory or file in megabytes (MB),
         * formatted to two decimal places.
         *
         * This function first computes the total size in bytes by recursively traversing
         * the directory structure if the provided [File] is a directory. It then converts
         * the byte count to megabytes and formats the result.
         *
         * @param dir The directory or file for which to calculate the size.
         * @return The size in megabytes, as a [Double] rounded to two decimal places.
         */
        fun getCacheSizeInMB(
            dir: File,
        ): Double {
            val sizeInBytes = getCacheSize(dir)
            val sizeInMegabytes = sizeInBytes.toDouble() / (1024 * 1024)
            return String.format(Locale.getDefault(), "%.2f", sizeInMegabytes).toDouble()
        }

        /**
         * Recursively deletes a directory and all its contents.
         *
         * This function traverses a directory tree and deletes all files and subdirectories.
         * If the provided `File` object is a file, it will be deleted directly. If it's a
         * directory, the function will first attempt to clear all its children before
         * deleting the directory itself.
         *
         * @param dir The file or directory to delete.
         * @return `true` if the file or directory was successfully deleted, `false` otherwise.
         *         If a directory is being deleted, this returns `false` if any of its
         *         children could not be deleted.
         */
        fun clearCache(
            dir: File,
        ): Boolean {
            try {
                if (dir.isDirectory) {
                    for (child in dir.listFiles()!!) {
                        if (!clearCache(child)) {
                            return false
                        }
                    }
                }
                return dir.delete()
            } catch (e: Exception) {
                crashlytics.recordException(e)
                return false
            }
        }
    }
