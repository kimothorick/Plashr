package com.kimothorick.plashr.search.domain.pagingsources

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.collection.SearchCollectionsResponse
import com.kimothorick.plashr.data.remote.SearchDataService
import javax.inject.Inject

/**
 * A [PagingSource] that pages through the Unsplash API's search collections endpoint.
 *
 * This class is responsible for fetching collections based on a search query from the
 * [SearchDataService] and providing them to a Pager.
 *
 * @param service The remote data service for fetching search results.
 * @param query The search term to find collections for.
 * @param crashlytics An instance of [FirebaseCrashlytics] for logging errors.
 *
 * @see SearchDataService
 */
class SearchCollectionsPagingSource @Inject constructor(
    private val service: SearchDataService,
    private val query: String,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<SearchCollectionsResponse.Result>(crashlytics = crashlytics) {
    override val logKeyName: String = "Search Collections Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<SearchCollectionsResponse.Result> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = service.searchCollections(
            query = query,
            page = page,
            perPage = params.loadSize,
        )

        return response.body()?.results?.filterNotNull() ?: throw NullResponseBodyException()
    }
}
