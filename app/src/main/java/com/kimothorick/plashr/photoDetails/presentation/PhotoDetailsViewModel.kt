package com.kimothorick.plashr.photoDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimothorick.plashr.auth.AuthRepository
import com.kimothorick.plashr.data.models.collection.CurrentUserCollection
import com.kimothorick.plashr.photoDetails.domain.PhotoRepository
import com.kimothorick.plashr.photoDetails.domain.PhotoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the photo details screen.
 *
 * This ViewModel is responsible for fetching and managing the state of a single photo,
 * including its details, like status, and user collections. It interacts with repositories
 * to get data and exposes it to the UI via `StateFlow`s.
 *
 * @property photoRepository The repository for fetching photo-related data.
 * @property authRepository The repository for handling authentication state.
 */
@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    authRepository: AuthRepository,
) : ViewModel() {
    val photoUiState: StateFlow<PhotoUiState> = photoRepository.photoUiState
    private val photoId = MutableStateFlow<String?>(null)

    private val _isLikedByCurrentUser = MutableStateFlow<Boolean?>(null)
    val isLikedByCurrentUser: StateFlow<Boolean?> = _isLikedByCurrentUser.asStateFlow()

    private val _currentUserCollections = MutableStateFlow<List<CurrentUserCollection?>?>(emptyList())
    val currentUserCollections: StateFlow<List<CurrentUserCollection?>?> = _currentUserCollections.asStateFlow()

    private val _isRefreshingAction = MutableStateFlow(false)
    val isRefreshingAction: StateFlow<Boolean> = _isRefreshingAction.asStateFlow()

    /**
     * Sets the refreshing state for the UI.
     *
     * This is used to indicate whether a data refresh operation, like pulling to refresh,
     * is currently in progress.
     *
     * @param isRefreshing True if the refresh action is active, false otherwise.
     */
    fun setRefreshingAction(
        isRefreshing: Boolean,
    ) {
        _isRefreshingAction.value = isRefreshing
    }

    /**
     * Updates the state of the collections the current user has added the photo to.
     *
     * This is used to reflect changes, such as when a photo is added to or removed from a collection,
     * without needing to re-fetch all photo details from the network.
     *
     * @param newCollections The updated list of collections for the current user.
     */
    fun setCurrentUserCollections(
        newCollections: List<CurrentUserCollection?>?,
    ) {
        _currentUserCollections.value = newCollections?.toList()
    }

    init {
        viewModelScope.launch {
            photoId.filterNotNull().collectLatest { photoId ->
                fetchPhoto(photoId)
            }
        }

        viewModelScope.launch {
            photoUiState.collect { state ->
                if (state is PhotoUiState.Success) {
                    if (_isLikedByCurrentUser.value != state.photo.likedByUser) {
                        _isLikedByCurrentUser.value = state.photo.likedByUser
                    }
                } else if (state is PhotoUiState.Loading || state is PhotoUiState.Error) {
                    _isLikedByCurrentUser.value = null
                }
            }
        }

        viewModelScope.launch {
            authRepository.isAppAuthorized.collectLatest { isAuthorized ->
                if (isAuthorized) {
                    photoId.value?.let { photoId ->
                        fetchPhoto(photoId = photoId)
                    }
                }
            }
        }
    }

    /**
     * Manually sets the like status of the photo for the current user.
     * This is useful for instantly updating the UI after a like/unlike action,
     * before the full state is refreshed from the repository.
     *
     * @param isLiked True if the photo is liked by the current user, false otherwise.
     */
    fun setIsLikedByCurrentUser(
        isLiked: Boolean,
    ) {
        _isLikedByCurrentUser.value = isLiked
    }

    /**
     * Sets the ID of the photo to be displayed.
     *
     * This function updates the internal `photoId` StateFlow, which in turn triggers
     * a call to fetch the details for the new photo ID.
     *
     * @param photoId The unique identifier of the photo.
     */
    fun setPhotoId(
        photoId: String,
    ) {
        this.photoId.value = photoId
    }

    /**
     * Triggers a manual refresh of the photo details.
     *
     * This function initiates a coroutine within the `viewModelScope` to re-fetch the
     * details for the current photo. It sets a refreshing indicator to `true` at the
     * start of the operation and ensures it's set back to `false` in a `finally` block,
     * guaranteeing the indicator is reset even if the fetch operation fails. It uses the
     * current `photoId` to call the `fetchPhoto` function.
     */
    fun refreshPhotoDetails() {
        viewModelScope.launch {
            setRefreshingAction(true)
            try {
                photoId.value?.let { photoId ->
                    fetchPhoto(photoId = photoId)
                }
            } finally {
                setRefreshingAction(false)
            }
        }
    }

    /**
     * Fetches the details for a specific photo and updates the UI state.
     *
     * This function launches a coroutine to call the `photoRepository` to get the photo data.
     * It collects the resulting `PhotoUiState` flow and updates the shared `photoUiState` in the repository.
     * If the fetch is successful, it also updates the local list of collections the current user has
     * added this photo to.
     *
     * @param photoId The unique identifier of the photo to fetch.
     */
    fun fetchPhoto(
        photoId: String,
    ) {
        viewModelScope.launch {
            photoRepository.getPhoto(photoId).collectLatest { photoUiState ->
                photoRepository.setPhotoUiState(photoUiState)
                if (photoUiState is PhotoUiState.Success) {
                    setCurrentUserCollections(photoUiState.photo.currentUserCollections)
                }
            }
        }
    }
}
