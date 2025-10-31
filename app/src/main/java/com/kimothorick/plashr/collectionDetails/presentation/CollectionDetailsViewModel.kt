package com.kimothorick.plashr.collectionDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.collectionDetails.domain.CollectionsPhotosRepository
import com.kimothorick.plashr.collections.presentation.components.ActionType
import com.kimothorick.plashr.collections.presentation.components.EditCollectionState
import com.kimothorick.plashr.common.paging.ErrorHandler
import com.kimothorick.plashr.data.models.collection.Collection
import com.kimothorick.plashr.data.models.collection.CollectionPhoto
import com.kimothorick.plashr.data.remote.CollectionsDataService
import com.kimothorick.plashr.utils.toFormattedDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
 * Represents the different states for the Collection Details screen UI.
 * This sealed class is used to manage and display the UI based on the data fetching status.
 */
sealed class CollectionUiState {
    /**
     * Represents the loading state of the collection details UI, indicating that data is currently being fetched.
     */
    object Loading : CollectionUiState()

    /**
     * Represents the successful state of the collection details screen.
     * This state is emitted when the collection data has been successfully fetched.
     *
     * @param collection The [Collection] object containing details about the collection.
     * @param formattedDate The publication date of the collection, formatted as a human-readable string.
     */
    data class Success(
        val collection: Collection,
        val formattedDate: String,
    ) : CollectionUiState()

    /**
     * Represents the error state of the collection details UI.
     * This state is used when fetching collection details fails.
     *
     * @param errorMessage A string resource or message describing the error that occurred.
     */
    data class Error(
        val errorMessage: String,
    ) : CollectionUiState()
}

/**
 * Represents one-time UI events that are sent from the ViewModel to the UI.
 * These events are intended to be consumed only once, for actions like navigation
 * or showing a toast message.
 */
sealed class UiEvent {
    data object NavigateBack : UiEvent()
}

