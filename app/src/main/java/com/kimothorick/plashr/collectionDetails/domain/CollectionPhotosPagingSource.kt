package com.kimothorick.plashr.collectionDetails.domain

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.collection.CollectionPhoto
import com.kimothorick.plashr.data.remote.CollectionsDataService

/**
 * A [PagingSource]implementation for fetching photos belonging to a specific collection from the Unsplash API.
 *
 * This class extends [BasePagingSource] to handle the pagination logic, loading data page by page
 * as the user scrolls. It uses the [CollectionsDataService] to make the actual network requests.
 *
 * @param collectionID The unique identifier of the collection for which to fetch photos.
 * @param collectionsDataService The service class responsible for making API calls to the collections endpoint.
 * @param crashlytics An instance of [FirebaseCrashlytics] for logging errors and reporting crashes.
 * @see BasePagingSource
 * @see CollectionPhoto
 */
class CollectionPhotosPagingSource(
    private val collectionID: String,
    private val collectionsDataService: CollectionsDataService,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<CollectionPhoto>(crashlytics = crashlytics) {
    override val logKeyName: String = "Collection Photos Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<CollectionPhoto> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = collectionsDataService.getCollectionPhotos(
            id = collectionID,
            page = page,
            perPage = params.loadSize,
        )

        return response.body() ?: throw NullResponseBodyException()
    }
}
