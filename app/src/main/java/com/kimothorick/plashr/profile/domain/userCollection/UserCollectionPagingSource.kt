package com.kimothorick.plashr.profile.domain.userCollection

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.data.remote.UserDataService

/**
 * A [PagingSource] that loads pages of collections for a specific user.
 *
 * This class fetches paginated user collections from the [UserDataService]
 * and provides them to a Pager from the Android Paging library. It extends
 * [BasePagingSource] to handle common paging logic like key management and
 * error handling.
 *
 * @param username The username of the user whose collections are to be fetched.
 * @param userDataService The service used to make API calls to get user collections.
 * @param crashlytics The Firebase Crashlytics instance for logging exceptions.
 * @see androidx.paging.PagingSource
 * @see BasePagingSource
 * @see UserCollection
 * @see UserDataService
 */
class UserCollectionPagingSource(
    private val username: String,
    private val userDataService: UserDataService,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<UserCollection>(crashlytics) {
    override val logKeyName: String = "User Collections Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<UserCollection> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = userDataService.getUserCollections(username, page, params.loadSize)

        return response.body() ?: throw NullResponseBodyException()
    }
}
