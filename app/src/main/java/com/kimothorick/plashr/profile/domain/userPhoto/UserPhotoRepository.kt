package com.kimothorick.plashr.profile.domain.userPhoto

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.user.UserPhoto
import com.kimothorick.plashr.data.remote.UserDataService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for fetching a user's photos.
 * This class handles the logic for creating a paginated stream of photos for a specific user.
 *
 * @property userDataService The remote data source for fetching user-related data.
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
class UserPhotoRepository
    @Inject constructor(
        private val userDataService: UserDataService,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        /**
         * Fetches a paginated list of photos for a specific user.
         *
         * This function uses the Paging 3 library to create a stream of `PagingData`
         * that can be collected by the UI layer to display a user's photos in a list.
         * The data is sourced from a [UserPhotoPagingSource].
         *
         * @param username The username of the user whose photos are to be fetched.
         * @return A [Flow] of [PagingData] containing the user's photos ([UserPhoto]).
         */
        fun getPhotos(
            username: String,
        ): Flow<PagingData<UserPhoto>> =
            Pager(
                config = PagingConfig(
                    pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                    prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                    enablePlaceholders = false,
                    initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
                ),
                pagingSourceFactory = { UserPhotoPagingSource(username, userDataService, crashlytics) },
            ).flow
    }
