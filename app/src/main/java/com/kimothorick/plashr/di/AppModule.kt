package com.kimothorick.plashr.di

import android.content.Context
import com.kimothorick.plashr.common.paging.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides application-level dependencies.
 *
 * This module is installed in the [SingletonComponent], meaning any bindings
 * provided here will have a singleton scope and be available throughout the
 * application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides a singleton instance of [ErrorHandler].
     *
     * This function is used by Dagger Hilt to inject the [ErrorHandler]
     * dependency throughout the application. It ensures that only one instance
     * of the error handler exists.
     *
     * @param context The application context, provided by Hilt.
     * @return A singleton instance of [ErrorHandler].
     */
    @Provides
    @Singleton
    fun provideErrorHandler(
        @ApplicationContext context: Context,
    ): ErrorHandler {
        return ErrorHandler(context)
    }
}
