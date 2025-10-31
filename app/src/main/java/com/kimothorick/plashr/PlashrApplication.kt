package com.kimothorick.plashr

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

/**
 * The main [android.app.Application] class for the Plashr app.
 *
 * This class is annotated with [@HiltAndroidApp] to enable field injection in
 * Android framework classes and to set up the Hilt dependency injection container.
 *
 * It also implements [coil3.SingletonImageLoader.Factory] to provide a custom, singleton
 * [coil3.ImageLoader] instance for the Coil image loading library. This custom instance
 * is configured with crossfade animations and a debug logger for development builds.
 *
 */
@HiltAndroidApp
class PlashrApplication :
    Application(),
    SingletonImageLoader.Factory {
    override fun newImageLoader(
        context: PlatformContext,
    ): ImageLoader =
        ImageLoader.Builder(this).crossfade(true).also {
            if (BuildConfig.DEBUG) {
                it.logger(DebugLogger())
            }
        }.build()
}
