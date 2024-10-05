package com.kimothorick.plashr.home.domain

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.remote.PhotoDataService
import okio.IOException
import javax.inject.Inject

// Unsplash page API is 1-based.
private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class PhotosPagingSource @Inject constructor(private val photoDataService: PhotoDataService) :
    PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
            val response = photoDataService.getPhotos(page, params.loadSize)

            Log.i("Network", "load: getting photos")

            LoadResult.Page(
                data = response.body()!!,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.body()!!.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let {
            anchorposition->
            state.closestPageToPosition(anchorposition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorposition)?.nextKey?.minus(1)
        }
    }

}