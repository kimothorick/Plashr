package com.kimothorick.plashr.topics.domain.topicPhotos

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.topics.TopicPhoto
import com.kimothorick.plashr.data.remote.TopicsDataService

/**
 * A [PagingSource] that loads photos for a specific topic from the Unsplash API.
 *
 * This class is responsible for fetching paginated data from the [TopicsDataService]
 * for a given topic ID and providing it to the Paging library.
 *
 * @param topicID The ID of the topic for which photos are to be fetched.
 * @param topicsDataService The service used to make network requests to the Unsplash API.
 * @param crashlytics The Firebase Crashlytics instance for logging exceptions.
 * @see BasePagingSource
 * @see com.kimothorick.plashr.data.models.topics.TopicPhoto
 */
class TopicPhotosPagingSource(
    private val topicID: String,
    private val topicsDataService: TopicsDataService,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<TopicPhoto>(crashlytics) {
    override val logKeyName: String = "Topic Photos Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<TopicPhoto> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = topicsDataService.getTopicPhotos(
            id = topicID,
            page = page,
            perPage = params.loadSize,
            orderBy = "latest",
        )
        return response.body() ?: throw NullResponseBodyException()
    }
}
