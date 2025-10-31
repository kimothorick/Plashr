package com.kimothorick.plashr.collections.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.Collection
import com.kimothorick.plashr.data.remote.CollectionsDataService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Repository for fetching collections data.
 *
 * This class is responsible for abstracting the data source for collections. It uses the Paging 3
 * library to provide a reactive stream of paged data to the UI layer.
 *
 * @param collectionsDataService The data service responsible for making network requests to fetch collections.
 * @param crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
class CollectionsRepository @Inject constructor(
    private val collectionsDataService: CollectionsDataService,
    private val crashlytics: FirebaseCrashlytics,
) {
    private val pager by lazy {
        Pager(
            PagingConfig(
                pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                enablePlaceholders = false,
                initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = { CollectionsPagingSource(collectionsDataService, crashlytics) },
        )
    }

    /**
     * Retrieves a [Flow] of [PagingData] for collections.
     *
     * This function provides a stream of paginated collection data, suitable for displaying
     * in a RecyclerView or a similar UI component that supports paging. The data is fetched
     * from the remote data source as the user scrolls.
     *
     * @return A [Flow] emitting [PagingData] of [Collection] items.
     */
    fun getCollections(): Flow<PagingData<Collection>> = pager.flow
}
