package com.kimothorick.plashr

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The main application class for the Plashr application.
 *
 * Annotated with `@HiltAndroidApp` to enableHilt dependency injection.
 */
@HiltAndroidApp
class PlashrApplication:Application() {
}