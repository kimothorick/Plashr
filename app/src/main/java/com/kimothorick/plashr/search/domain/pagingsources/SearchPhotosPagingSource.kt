package com.kimothorick.plashr.search.domain.pagingsources

import androidx.paging.PagingSource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.common.paging.BasePagingSource
import com.kimothorick.plashr.common.paging.NullResponseBodyException
import com.kimothorick.plashr.data.models.photo.SearchPhotosResponse
import com.kimothorick.plashr.data.remote.SearchDataService
import com.kimothorick.plashr.search.presentation.components.ColorFilter
import com.kimothorick.plashr.search.presentation.components.ContentFilter
import com.kimothorick.plashr.search.presentation.components.FilterOptions
import com.kimothorick.plashr.search.presentation.components.OrderBy
import com.kimothorick.plashr.search.presentation.components.Orientation
import javax.inject.Inject

/**
 * A [PagingSource] that loads paginated data for photo search results from the Unsplash API.
 *
 * This class is responsible for fetching pages of photos based on a search query and optional
 * filters. It handles the logic for loading subsequent pages as the user scrolls.
 *
 * @param service The [SearchDataService] instance used to make API calls to the search endpoint.
 * @param query The search term entered by the user.
 * @param filters A [FilterOptions] object containing user-selected filters such as order,
 * @param crashlytics An instance of [FirebaseCrashlytics] for logging errors.
 * color, and orientation to be applied to the search query.
 */
class SearchPhotosPagingSource @Inject constructor(
    private val service: SearchDataService,
    private val query: String,
    private val filters: FilterOptions,
    crashlytics: FirebaseCrashlytics,
) : BasePagingSource<SearchPhotosResponse.Result>(crashlytics = crashlytics) {
    override val logKeyName: String = "Search Photos Paging"

    override suspend fun loadData(
        params: LoadParams<Int>,
    ): List<SearchPhotosResponse.Result> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        val response = service.searchPhotos(
            query = query,
            page = page,
            perPage = params.loadSize,
            orderBy = filters.orderBy.toApiString(),
            contentFilter = filters.contentFilter.toApiString(),
            color = filters.colorFilter?.toApiString(),
            orientation = filters.orientation?.toApiString(),
        )

        return response.body()?.results?.filterNotNull() ?: throw NullResponseBodyException()
    }

    // Helper functions to convert enum filters to the string values the API expects.
    private fun OrderBy.toApiString(): String = this.name.lowercase()

    private fun ContentFilter.toApiString(): String = this.name.lowercase()

    private fun Orientation.toApiString(): String = this.name.lowercase()

    private fun ColorFilter.toApiString(): String {
        return when (this) {
            ColorFilter.BlackAndWhite -> "black_and_white"
            else -> this.name.lowercase()
        }
    }
}