/**
 * ViewModel for the Collection Details screen.
 *
 * This ViewModel manages the UI state and business logic for displaying the details of a single
 * collection. It is responsible for:
 * - Fetching the collection's metadata (title, description, etc.).
 * - Fetching a paginated list of photos belonging to the collection.
 * - Handling user actions such as updating or deleting the collection.
 * - Managing UI states for loading, success, error, and refresh operations.
 *
 * @property collectionsDataService Service for fetching, updating, and deleting collection data.
 * @property collectionsPhotosRepository Repository for fetching the photos within a collection.
 * @property errorHandler Utility to convert exceptions into user-friendly error messages.
 * @property crashlytics Service for logging non-fatal exceptions to Firebase Crashlytics.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    private val collectionsDataService: CollectionsDataService,
    private val collectionsPhotosRepository: CollectionsPhotosRepository,
    private val errorHandler: ErrorHandler,
    private val crashlytics: FirebaseCrashlytics,
) : ViewModel() {
    private val _collectionUiState = MutableStateFlow<CollectionUiState>(CollectionUiState.Loading)
    val collectionUiState: StateFlow<CollectionUiState> = _collectionUiState

    private val collectionId = MutableStateFlow<String?>(null)
    private val currentCollectionId = MutableStateFlow<String?>(null)

    private val _isRefreshingAction = MutableStateFlow(false)
    val isRefreshingAction: StateFlow<Boolean> = _isRefreshingAction.asStateFlow()

    private val _showEditCollectionSheet = MutableStateFlow(false)
    val showEditCollectionSheet: StateFlow<Boolean> = _showEditCollectionSheet.asStateFlow()

    private val _editCollectionState = MutableStateFlow<EditCollectionState>(EditCollectionState.Idle)
    val editCollectionState: StateFlow<EditCollectionState> = _editCollectionState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()

    val eventFlow = _eventFlow.asSharedFlow()

    /**
     * Updates the state of the edit collection operation.
     *
     * This function is used to modify the `_editCollectionState` flow, which reflects the
     * current status of an edit or delete operation (e.g., Idle, InProgress, Success, Failed).
     * The UI observes this state to provide feedback to the user.
     *
     * @param state The new [EditCollectionState] to be set.
     */
    fun updateEditCollectionState(
        state: EditCollectionState,
    ) {
        _editCollectionState.value = state
    }

    /**
     * Handles the click event for editing a collection.
     *
     * This function is triggered when the user initiates an action to edit the current collection.
     * It checks if the collection data has been successfully loaded ([CollectionUiState.Success]).
     * If so, it sets the `_showEditCollectionSheet` state to `true`, which signals the UI
     * to display the bottom sheet for editing the collection's details.
     */
    fun onEditCollectionClicked() {
        if (collectionUiState.value is CollectionUiState.Success) {
            _showEditCollectionSheet.value = true
        }
    }

    /**
     * Handles the dismissal of the "Edit Collection" bottom sheet.
     *
     * This function is called when the user dismisses the edit sheet, either by swiping it down
     * or through an explicit close action. It resets the related UI states by setting
     * `_showEditCollectionSheet` to `false` to hide the sheet and `_editCollectionState` to `Idle`
     * to clear any ongoing edit/update/delete status.
     */
    fun onEditCollectionDismissed() {
        _showEditCollectionSheet.value = false
        _editCollectionState.value = EditCollectionState.Idle
    }

    /**
     * Updates the state of the pull-to-refresh action.
     *
     * This function is called to manually set the refreshing status of the UI,
     * typically used to show or hide a loading indicator when data is being refreshed.
     *
     * @param isRefreshing A boolean indicating whether the refresh action is active (`true`) or not (`false`).
     */
    fun setRefreshingAction(
        isRefreshing: Boolean,
    ) {
        _isRefreshingAction.value = isRefreshing
    }

    init {
        viewModelScope.launch {
            collectionId.filterNotNull().distinctUntilChanged().collectLatest { collectionId ->
                fetchCollection(collectionId)
            }
        }
    }

    /**
     * Sets the ID of the collection to be displayed.
     *
     * This function updates the `collectionId` StateFlow with the provided ID.
     * An observer on this StateFlow will then trigger the `fetchCollection` function
     * to load the details for the new collection ID.
     *
     * @param collectionId The unique identifier for the collection.
     */
    fun setCollectionId(
        collectionId: String,
    ) {
        this.collectionId.value = collectionId
    }

    /**
     * Fetches the details for a specific collection by its ID.
     *
     * This suspend function initiates an API call to retrieve collection data. It updates the
     * `_collectionUiState` to [CollectionUiState.Loading] at the start. On a successful API
     * response, it transitions the state to [CollectionUiState.Success] with the fetched
     * collection data. In case of an API error, a network failure, or any other exception,
     * it updates the state to [CollectionUiState.Error] with an appropriate error message.
     *
     * @param collectionId The unique identifier of the collection to fetch.
     */
    private suspend fun fetchCollection(
        collectionId: String,
    ) {
        currentCollectionId.value = collectionId
        _collectionUiState.value = CollectionUiState.Loading

        try {
            val result = collectionsDataService.getCollection(id = collectionId)
            if (result.isSuccessful) {
                result.body()?.let { collection ->
                    _collectionUiState.value = CollectionUiState.Success(
                        collection = collection,
                        formattedDate = collection.publishedAt?.toFormattedDate() ?: "",
                    )
                } ?: run {
                    val exception = IllegalStateException("Successful response with null body")
                    crashlytics.recordException(exception)
                    _collectionUiState.value = CollectionUiState.Error(
                        errorMessage = errorHandler.getErrorMessage(exception),
                    )
                }
            } else {
                val httpException = HttpException(result)
                crashlytics.recordException(httpException)
                _collectionUiState.value = CollectionUiState.Error(errorMessage = errorHandler.getErrorMessage(httpException))
            }
        } catch (exception: Exception) {
            crashlytics.recordException(exception)
            _collectionUiState.value = CollectionUiState.Error(
                errorMessage = errorHandler.getErrorMessage(exception),
            )
        }
    }

    /**
     * Deletes the current collection.
     *
     * This function handles the process of deleting a collection. It first updates the UI state
     * to indicate that the deletion is in progress. It then calls the `collectionsDataService`
     * to perform the deletion.
     * - On success, it updates the state to [EditCollectionState.Success], waits for a short
     *   delay, and then emits a [UiEvent.NavigateBack] event to navigate away from the screen.
     * - On failure (either an unsuccessful API response or an exception), it updates the state
     *   to [EditCollectionState.Failed] with an appropriate error message.
     */
    suspend fun deleteCollection() {
        val collectionId = collectionId.value ?: return
        updateEditCollectionState(EditCollectionState.InProgress(ActionType.DELETE))
        try {
            val result = collectionsDataService.deleteCollection(id = collectionId)
            if (result.isSuccessful) {
                updateEditCollectionState(
                    state = EditCollectionState.Success(ActionType.DELETE),
                )
                delay(1500L)
                _eventFlow.emit(UiEvent.NavigateBack)
            } else {
                val httpException = HttpException(result)
                crashlytics.recordException(httpException)
                updateEditCollectionState(
                    state = EditCollectionState.Failed(
                        actionType = ActionType.DELETE,
                        errorMessage = errorHandler.getErrorMessage(httpException),
                    ),
                )
            }
        } catch (exception: Exception) {
            crashlytics.recordException(exception)
            updateEditCollectionState(
                state = EditCollectionState.Failed(
                    actionType = ActionType.DELETE,
                    errorMessage = errorHandler.getErrorMessage(exception),
                ),
            )
        }
    }

    /**
     * Updates the details of the current collection.
     *
     * This function initiates an API call to update the collection's title, description, and privacy status.
     * It follows these steps:
     * 1. Sets the UI state to `EditCollectionState.InProgress` to show a loading indicator for the update action.
     * 2. Calls the `collectionsDataService.updateCollection` with the provided new details.
     * 3. On a successful API response:
     *    - Updates the `_collectionUiState` with the new collection data.
     *    - Sets the edit state to `EditCollectionState.Success`.
     *    - After a short delay, dismisses the edit sheet.
     * 4. If the API call fails or an exception occurs:
     *    - Sets the edit state to `EditCollectionState.Failed` and provides a user-friendly error message.
     *
     * @param title The new title for the collection.
     * @param description The new description for the collection (can be null).
     * @param isPrivate The new privacy setting for the collection.
     */
    suspend fun updateCollection(
        title: String,
        description: String?,
        isPrivate: Boolean,
    ) {
        val collectionId = collectionId.value ?: return
        updateEditCollectionState(EditCollectionState.InProgress(ActionType.UPDATE))
        try {
            val result = collectionsDataService.updateCollection(
                id = collectionId,
                title = title,
                description = description,
                isPrivate = isPrivate,
            )
            if (result.isSuccessful) {
                result.body()?.let { updatedCollection ->
                    val currentState = _collectionUiState.value
                    if (currentState is CollectionUiState.Success) {
                        _collectionUiState.value = currentState.copy(
                            collection = updatedCollection,
                            formattedDate = updatedCollection.publishedAt?.toFormattedDate() ?: "",
                        )
                    }
                    updateEditCollectionState(
                        state = EditCollectionState.Success(actionType = ActionType.UPDATE),
                    )

                    delay(1500L)
                    onEditCollectionDismissed()
                } ?: run {
                    val exception = IllegalStateException("Successful response with null body on update")
                    crashlytics.recordException(exception)
                    updateEditCollectionState(
                        state = EditCollectionState.Failed(
                            actionType = ActionType.UPDATE,
                            errorMessage = errorHandler.getErrorMessage(exception),
                        ),
                    )
                }
            } else {
                val httpException = HttpException(result)
                crashlytics.recordException(httpException)
                updateEditCollectionState(
                    state = EditCollectionState.Failed(
                        actionType = ActionType.UPDATE,
                        errorMessage = errorHandler.getErrorMessage(httpException),
                    ),
                )
            }
        } catch (exception: Exception) {
            crashlytics.recordException(exception)
            updateEditCollectionState(
                state = EditCollectionState.Failed(
                    actionType = ActionType.UPDATE,
                    errorMessage = errorHandler.getErrorMessage(exception),
                ),
            )
        }
    }

    /**
     * Refreshes the details of the current collection.
     *
     * This function is typically triggered by a pull-to-refresh action in the UI. It sets the
     * refreshing state to true, re-fetches the collection data using the current `collectionId`,
     * and then sets the refreshing state back to false, regardless of whether the fetch was
     * successful or not.
     */
    fun refreshCollection() {
        viewModelScope.launch {
            setRefreshingAction(true)
            try {
                collectionId.value?.let {
                    fetchCollection(collectionId = it)
                }
            } finally {
                setRefreshingAction(false)
            }
        }
    }

    private val collectionPhotoFlowCache = mutableMapOf<String, Flow<PagingData<CollectionPhoto>>>()

    val collectionPhotosFlow: Flow<PagingData<CollectionPhoto>> =
        collectionId.filterNotNull().distinctUntilChanged().flatMapLatest { collectionId ->
            collectionPhotoFlowCache.getOrPut(key = collectionId) {
                collectionsPhotosRepository.getTopicPhotos(topicID = collectionId).cachedIn(viewModelScope).flowOn(Dispatchers.IO)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty(),
        )

    /**
     * Generates a user-friendly error message for photo loading failures.
     *
     * This function takes a [Throwable] instance, which represents the error that occurred
     * while loading photos, and uses the [ErrorHandler] to convert it into a human-readable
     * string. This message can then be displayed in the UI to inform the user about the
     * loading error.
     *
     * @param error The [Throwable] caught during the photo loading process.
     * @return A [String] containing the user-friendly error message.
     */
    fun generatePhotoLoadErrorMessage(
        error: Throwable,
    ): String {
        return errorHandler.getErrorMessage(error)
    }
}
