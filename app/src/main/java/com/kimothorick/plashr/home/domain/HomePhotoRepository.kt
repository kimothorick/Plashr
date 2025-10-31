package com.kimothorick.plashr.home.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.remote.PhotoDataService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for fetching photos for the home screen.
 * This class uses the Paging 3 library to provide a stream of paginated photo data.
 *
 * @property apiService The remote data source for fetching photos.
 * @property crashlytics An instance of Firebase Crashlytics for reporting errors.
 */
class HomePhotoRepository
    @Inject
    constructor(
        private val apiService: PhotoDataService,
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
                pagingSourceFactory = { PhotosPagingSource(apiService, crashlytics = crashlytics) },
            )
        }

        /**
         * Retrieves a [Flow] of [PagingData] containing [Photo] items.
         *
         * This function exposes the data stream from the Pager, which handles loading paginated data
         * from the [PhotosPagingSource]. The collected flow can be submitted to a PagingDataAdapter
         * to be displayed in a RecyclerView.
         *
         * @return A [Flow] that emits [PagingData] of photos.
         */
        fun getPhotos(): Flow<PagingData<Photo>> = pager.flow
    }
