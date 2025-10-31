package com.kimothorick.plashr.home.domain

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.remote.PhotoDataService
import javax.inject.Inject

/**
 * A [PagingSource] that loads pages of photos from the Unsplash API.
 * This class is used by the Paging 3 library to create a stream of paged data.
 *
 * It extends [BasePagingSource] to handle the common logic for loading pages,
 * error handling, and determining the next page key.
 *
 * @param photoDataService The service used to make network requests for photos.
 * @param crashlytics The Crashlytics instance used for logging errors.
 * @see BasePagingSource
 * @see com.kimothorick.plashr.data.models.photo.Photo
 */
class PhotosPagingSource
    @Inject
    constructor(
        private val photoDataService: PhotoDataService,
        crashlytics: FirebaseCrashlytics,
    ) : BasePagingSource<Photo>(crashlytics) {
        override val logKeyName: String = "Photos Paging"

        override suspend fun loadData(
            params: LoadParams<Int>,
        ): List<Photo> {
            val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
            val response = photoDataService.getPhotos(page, params.loadSize)
            return response.body() ?: throw NullResponseBodyException()
        }
    }
