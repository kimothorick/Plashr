package com.kimothorick.plashr.di

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.utils.DownloadHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Dagger Hilt module responsible for providing download-related dependencies.
 *
 * This module is installed in the `ViewModelComponent`, meaning the provided dependencies
 * will have a lifecycle scoped to ViewModels.
 */
@Module
@InstallIn(ViewModelComponent::class)
object DownloadModule {
    @Provides
    fun provideDownloadHandler(
        @ApplicationContext context: Context,
        crashlytics: FirebaseCrashlytics,
    ): DownloadHandler = DownloadHandler(context, crashlytics)
}
