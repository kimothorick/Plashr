package com.kimothorick.plashr.topics.domain.topics

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.data.remote.TopicsDataService
import jakarta.inject.Inject

/**
 * A [PagingSource] for fetching a list of [Topic] items from the Unsplash API.
 *
 * This class extends [BasePagingSource] and is responsible for loading paginated data
 * for topics. It interacts with the [TopicsDataService] to retrieve the data from the
 * remote server.
 *
 * @param topicsDataService The service responsible for making API calls to fetch topic data.
 * @param crashlytics An instance of [FirebaseCrashlytics] for logging errors.
 * @see BasePagingSource
 * @see Topic
 * @see TopicsDataService
 */
class TopicsPagingSource
    @Inject
    constructor(
        private val topicsDataService: TopicsDataService,
        crashlytics: FirebaseCrashlytics,
    ) : BasePagingSource<Topic>(crashlytics = crashlytics) {
        override val logKeyName: String = "Topics Paging"

        override suspend fun loadData(
            params: LoadParams<Int>,
        ): List<Topic> {
            val page = params.key ?: Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
            val response = topicsDataService.getTopics(page, params.loadSize)

            return response.body() ?: throw NullResponseBodyException()
        }
    }
