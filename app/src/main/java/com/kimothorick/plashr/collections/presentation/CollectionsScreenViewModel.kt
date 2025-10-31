package com.kimothorick.plashr.collections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kimothorick.plashr.collections.domain.CollectionsRepository
import com.kimothorick.plashr.common.paging.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the Collections screen.
 *
 * This ViewModel is responsible for providing data to the Collections UI and handling
 * business logic related to it. It exposes a flow of paginated collection data and
 * provides a utility for generating error messages.
 *
 * @param collectionsRepository The repository for fetching collection data.
 * @param errorHandler The utility for creating user-friendly error messages from exceptions.
 */
@HiltViewModel
class CollectionsScreenViewModel @Inject constructor(
    collectionsRepository: CollectionsRepository,
    private val errorHandler: ErrorHandler,
) : ViewModel() {
    val collectionsFlow = collectionsRepository.getCollections()
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty(),
        )

    /**
     * Generates a user-friendly error message from a [Throwable].
     *
     * This function utilizes an [ErrorHandler] to convert a given exception
     * into a human-readable string that can be displayed to the user.
     *
     * @param error The [Throwable] caught during an operation.
     * @return A [String] containing the formatted error message.
     */
    fun generateErrorMessage(
        error: Throwable,
    ): String {
        return errorHandler.getErrorMessage(error)
    }
}
