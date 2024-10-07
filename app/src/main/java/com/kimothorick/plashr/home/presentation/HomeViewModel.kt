package com.kimothorick.plashr.home.presentation

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.home.domain.HomePhotoRepository
import com.kimothorick.plashr.home.domain.PhotosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    //photoPagingSource: PhotosPagingSource,
    private val homePhotoRepository: HomePhotoRepository
) : ViewModel() {

    /*val PAGE_SIZE = 20
    val photosFlow = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
        ),
        pagingSourceFactory = {photoPagingSource}).flow.cachedIn(viewModelScope)*/


    fun getHomePhotos(): Flow<PagingData<Photo>> =
        homePhotoRepository.getPhotos().cachedIn(viewModelScope)


}