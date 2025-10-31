package com.kimothorick.plashr.topics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.models.topics.Topic
import com.kimothorick.plashr.data.models.topics.TopicPhoto
import com.kimothorick.plashr.data.remote.TopicsDataService
import com.kimothorick.plashr.topics.domain.topicPhotos.TopicPhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Represents the UI state of a topic.
 *
 * This sealed class encapsulates the different states that the UI can be in
 * when displaying information about a topic, including loading, success, and error states.
 */
sealed class TopicUiState {
    object Loading : TopicUiState()

    data class Success(
        val topic: Topic,
    ) : TopicUiState()

    data class Error(
        val errorMessage: String,
    ) : TopicUiState()
}

/**
 * A [ViewModel] for the topic details screen.
 *
 * This ViewModel is responsible for fetching, managing, and exposing UI-related data for a specific
 * topic. It handles the logic for retrieving the topic's details and a paginated list of its photos,
 * exposing them through [StateFlow]s for the UI to observe.
 *
 * The ViewModel manages the following key data points:
 * - The UI state for the topic details, represented by [TopicUiState], which can be Loading,
 *   Success, or Error.
 * - A paginated flow of photos belonging to the topic, represented by [topicPhotosFlow].
 * - The refreshing state for pull-to-refresh actions.
 *
 * It uses dependency injection to get repositories and services required for data fetching and
 * error handling.
 *
 * @property topicsDataService Service to fetch details for a specific topic.
 * @property topicPhotosRepository Repository to get a paginated list of photos for a topic.
 * @property errorHandler Utility to convert exceptions into user-friendly error messages.
 * @property crashlytics Service for logging non-fatal exceptions.
 */

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TopicDetailsViewModel @Inject constructor(
    private val topicsDataService: TopicsDataService,
    private val topicPhotosRepository: TopicPhotosRepository,
    private val errorHandler: ErrorHandler,
    private val crashlytics: FirebaseCrashlytics,
) : ViewModel() {
    private val _isRefreshingAction = MutableStateFlow(false)
    val isRefreshingAction: StateFlow<Boolean> = _isRefreshingAction.asStateFlow()

    private val _topicUiState = MutableStateFlow<TopicUiState>(TopicUiState.Loading)
    val topicUiState: StateFlow<TopicUiState> = _topicUiState
    private val requestedTopicId = MutableStateFlow<String?>(null)
    private val displayedTopicId = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            requestedTopicId.filterNotNull().distinctUntilChanged().collectLatest { requestedTopicId ->
                fetchTopic(requestedTopicId)
            }
        }
    }

    fun setRefreshAction(
        isRefreshing: Boolean,
    ) {
        _isRefreshingAction.value = isRefreshing
    }

    /**
     * Sets the value of the topic ID.
     *
     * This function updates the internal `_topicId` MutableLiveData with the provided topic ID.
     *
     * @param requestedTopicId The new topic ID to be set.  Must be a non-null String.
     *
     * @see requestedTopicId
     */
    fun setTopicId(
        requestedTopicId: String,
    ) {
        this.requestedTopicId.value = requestedTopicId
    }

    /**
     * Fetches a topic by its ID and updates the `_topicUiState` accordingly.
     *
     * This function attempts to retrieve a topic from the `topicsDataService`.
     * It handles loading, success (with formatted publication date), and error states,
     * including cases of missing resources and network issues.
     *
     * @param requestedTopicId The ID of the topic to fetch.
     *
     * @see TopicUiState
     * @see topicsDataService
     * @see toFormattedDate
     */
    private suspend fun fetchTopic(
        requestedTopicId: String,
    ) {
        displayedTopicId.value = requestedTopicId
        _topicUiState.value = TopicUiState.Loading

        try {
            val topicResponse = topicsDataService.getTopic(id = requestedTopicId)
            if (topicResponse.isSuccessful) {
                topicResponse.body()?.let { topic ->
                    _topicUiState.value = TopicUiState.Success(topic)
                } ?: run {
                    val exception = HttpException(topicResponse)
                    crashlytics.recordException(exception)
                    _topicUiState.value = TopicUiState.Error(errorHandler.getErrorMessage(exception))
                }
            } else {
                val httpException = HttpException(topicResponse)
                crashlytics.recordException(httpException)
                _topicUiState.value = TopicUiState.Error(
                    errorMessage = errorHandler.getErrorMessage(httpException),
                )
            }
        } catch (exception: Exception) {
            crashlytics.recordException(exception)
            _topicUiState.value = TopicUiState.Error(errorHandler.getErrorMessage(exception))
        }
    }

    /**
     * Refreshes the currently displayed topic by re-fetching its data.
     *
     * This function reloads the topic data using the stored topic ID.
     * It does nothing if no topic ID is set.
     *
     * @see fetchTopic
     */
    fun refreshTopic() {
        viewModelScope.launch {
            setRefreshAction(true)
            try {
                requestedTopicId.value?.let {
                    fetchTopic(it)
                }
            } finally {
                setRefreshAction(false)
            }
        }
    }

    // Add a map to cache flows by topic ID
    private val topicPhotoFlowCache = mutableMapOf<String, Flow<PagingData<TopicPhoto>>>()

    val topicPhotosFlow: StateFlow<PagingData<TopicPhoto>> =
        requestedTopicId.filterNotNull().distinctUntilChanged().flatMapLatest { topicId ->
            topicPhotoFlowCache.getOrPut(topicId) {
                topicPhotosRepository.getTopicPhotos(topicId).cachedIn(viewModelScope).flowOn(Dispatchers.IO)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty(),
        )

    /**
     * Generates a user-friendly error message from a [Throwable].
     *
     * This function utilizes the [ErrorHandler] to convert a given exception or error
     * into a displayable string, suitable for showing to the user when a photo fails to load.
     *
     * @param cause The [Throwable] (exception or error) that occurred during photo loading.
     * @return A [String] containing the user-friendly error message.
     */
    fun generatePhotoLoadErrorMessage(
        cause: Throwable,
    ): String {
        return errorHandler.getErrorMessage(cause)
    }
}
