package com.kimothorick.plashr.profile.domain.userPhoto

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.user.UserPhoto
import com.kimothorick.plashr.data.remote.UserDataService

/**
 * A [PagingSource] that loads pages of photos for a specific user.
 *
 * This class is responsible for fetching paginated lists of a user's photos from the [UserDataService].
 * It extends [BasePagingSource] to handle the common paging logic, such as key management and error handling.
 *
 * @param username The username of the user whose photos are to be fetched.
 * @param userDataService The service used to make the network request for user photos.
 * @param crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
class UserPhotoPagingSource(
    private val username: String,
    private val userDataService: UserDataService,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<UserPhoto>(crashlytics) {
    override val logKeyName: String = "User Photo Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<UserPhoto> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val response = userDataService.getUserPhotos(
            username = username,
            page = page,
            perPage = params.loadSize,
            orderBy = "latest",
            stats = false,
            resolution = "days",
            quantity = 30,
        )

        return response.body() ?: throw NullResponseBodyException()
    }
}
