package com.kimothorick.plashr.topics.domain.topics

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.data.remote.TopicsDataService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Repository for fetching topics data from the network.
 *
 * This class is responsible for abstracting the data source for topics. It uses the Paging 3 library
 * to provide a `Flow` of `PagingData<Topic>`, which allows for efficient and gradual loading of topics data.
 *
 * @param topicsDataService The remote data service for fetching topics.
 * @param crashlytics The Firebase Crashlytics instance for logging errors.
 */
class TopicsRepository
    @Inject
    constructor(
        private val topicsDataService: TopicsDataService,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        private val pager by lazy {
            Pager(
                config =
                    PagingConfig(
                        pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                        prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                        enablePlaceholders = false,
                        initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
                    ),
                pagingSourceFactory = { TopicsPagingSource(topicsDataService, crashlytics) },
            )
        }

        /**
         * Retrieves a [Flow] of [PagingData] for topics.
         *
         * This function provides a stream of paginated topic data, suitable for use with
         * libraries like Jetpack Paging 3 to display an infinitely scrolling list of topics.
         * The data is fetched from the [TopicsPagingSource] which is managed by an internal [Pager].
         *
         * @return A [Flow] emitting [PagingData] of [Topic] items.
         */
        fun getTopics(): Flow<PagingData<Topic>> = pager.flow
    }
