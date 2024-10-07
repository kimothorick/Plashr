package com.kimothorick.plashr.home.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kimothorick.plashr.data.remote.PhotoDataService
import javax.inject.Inject

class HomePhotoRepository @Inject constructor(
    private val apiService: PhotoDataService
) {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    fun getPhotos() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            prefetchDistance = 4,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            PhotosPagingSource(apiService)
        }
    ).flow
}