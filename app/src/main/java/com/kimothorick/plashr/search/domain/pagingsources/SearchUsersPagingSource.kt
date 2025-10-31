package com.kimothorick.plashr.search.domain.pagingsources

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.user.SearchUsersResponse
import com.kimothorick.plashr.data.remote.SearchDataService
import jakarta.inject.Inject

/**
 * A [PagingSource] that loads paginated user search results from the Unsplash API.
 *
 * This class is responsible for fetching pages of users based on a search query.
 * It handles the logic for loading data, managing page keys (for previous and next pages),
 * and handling potential network or API errors.
 *
 * @param service The [SearchDataService] instance used to make API calls to search for users.
 * @param query The search query string used to find users.
 * @param crashlytics The [FirebaseCrashlytics] instance for logging errors.
 * @see BasePagingSource
 * @see com.kimothorick.plashr.data.models.user.SearchUsersResponse.Result
 */
class SearchUsersPagingSource @Inject constructor(
    private val service: SearchDataService,
    private val query: String,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<SearchUsersResponse.Result>(crashlytics = crashlytics) {
    override val logKeyName: String = "Search Users Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<SearchUsersResponse.Result> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = service.searchUsers(
            query = query,
            page = page,
            perPage = params.loadSize,
        )

        return response.body()?.results?.filterNotNull() ?: throw NullResponseBodyException()
    }
}
