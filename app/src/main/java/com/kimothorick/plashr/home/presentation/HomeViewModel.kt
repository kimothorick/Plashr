package com.kimothorick.plashr.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kimothorick.plashr.auth.AuthRepository
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.home.domain.HomePhotoRepository
import com.kimothorick.plashr.topics.domain.topics.TopicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the home screen.
 *
 * This ViewModel is responsible for providing data to the home UI and handling related business logic.
 * It exposes flows of paginated data for photos and topics, which are fetched from their respective
 * repositories. The data fetching is triggered by the app's authorization status.
 *
 * @param homePhotoRepository The repository for fetching home photos.
 * @param topicsRepository The repository for fetching topics.
 * @param authRepository The repository for managing authentication status.
 * @param errorHandler A utility for generating user-friendly error messages from exceptions.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    homePhotoRepository: HomePhotoRepository,
    topicsRepository: TopicsRepository,
    authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) : ViewModel() {
    val photosFlow: StateFlow<PagingData<Photo>> = authRepository.isAppAuthorized.flatMapLatest { _ ->
        homePhotoRepository.getPhotos()
    }.cachedIn(viewModelScope).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PagingData.empty(),
    )

    val topicsFlow: StateFlow<PagingData<Topic>> = authRepository.isAppAuthorized.flatMapLatest { _ ->
        topicsRepository.getTopics()
    }.cachedIn(viewModelScope).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PagingData.empty(),
    )

    /**
     * Generates a user-friendly error message from a given [Throwable].
     *
     * This function utilizes an [ErrorHandler] to process the exception and produce
     * a localized, understandable message suitable for display in the UI.
     *
     * @param error The [Throwable] (exception) that occurred.
     * @return A [String] containing the formatted error message.
     */
    fun generateErrorMessage(
        error: Throwable,
    ): String {
        return errorHandler.getErrorMessage(error)
    }
}
