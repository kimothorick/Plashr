package com.kimothorick.plashr.di

import android.content.Context
import com.kimothorick.plashr.data.remote.PhotoDataService
import com.kimothorick.plashr.profile.domain.ProfileDataStore
import com.kimothorick.plashr.data.remote.UserDataService
import com.kimothorick.plashr.home.domain.PhotosPagingSource
import com.kimothorick.plashr.settings.domain.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * A Hilt module that provides dependencies for networking related components.
 *
 * This module is installed in the `SingletonComponent`,which means that the provided dependencies
 * will have a singleton scope and be available throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    /**
     * Provides the base URL for the Unsplash API.
     *
     * @return The base URL string.
     */
    @Provides
    fun provideBaseURL(): String = "https://api.unsplash.com/"

    /**
     * Provides an instance of [AuthenticationInterceptor].
     *
     * @param profileDataStore The data store used to access user profile information.
     * @return An instance of [AuthenticationInterceptor].
     */
    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(
        profileDataStore: ProfileDataStore,
    ): AuthenticationInterceptor {
        return AuthenticationInterceptor(profileDataStore)
    }

    /**
     * Provides an instance of[OkHttpClient] configured with the [AuthenticationInterceptor].
     *
     * @param authInterceptor The authentication interceptor to add to the OkHttp client.
     * @return An instance of [OkHttpClient].
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthenticationInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    /**
     * Provides an instance of [Retrofit] configured with the base URL and OkHttp client.
     *
     * @param baseURL The base URL for the API.
     * @param okHttpClient The OkHttp client to use for network requests.
     * @return An instance of [Retrofit].
     */
    @Provides
    @Singleton
    fun provideRetrofit(baseURL: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(baseURL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    /**
     * Provides an instance of [UserDataService].
     *
     * @param retrofit The Retrofit instance used to create the API service.
     * @return An instance of [UserDataService].
     */
    @Provides
    @Singleton
    fun provideUserDataService(retrofit: Retrofit): UserDataService =
        retrofit.create(UserDataService::class.java)

    /**
     * Provides an instance of [PhotoDataService].
     *
     * @param retrofit The Retrofit instance used to create the API service.
     * @return An instance of [PhotoDataService].
     */
    @Provides
    @Singleton
    fun providePhotosService(retrofit: Retrofit): PhotoDataService =
        retrofit.create(PhotoDataService::class.java)


    @Provides
    @Singleton
    fun providePhotosPagingSource(
        photoDataService: PhotoDataService,
    ): PhotosPagingSource {
        return PhotosPagingSource(
            photoDataService = photoDataService
        )
    }

}