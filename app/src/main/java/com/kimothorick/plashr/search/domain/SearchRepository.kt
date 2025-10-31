package com.kimothorick.plashr.search.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.SearchCollectionsResponse
import com.kimothorick.plashr.data.models.photo.SearchPhotosResponse
import com.kimothorick.plashr.data.models.user.SearchUsersResponse
import com.kimothorick.plashr.data.remote.SearchDataService
import com.kimothorick.plashr.search.domain.pagingsources.SearchCollectionsPagingSource
import com.kimothorick.plashr.search.domain.pagingsources.SearchPhotosPagingSource
import com.kimothorick.plashr.search.domain.pagingsources.SearchUsersPagingSource
import com.kimothorick.plashr.search.presentation.components.FilterOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for handling search-related data operations.
 *
 * This class serves as a single source of truth for search data. It uses the `SearchDataService`
 * to fetch data from the network and exposes it as a stream of paginated data using the Paging 3
 * library.
 *
 * @param searchDataService The remote data source for performing search queries.
 * @param crashlytics An instance of Firebase Crashlytics for logging errors.
 */
class SearchRepository @Inject constructor(
    private val searchDataService: SearchDataService,
    private val crashlytics: FirebaseCrashlytics,
) {
    /**
     * Searches for photos based on a query and optional filters, returning a paginated stream of results.
     *
     * This function utilizes the Paging 3 library to create a reactive stream of `PagingData`
     * which can be efficiently consumed by the UI layer. It sets up a `Pager` with a standard
     * configuration and uses [SearchPhotosPagingSource] to fetch the data from the remote service
     * page by page.
     *
     * @param query The search query string.
     * @param filters A [FilterOptions] object containing additional filtering criteria such as
     * orientation, color, and sorting order.
     * @return A [Flow] of [PagingData] containing the search results ([SearchPhotosResponse.Result]).
     */
    fun searchPhotos(
        query: String,
        filters: FilterOptions,
    ): Flow<PagingData<SearchPhotosResponse.Result>> =
        Pager(
            config = PagingConfig(
                pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                enablePlaceholders = false,
                initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                SearchPhotosPagingSource(
                    service = searchDataService,
                    query = query,
                    filters = filters,
                    crashlytics = crashlytics,
                )
            },
        ).flow

    /**
     * Searches for collections based on a query string.
     *
     * This function uses the Paging 3 library to create a reactive stream of paged data.
     * It sets up a [Pager] with a specific [PagingConfig] and uses [SearchCollectionsPagingSource]
     * as the factory for creating [androidx.paging.PagingSource] instances.
     *
     * @param query The search query string for collections.
     * @return A [Flow] of [PagingData] containing the search results for collections.
     */
    fun searchCollections(
        query: String,
    ): Flow<PagingData<SearchCollectionsResponse.Result>> =
        Pager(
            config = PagingConfig(
                pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                enablePlaceholders = false,
                initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                SearchCollectionsPagingSource(
                    service = searchDataService,
                    query = query,
                    crashlytics = crashlytics,
                )
            },
        ).flow

    /**
     * Searches for users based on a query string.
     *
     * This function utilizes the Paging 3 library to create a stream of paginated user search results.
     * It configures a [Pager] with a specific page size and prefetch distance, and uses
     * [SearchUsersPagingSource] as the factory for fetching data from the [searchDataService].
     *
     * @param query The search query string used to find users.
     * @return A [Flow] of [PagingData] containing the search results for users.
     */
    fun searchUsers(
        query: String,
    ): Flow<PagingData<SearchUsersResponse.Result>> =
        Pager(
            config = PagingConfig(
                pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                enablePlaceholders = false,
                initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                SearchUsersPagingSource(
                    service = searchDataService,
                    query = query,
                    crashlytics = crashlytics,
                )
            },
        ).flow
}
