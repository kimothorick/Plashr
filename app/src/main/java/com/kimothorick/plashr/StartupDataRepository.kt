package com.kimothorick.plashr

import kotlinx.coroutines.flow.Flow

interface StartupDataRepository {

    val startupData: Flow<StartupData>

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setThemeConfig(themeConfig: ThemeConfig)

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

}

data class StartupData(
    val darkThemeConfig: ThemeConfig,
    val shouldHideOnboarding: Boolean,
)

enum class ThemeConfig {
    FOLLOW_SYSTEM, LIGHT, DARK,
}
