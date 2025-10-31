package com.kimothorick.plashr.collectionDetails.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.CollectionPhoto
import com.kimothorick.plashr.data.remote.CollectionsDataService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for fetching photos belonging to a specific collection (topic).
 *
 * This class uses the Paging 3 library to provide a stream of paginated data
 * for displaying a collection's photos.
 *
 * @property collectionsDataService The remote data source for fetching collection-related data.
 * @property crashlytics An instance of FirebaseCrashlytics for reporting errors.
 */
class CollectionsPhotosRepository @Inject
    constructor(
        private val collectionsDataService: CollectionsDataService,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        fun getTopicPhotos(
            topicID: String,
        ): Flow<PagingData<CollectionPhoto>> {
            val pager =
                Pager(
                    config =
                        PagingConfig(
                            pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                            prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                            enablePlaceholders = false,
                            initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
                        ),
                    pagingSourceFactory = {
                        CollectionPhotosPagingSource(topicID, collectionsDataService, crashlytics = crashlytics)
                    },
                )

            return pager.flow
        }
    }
