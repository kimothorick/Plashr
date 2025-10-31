package com.kimothorick.plashr.profile.domain.userLikes

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.user.UserPhotoLikes
import com.kimothorick.plashr.data.remote.UserDataService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for fetching a paginated list of photos liked by a specific user.
 *
 * This class uses the Paging 3 library to create a stream of `PagingData` that can be
 * efficiently displayed in a UI component like `LazyColumn`. It retrieves data from the
 * [UserDataService] via a [UserLikesPagingSource].
 *
 * @property userDataService The remote data source for user-related data.
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
class UserLikesRepository
    @Inject constructor(
        private val userDataService: UserDataService,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        /**
         * Retrieves a paginated flow of photos liked by a specific user.
         *
         * This function uses the Paging 3 library to create a stream of `PagingData`
         * containing `UserPhotoLikes`. The data is fetched from the network by
         * [UserLikesPagingSource].
         *
         * @param username The username of the user whose liked photos are to be fetched.
         * @return A [Flow] of [PagingData] that emits pages of [UserPhotoLikes].
         */
        fun getLikes(
            username: String,
        ): Flow<PagingData<UserPhotoLikes>> =
            Pager(
                config = PagingConfig(
                    pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                    prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                    enablePlaceholders = false,
                    initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
                ),
                pagingSourceFactory = { UserLikesPagingSource(username, userDataService, crashlytics) },
            ).flow
    }
