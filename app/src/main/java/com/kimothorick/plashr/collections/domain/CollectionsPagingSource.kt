package com.kimothorick.plashr.collections.domain

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.collection.Collection
import com.kimothorick.plashr.data.remote.CollectionsDataService

/**
 * A [PagingSource] that loads collections from the Unsplash API.
 *
 * This class is responsible for fetching paginated data for the collections list.
 * It uses [CollectionsDataService] to make the network request.
 *
 * @param collectionsDataService The service used to fetch collections from the remote API.
 * @param crashlytics The Firebase Crashlytics instance for logging exceptions.
 * @see BasePagingSource
 */
class CollectionsPagingSource(
    private val collectionsDataService: CollectionsDataService,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<Collection>(crashlytics) {
    override val logKeyName: String = "Collections Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<Collection> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = collectionsDataService.getCollections(page, params.loadSize)

        return response.body() ?: throw NullResponseBodyException()
    }
}
