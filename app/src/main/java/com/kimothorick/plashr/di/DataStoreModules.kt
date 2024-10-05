package com.kimothorick.plashr.di

import android.content.Context
import com.kimothorick.plashr.StartupDataRepository
import com.kimothorick.plashr.data.remote.PhotoDataService
import com.kimothorick.plashr.home.domain.PhotosPagingSource
import com.kimothorick.plashr.profile.domain.ProfileDataStore
import com.kimothorick.plashr.settings.domain.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * Hilt module for providing DataStore instances.
 *
 * This module is responsible for providing instances of [SettingsDataStore] and[ProfileDataStore]
 * to be used in the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Provides a singleton instance of [SettingsDataStore].
     *
     * @param context The application context.
     * @return The [SettingsDataStore] instance.
     */
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context)
    }

    /**
     * Provides a singleton instance of [ProfileDataStore].
     *
     * @param context The application context.
     * @return The [ProfileDataStore] instance.
     */
    @Provides
    @Singleton
    fun provideProfileDataStore(@ApplicationContext context: Context): ProfileDataStore {
        return ProfileDataStore(context)
    }

}