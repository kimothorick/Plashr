package com.kimothorick.plashr.di

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.auth.AuthRepository
import com.kimothorick.plashr.data.remote.PhotoDataService
import com.kimothorick.plashr.home.domain.HomePhotoRepository
import com.kimothorick.plashr.profile.domain.ProfileDataStore
import com.kimothorick.plashr.settings.domain.SettingsDataStore
import com.kimothorick.plashr.utils.connectivityObserver.ConnectivityObserver
import com.kimothorick.plashr.utils.connectivityObserver.ConnectivityObserverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing singleton-scoped dependencies related to
 * data storage, repositories, and utility services across the application.
 *
 * This module is installed in the [SingletonComponent], meaning the provided
 * dependencies will have a single instance throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    /**
     * Provides a singleton instance of [SettingsDataStore].
     *
     * This function is responsible for creating and providing the [SettingsDataStore]
     * which is used for managing user preferences and application settings. It uses
     * Dagger Hilt to inject the application context required by the data store.
     *
     * @param context The application context provided by Hilt.
     * @return A singleton instance of [SettingsDataStore].
     */
    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context,
    ): SettingsDataStore = SettingsDataStore(context)

    /**
     * Provides a singleton instance of [ProfileDataStore].
     *
     * This function is used by Hilt to inject the [ProfileDataStore]
     * which is responsible for managing and persisting user profile data, such as
     * authentication tokens and user information, using Jetpack DataStore.
     *
     * @param context The application context, provided by Hilt.
     * @return A singleton instance of [ProfileDataStore].
     */
    @Provides
    @Singleton
    fun provideProfileDataStore(
        @ApplicationContext context: Context,
    ): ProfileDataStore = ProfileDataStore(context)

    /**
     * Provides a singleton instance of [HomePhotoRepository].
     *
     * This function is used by Hilt to inject the repository responsible for fetching
     * home screen photo data from the remote data source.
     *
     * @param apiService The remote data service for photos, provided by another Hilt module.
     * @param crashlytics The Firebase Crashlytics instance for logging errors.
     * @return A singleton [HomePhotoRepository] instance.
     */
    @Provides
    @Singleton
    fun provideHomePhotoRepository(
        apiService: PhotoDataService,
        crashlytics: FirebaseCrashlytics,
    ): HomePhotoRepository = HomePhotoRepository(apiService, crashlytics)

    /**
     * Provides a singleton instance of [ConnectivityObserver].
     *
     * This observer is used throughout the application to monitor network connectivity status.
     * By providing it as a singleton, we ensure that there's only one active observer,
     * which is efficient and prevents resource leaks.
     *
     * @param context The application context, required to access the system's ConnectivityManager.
     * @return An implementation of [ConnectivityObserver].
     */
    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context,
    ): ConnectivityObserver = ConnectivityObserverImpl(context)

    /**
     * Provides a singleton instance of [AuthRepository].
     *
     * This repository is responsible for handling authentication-related data,
     * such as access tokens, by interacting with the [ProfileDataStore].
     *
     * @param profileDataStore The data store for persisting user profile and auth data.
     * @return A singleton [AuthRepository] instance.
     */
    @Singleton
    @Provides
    fun provideAuthRepository(
        profileDataStore: ProfileDataStore,
    ): AuthRepository = AuthRepository(profileDataStore)
}
