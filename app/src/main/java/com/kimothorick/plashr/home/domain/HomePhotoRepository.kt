package com.kimothorick.plashr.home.domain

import androidx.paging.PagingData
import com.kimothorick.plashr.data.models.photo.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomePhotoRepository @Inject constructor(private val photoPagingSource: PhotosPagingSource ) {

}