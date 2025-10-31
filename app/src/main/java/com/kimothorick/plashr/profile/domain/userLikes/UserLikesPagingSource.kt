package com.kimothorick.plashr.profile.domain.userLikes

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.user.UserPhotoLikes
import com.kimothorick.plashr.data.remote.UserDataService

/**
 * A [PagingSource] that loads pages of photos liked by a specific user.
 *
 * This class handles the logic for fetching paginated data of a user's liked photos
 * from the [UserDataService]. It is used by the Paging 3 library to create a stream
 * of paged data.
 *
 * @param username The username of the user whose liked photos are to be fetched.
 * @param userDataService The service responsible for making the API call to fetch user data.
 * @param crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
class UserLikesPagingSource(
    private val username: String,
    private val userDataService: UserDataService,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<UserPhotoLikes>(crashlytics) {
    override val logKeyName: String = "User Likes Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<UserPhotoLikes> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = userDataService.getUserLikes(
            username,
            page,
            params.loadSize,
            orderBy = "latest",
            orientation = null,
        )

        return response.body() ?: throw NullResponseBodyException()
    }
}
