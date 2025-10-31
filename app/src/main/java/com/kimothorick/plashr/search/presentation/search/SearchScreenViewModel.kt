package com.kimothorick.plashr.search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.models.collection.SearchCollectionsResponse
import com.kimothorick.plashr.data.models.photo.SearchPhotosResponse
import com.kimothorick.plashr.data.models.user.SearchUsersResponse
import com.kimothorick.plashr.search.domain.SearchRepository
import com.kimothorick.plashr.search.presentation.components.FilterOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

/**
 * ViewModel for the Search screen.
 *
 * This ViewModel is responsible for managing the state and business logic of the search feature.
 * It receives search queries and filter options, triggers searches against the [SearchRepository],
 * and exposes the search results (photos, collections, and users) as reactive [Flow]s of [PagingData].
 *
 * @property searchRepository The repository responsible for fetching search data from the API.
 * @property errorHandler A utility for handling errors that may occur during data fetching.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    val errorHandler: ErrorHandler,
) : ViewModel() {
    private val searchQuery = MutableStateFlow<Pair<String, FilterOptions>?>(null)

    val photos: Flow<PagingData<SearchPhotosResponse.Result>> = searchQuery.filterNotNull().flatMapLatest { (query, filters) ->
        searchRepository.searchPhotos(query, filters)
    }.cachedIn(viewModelScope)

    val collections: Flow<PagingData<SearchCollectionsResponse.Result>> = searchQuery.filterNotNull().flatMapLatest { (query, _) ->
        searchRepository.searchCollections(query)
    }.cachedIn(viewModelScope)

    val users: Flow<PagingData<SearchUsersResponse.Result>> = searchQuery.filterNotNull().flatMapLatest { (query, _) ->
        searchRepository.searchUsers(query)
    }.cachedIn(viewModelScope)

    /**
     * Executes a new search by updating the [searchQuery] state flow.
     *
     * This function triggers the reactive flows ([photos], [collections], [users]) to fetch
     * new data from the repository based on the provided query and filters. The search
     * is only performed if the query string is not blank.
     *
     * @param query The search term entered by the user.
     * @param filters The selected filter options to apply to the photo search.
     */
    fun executeSearch(
        query: String,
        filters: FilterOptions,
    ) {
        if (query.isNotBlank()) {
            searchQuery.value = Pair(query, filters)
        }
    }
}
