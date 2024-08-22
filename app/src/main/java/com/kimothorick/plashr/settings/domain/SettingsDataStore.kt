package com.kimothorick.plashr.settings.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides access to user settings persisted using Jetpack DataStore.
 *
 * This class leverages the Preferences DataStore to storekey-value pairs.
 * It is designed to be injected using a dependency injection framework.
 */
@Singleton
class SettingsDataStore @Inject constructor(val context: Context) {

    companion object {
        // Default options for app theme, photo layout, and download quality
        val themeOptions = listOf("Light", "Dark", "System default")
        val layoutOptions = listOf("List", "Cards", "Staggered grid")
        val downloadQualityOptions = listOf("Raw", "Regular", "Full")

        /**
         * Extension property on `Context` to provide access to the `DataStore<Preferences>` instance.
         */
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    private val APP_THEME = stringPreferencesKey("app_theme")

    /**
     * Flow to observe changes to the app theme setting.
     */
    val appThemeFlow: Flow<String> = context.dataStore.data.map {preferences ->
        preferences[APP_THEME] ?: themeOptions[0]
    }

    /**
     * Sets the app theme setting.
     *
     * @param theme The new app theme to set.
     */
    suspend fun setAppTheme(theme: String) {
        context.dataStore.edit {preferences ->
            preferences[APP_THEME] = theme
        }
    }

    private val PHOTO_LAYOUT = stringPreferencesKey("photo_layout")

    /**
     * Flow to observe changes to the photo layout setting.
     */
    val photoLayoutFlow: Flow<String> = context.dataStore.data.map {preferences ->
        preferences[PHOTO_LAYOUT] ?: layoutOptions[0]
    }

    /**
     * Sets the photo layout setting.
     *
     * @param layout The new photo layout to set.
     */
    suspend fun setPhotoLayout(layout: String) {
        context.dataStore.edit {preferences ->
            preferences[PHOTO_LAYOUT] = layout
        }
    }

    private val DOWNLOAD_QUALITY = stringPreferencesKey("download_quality")

    /**
     * Flow to observe changes to the download quality setting.
     */
    val downloadQualityFlow: Flow<String> = context.dataStore.data.map {preferences ->
        preferences[DOWNLOAD_QUALITY] ?: downloadQualityOptions[0]
    }

    /**
     * Sets the download quality setting.
     *
     * @param quality The new download quality to set.
     */
    suspend fun setDownloadQuality(quality: String) {
        context.dataStore.edit {preferences ->
            preferences[DOWNLOAD_QUALITY] = quality
        }

    }

}