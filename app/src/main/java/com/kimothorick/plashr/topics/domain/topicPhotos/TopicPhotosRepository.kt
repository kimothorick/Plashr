package com.kimothorick.plashr.topics.domain.topicPhotos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.topics.TopicPhoto
import com.kimothorick.plashr.data.remote.TopicsDataService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for fetching a paginated list of photos for a specific topic.
 *
 * This class uses the Paging 3 library to create a stream of [PagingData] that can be
 * observed by the UI layer. It configures a [Pager] with a [TopicPhotosPagingSource]
 * to handle the actual data loading from the [TopicsDataService].
 *
 * @property topicsDataService The remote data source for fetching topic-related data.
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
class TopicPhotosRepository
    @Inject
    constructor(
        private val topicsDataService: TopicsDataService,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        /**
         * Retrieves a paginated flow of photos for a specific topic.
         *
         * This function sets up a [Pager] from the Paging 3 library to handle the pagination
         * of photos for the given topic ID. It uses [TopicPhotosPagingSource] as the source
         * of data, which fetches photos from the [topicsDataService] page by page.
         *
         * @param topicID The unique identifier of the topic for which to fetch photos.
         * @return A [Flow] of [PagingData] containing [TopicPhoto] items.
         */
        fun getTopicPhotos(
            topicID: String,
        ): Flow<PagingData<TopicPhoto>> {
            val pager =
                Pager(
                    config =
                        PagingConfig(
                            pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                            prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                            enablePlaceholders = false,
                            initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
                        ),
                    pagingSourceFactory = { TopicPhotosPagingSource(topicID, topicsDataService, crashlytics) },
                )

            return pager.flow
        }
    }
