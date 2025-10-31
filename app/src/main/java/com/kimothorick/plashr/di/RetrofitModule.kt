package com.kimothorick.plashr.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.collections.domain.CollectionsPagingSource
import com.kimothorick.plashr.collections.domain.CollectionsRepository
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.remote.CollectionsDataService
import com.kimothorick.plashr.data.remote.PhotoDataService
import com.kimothorick.plashr.data.remote.SearchDataService
import com.kimothorick.plashr.data.remote.TopicsDataService
import com.kimothorick.plashr.data.remote.UserDataService
import com.kimothorick.plashr.di.RetrofitModule.provideBaseURL
import com.kimothorick.plashr.di.RetrofitModule.provideOkHttpClient
import com.kimothorick.plashr.home.domain.PhotosPagingSource
import com.kimothorick.plashr.photoDetails.domain.PhotoRepository
import com.kimothorick.plashr.profile.domain.ProfileDataStore
import com.kimothorick.plashr.profile.domain.userCollection.UserCollectionPagingSource
import com.kimothorick.plashr.profile.domain.userCollection.UserCollectionRepository
import com.kimothorick.plashr.profile.domain.userLikes.UserLikesPagingSource
import com.kimothorick.plashr.profile.domain.userLikes.UserLikesRepository
import com.kimothorick.plashr.profile.domain.userPhoto.UserPhotoPagingSource
import com.kimothorick.plashr.profile.domain.userPhoto.UserPhotoRepository
import com.kimothorick.plashr.search.domain.SearchRepository
import com.kimothorick.plashr.topics.domain.topicPhotos.TopicPhotosRepository
import com.kimothorick.plashr.topics.domain.topics.TopicsPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing network-related dependencies, primarily centered around Retrofit.
 *
 * This module is responsible for configuring and providing instances of:
 * - Retrofit and its underlying OkHttpClient.
 * - An [AuthenticationInterceptor] to handle API authentication.
 * - Various data service interfaces for different Unsplash API endpoints.
 * - Repositories and PagingSources that utilize these data services to fetch data.
 *
 * All provided dependencies are scoped as singletons to ensure a single instance is used
 * throughout the application's lifecycle, improving performance and resource management.
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    fun provideBaseURL(): String = "https://api.unsplash.com/"

    /**
     * Provides a singleton instance of [AuthenticationInterceptor].
     *
     * This interceptor is responsible for adding the necessary authentication headers
     * to outgoing network requests. It retrieves authentication details, like an access token,
     * from the [ProfileDataStore] to authorize requests with the Unsplash API.
     *
     * @param profileDataStore The data store used to access saved user profile and authentication data.
     * @return A singleton [AuthenticationInterceptor] instance.
     */
    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(
        profileDataStore: ProfileDataStore,
    ): AuthenticationInterceptor = AuthenticationInterceptor(profileDataStore)

    /**
     * Provides a singleton [OkHttpClient] instance.
     *
     * This client is configured with an [AuthenticationInterceptor] to handle
     * authorization for all outgoing network requests.
     *
     * @param authInterceptor The interceptor used to add authentication headers to requests.
     * @return A configured [OkHttpClient] instance.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthenticationInterceptor,
    ): OkHttpClient = OkHttpClient.Builder().addInterceptor(authInterceptor).build()

    /**
     * Provides a singleton [Retrofit] instance for network operations.
     *
     * This function configures Retrofit with the base URL for the Unsplash API and
     * an [OkHttpClient] that includes an authentication interceptor. It also adds
     * a [GsonConverterFactory] to handle JSON serialization and deserialization.
     *
     * @param baseURL The base URL for the API, provided by [provideBaseURL].
     * @param okHttpClient The OkHttp client to use for requests, provided by [provideOkHttpClient].
     * @return A configured [Retrofit] instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        baseURL: String,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder().baseUrl(baseURL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()

    /**
     * Provides a singleton instance of [UserDataService].
     *
     * This function uses the provided [Retrofit] instance to create and configure
     * the service responsible for making API calls related to user data.
     *
     * @param retrofit The configured Retrofit instance for network operations.
     * @return A singleton instance of [UserDataService].
     */
    @Provides
    @Singleton
    fun provideUserDataService(
        retrofit: Retrofit,
    ): UserDataService = retrofit.create(UserDataService::class.java)

    /**
     * Provides a singleton instance of [PhotoDataService].
     * This service is used for all API calls related to photos, such as fetching lists of photos.
     *
     * @param retrofit The [Retrofit] instance used to create the service.
     * @return An instance of [PhotoDataService].
     */
    @Provides
    @Singleton
    fun providePhotosService(
        retrofit: Retrofit,
    ): PhotoDataService = retrofit.create(PhotoDataService::class.java)

    /**
     * Provides a singleton instance of [TopicsDataService].
     *
     * This function uses the provided [Retrofit] instance to create a service
     * for interacting with the Unsplash API's topics-related endpoints.
     *
     * @param retrofit The [Retrofit] instance configured for the Unsplash API.
     * @return An implementation of the [TopicsDataService] interface.
     */
    @Provides
    @Singleton
    fun provideTopicsService(
        retrofit: Retrofit,
    ): TopicsDataService = retrofit.create(TopicsDataService::class.java)

    /**
     * Provides a singleton instance of [SearchDataService].
     *
     * This function uses the provided [Retrofit] instance to create and configure
     * the service responsible for handling search-related API calls to the Unsplash API.
     *
     * @param retrofit The configured [Retrofit] instance for making network requests.
     * @return An instance of [SearchDataService].
     */
    @Provides
    @Singleton
    fun provideSearchService(
        retrofit: Retrofit,
    ): SearchDataService = retrofit.create(SearchDataService::class.java)

    /**
     * Provides a singleton instance of [PhotoRepository].
     * This repository is responsible for fetching photo-related data.
     *
     * @param photoDataService The data service for accessing photo data from the remote API.
     * @return A singleton [PhotoRepository] instance.
     */
    @Provides
    fun providePhotoRepository(
        photoDataService: PhotoDataService,
        errorHandler: ErrorHandler,
        crashlytics: FirebaseCrashlytics,
    ): PhotoRepository =
        PhotoRepository(
            photoDataService = photoDataService,
            errorHandler = errorHandler,
            crashlytics = crashlytics,
        )

    /**
     * Dagger Hilt provider for creating a singleton instance of [PhotosPagingSource].
     *
     * This function supplies the necessary dependencies to construct a `PhotosPagingSource`,
     * which is used to paginate through the list of photos from the Unsplash API.
     *
     * @param photoDataService The remote data service for fetching photos.
     * @return A singleton instance of [PhotosPagingSource].
     */
    @Provides
    @Singleton
    fun providePhotosPagingSource(
        photoDataService: PhotoDataService,
        crashlytics: FirebaseCrashlytics,
    ): PhotosPagingSource =
        PhotosPagingSource(
            photoDataService = photoDataService,
            crashlytics = crashlytics,
        )

    /**
     * Provides a singleton instance of [TopicsPagingSource].
     *
     * This function is used by Hilt to inject a [TopicsPagingSource], which is responsible
     * for loading paginated topic data from the Unsplash API.
     *
     * @param topicsDataService The data service for fetching topics.
     * @param crashlytics The crashlytics instance for logging errors.
     * @return An instance of [TopicsPagingSource].
     */
    @Provides
    @Singleton
    fun provideTopicsPagingSource(
        topicsDataService: TopicsDataService,
        crashlytics: FirebaseCrashlytics,
    ): TopicsPagingSource =
        TopicsPagingSource(
            topicsDataService = topicsDataService,
            crashlytics = crashlytics,
        )

    /**
     * Provides a singleton instance of [TopicPhotosRepository].
     *
     * This repository is responsible for fetching photos related to a specific topic
     * from the Unsplash API.
     *
     * @param topicsDataService The data service used to make network calls for topics.
     * @return A singleton instance of [TopicPhotosRepository].
     */
    @Singleton
    @Provides
    fun provideTopicPhotosRepository(
        topicsDataService: TopicsDataService,
        crashlytics: FirebaseCrashlytics,
    ): TopicPhotosRepository = TopicPhotosRepository(topicsDataService, crashlytics)

    /**
     * Provides a singleton instance of [CollectionsDataService].
     *
     * This function is used by Hilt to inject the Retrofit service that handles
     * API requests related to photo collections from the Unsplash API.
     *
     * @param retrofit The configured [Retrofit] instance.
     * @return An implementation of the [CollectionsDataService] interface.
     */
    @Singleton
    @Provides
    fun provideCollectionsService(
        retrofit: Retrofit,
    ): CollectionsDataService = retrofit.create(CollectionsDataService::class.java)

    /**
     * Provides a singleton instance of [CollectionsPagingSource].
     *
     * This function is used by Hilt to inject the paging source for fetching collections
     * from the Unsplash API. It depends on [CollectionsDataService] to make the actual
     * network requests.
     *
     * @param collectionsDataService The data service responsible for fetching collections data.
     * @return An instance of [CollectionsPagingSource].
     */
    @Provides
    @Singleton
    fun provideCollectionsPagingSource(
        collectionsDataService: CollectionsDataService,
        crashlytics: FirebaseCrashlytics,
    ): CollectionsPagingSource =
        CollectionsPagingSource(
            collectionsDataService = collectionsDataService,
            crashlytics = crashlytics,
        )

    /**
     * Provides a singleton instance of [CollectionsRepository].
     *
     * This repository is responsible for fetching collection-related data from the remote data source.
     * It is used to get details for a specific collection.
     *
     * @param collectionsDataService The remote data service for collections.
     * @return A singleton instance of [CollectionsRepository].
     */
    @Provides
    @Singleton
    fun provideCollectionsRepository(
        collectionsDataService: CollectionsDataService,
        crashlytics: FirebaseCrashlytics,
    ): CollectionsRepository = CollectionsRepository(collectionsDataService, crashlytics = crashlytics)

    /**
     * Provides a singleton instance of [UserCollectionPagingSource].
     *
     * This function creates a [UserCollectionPagingSource] which is used to paginate
     * through the collections of a specific user from the Unsplash API.
     * It depends on the user's [username] and the [UserDataService] for fetching data.
     *
     * Note: A `username` must be provided elsewhere in the dependency graph for this provider to work.
     * This is typically handled by a different scope, such as an `@ActivityScoped` or `@ViewModelScoped`
     * component that can provide the specific username for the profile being viewed.
     *
     * @param username The username of the user whose collections are to be fetched.
     * @param userDataService The service responsible for making API calls related to user data.
     * @return A configured instance of [UserCollectionPagingSource].
     */
    @Provides
    @Singleton
    fun provideUserCollectionPagingSource(
        username: String,
        userDataService: UserDataService,
        crashlytics: FirebaseCrashlytics,
    ): UserCollectionPagingSource =
        UserCollectionPagingSource(
            username = username,
            userDataService = userDataService,
            crashlytics = crashlytics,
        )

    /**
     * Provides a singleton instance of [UserCollectionRepository].
     *
     * This repository is responsible for handling data operations related to a user's collections,
     * such as fetching the list of collections created by a specific user. It depends on
     * [UserDataService] and [CollectionsDataService] to perform these operations.
     *
     * @param userDataService The service for fetching user-specific data.
     * @param collectionsDataService The service for fetching collection data.
     * @param crashlytics The crashlytics instance for logging errors.
     * @return A singleton [UserCollectionRepository] instance.
     */
    @Provides
    @Singleton
    fun provideUserCollectionRepository(
        userDataService: UserDataService,
        collectionsDataService: CollectionsDataService,
        crashlytics: FirebaseCrashlytics,
    ): UserCollectionRepository =
        UserCollectionRepository(
            userDataService = userDataService,
            collectionsDataService = collectionsDataService,
            crashlytics = crashlytics,
        )

    /**
     * Provides a singleton instance of [UserPhotoPagingSource].
     *
     * This function creates a [UserPhotoPagingSource] which is used to load photos
     * posted by a specific user in a paginated manner.
     *
     * @param username The username of the user whose photos are to be fetched.
     * @param userDataService The service responsible for fetching user-related data from the API.
     * @return An instance of [UserPhotoPagingSource] configured for the given user.
     */
    @Provides
    @Singleton
    fun provideUserPhotosPagingSource(
        username: String,
        userDataService: UserDataService,
        crashlytics: FirebaseCrashlytics,
    ): UserPhotoPagingSource =
        UserPhotoPagingSource(
            username = username,
            userDataService = userDataService,
            crashlytics = crashlytics,
        )

    /**
     * Provides a singleton instance of [UserPhotoRepository].
     *
     * This repository is responsible for fetching photos uploaded by a specific user.
     *
     * @param userDataService The remote data source for user-related data.
     * @return An instance of [UserPhotoRepository].
     */
    @Provides
    @Singleton
    fun provideUserPhotoRepository(
        userDataService: UserDataService,
        crashlytics: FirebaseCrashlytics,
    ): UserPhotoRepository = UserPhotoRepository(userDataService, crashlytics = crashlytics)

    /**
     * Provides a singleton instance of [UserLikesPagingSource].
     *
     * This paging source is responsible for fetching a paginated list of photos liked by a specific user.
     *
     * @param username The username of the user whose liked photos are to be fetched.
     * @param userDataService The service to fetch user-related data from the API.
     * @return An instance of [UserLikesPagingSource].
     */
    @Provides
    @Singleton
    fun provideUserLikesPagingSource(
        username: String,
        userDataService: UserDataService,
        crashlytics: FirebaseCrashlytics,
    ): UserLikesPagingSource =
        UserLikesPagingSource(
            username = username,
            userDataService = userDataService,
            crashlytics = crashlytics,
        )

    /**
     * Provides a singleton instance of [UserLikesRepository].
     *
     * This repository is responsible for fetching photos liked by a specific user from the data source.
     * It's used to populate the user's "Likes" tab in their profile.
     *
     * @param userDataService The remote data service for user-related API calls.
     * @return A singleton instance of [UserLikesRepository].
     */
    @Provides
    @Singleton
    fun provideUserLikesRepository(
        userDataService: UserDataService,
        crashlytics: FirebaseCrashlytics,
    ): UserLikesRepository = UserLikesRepository(userDataService = userDataService, crashlytics = crashlytics)

    /**
     * Provides a singleton instance of [SearchRepository].
     *
     * This function creates and provides a repository for handling search-related data operations,
     * such as fetching search results for photos, collections, and users from the Unsplash API.
     * It depends on [SearchDataService] to make the actual network requests.
     *
     * @param searchDataService The data service responsible for making network calls to the search endpoints.
     * @param crashlytics The crashlytics instance for logging errors.
     * @return A singleton instance of [SearchRepository].
     */
    @Provides
    @Singleton
    fun provideSearchRepository(
        searchDataService: SearchDataService,
        crashlytics: FirebaseCrashlytics,
    ): SearchRepository = SearchRepository(searchDataService = searchDataService, crashlytics = crashlytics)
}
