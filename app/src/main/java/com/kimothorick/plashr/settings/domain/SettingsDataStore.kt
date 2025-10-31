package com.kimothorick.plashr.settings.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kimothorick.plashr.settings.domain.SettingsDataStore.Companion.downloadQualityOptions
import com.kimothorick.plashr.settings.domain.SettingsDataStore.Companion.layoutOptions
import com.kimothorick.plashr.settings.domain.SettingsDataStore.Companion.themeOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages application-wide user settings using Jetpack DataStore.
 *
 * This class provides a centralized way to access and modify user preferences,
 * such as the app's theme, dynamic color settings, photo layout, and download quality.
 * It exposes these settings as [Flow]s, allowing UI components to reactively update
 * when a preference changes. It also provides suspend functions to persist new setting values.
 *
 * It is implemented as a Singleton and injected via Hilt, ensuring a single instance
 * throughout the application's lifecycle.
 *
 * @property context The application context, injected by Hilt, used to initialize the DataStore.
 */
@Singleton
class SettingsDataStore
    @Inject
    constructor(
        @ApplicationContext val context: Context,
    ) {
        companion object {
            private const val SETTINGS_NAME = "settings"

            val themeOptions = listOf("System default", "Light", "Dark")
            val layoutOptions = listOf("List", "Cards", "Staggered grid")
            val downloadQualityOptions = listOf("Raw", "Regular", "Full")

            val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_NAME)
        }

        private val appThemePrefKey = stringPreferencesKey("app_theme")

        val appThemeFlow: Flow<String> =
            context.dataStore.data.map { preferences ->
                preferences[appThemePrefKey] ?: themeOptions[2]
            }.distinctUntilChanged()

        /**
         * Sets and persists the user's chosen application theme.
         *
         * This is a suspending function that safely updates the DataStore preference
         * for the app theme. It should be called from a coroutine scope.
         *
         * @param theme The theme to set, which should be one of the values from [themeOptions].
         */
        suspend fun setAppTheme(
            theme: String,
        ) {
            context.dataStore.edit { preferences ->
                preferences[appThemePrefKey] = theme
            }
        }

        private val dynamicThemePrefKey = booleanPreferencesKey("dynamic_theme")

        val appDynamicThemeFlow: Flow<Boolean> =
            context.dataStore.data.map { preferences ->
                preferences[dynamicThemePrefKey] ?: false
            }.distinctUntilChanged()

        /**
         * Persists the user's preference for using a dynamic theme (Material You).
         *
         * This function updates the DataStore with the given boolean value, which determines
         * whether the app should use colors derived from the user's wallpaper.
         *
         * @param useDynamicTheme `true` to enable dynamic theming, `false` to disable it.
         */
        suspend fun setDynamicThemePreference(
            useDynamicTheme: Boolean,
        ) {
            context.dataStore.edit { preferences ->
                preferences[dynamicThemePrefKey] = useDynamicTheme
            }
        }

        private val photoLayoutPrefKey = stringPreferencesKey("photo_layout")

        val photoLayoutFlow: Flow<String> =
            context.dataStore.data.map { preferences ->
                preferences[photoLayoutPrefKey] ?: layoutOptions[0]
            }.distinctUntilChanged()

        /**
         * Persists the user's preferred photo layout to the DataStore.
         *
         * @param layout The layout option to save, expected to be one of the values
         * from [layoutOptions] (e.g., "List", "Cards", "Staggered grid").
         */
        suspend fun setPhotoLayout(
            layout: String,
        ) {
            context.dataStore.edit { preferences ->
                preferences[photoLayoutPrefKey] = layout
            }
        }

        private val downloadQualityPrefKey = stringPreferencesKey("download_quality")

        val downloadQualityFlow: Flow<String> =
            context.dataStore.data.map { preferences ->
                preferences[downloadQualityPrefKey] ?: downloadQualityOptions[0]
            }.distinctUntilChanged()

        /**
         * Persists the user's preferred download quality to the DataStore.
         *
         * @param quality The selected download quality string. Should be one of the values
         * from [downloadQualityOptions].
         */
        suspend fun setDownloadQuality(
            quality: String,
        ) {
            context.dataStore.edit { preferences ->
                preferences[downloadQualityPrefKey] = quality
            }
        }
    }
