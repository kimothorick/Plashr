package com.kimothorick.plashr.home.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.photo.sharedLink
import com.kimothorick.plashr.home.presentation.components.PhotoCardItem
import com.kimothorick.plashr.home.presentation.components.PhotoListItem
import com.kimothorick.plashr.settings.domain.SettingsDataStore
import com.kimothorick.plashr.settings.presentation.SettingsViewModel
import java.util.UUID
import javax.inject.Inject

/**
 * The Home screen.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    Log.i("Home Screen", "Home Screen: Recreation")

    val photos = homeViewModel.photosFlow.collectAsLazyPagingItems()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val staggeredGridState =
        rememberSaveable(saver = LazyStaggeredGridState.Saver, key = "home_screen_grid_state") {
            Log.d("Home Screen", "Creating new LazyStaggeredGridState")
            LazyStaggeredGridState()
        }
    //val staggeredGridState = rememberLazyStaggeredGridState()
    Log.i("Home Screen", "Home Screen: Recreation")


    val columnCount = when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT, WindowWidthSizeClass.MEDIUM -> 2
        WindowWidthSizeClass.EXPANDED -> 3
        else -> 1
    }

    PhotoGrid(photos = photos, columnCount = columnCount)

}

@Composable
fun PhotoGrid(
    photos: LazyPagingItems<Photo>,
    columnCount: Int,
) {

    /*when (photos.loadState.refresh) {
        LoadState.Loading -> {
            //TODO implement loading state
        }

        is LoadState.Error -> {
            //TODO implement error state
        }

        else -> {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(count = photos.itemCount, key = {index ->
                    photos[index]!!.id
                }) {index ->
                    if (index == 0) {
                        Log.i("Home screen", "Photo: ${photos[0]} ")
                    }
                    PhotoContent(photos, index)
                }

                if (photos.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }

                }
            }

        }
    }*/
    when (photos.loadState.refresh) {
        LoadState.Loading -> {
            //TODO implement loading state
        }

        is LoadState.Error -> {
            //TODO implement error state
        }

        else -> {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(columnCount),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(count = photos.itemCount, key = {index ->
                    photos[index]!!.id
                }) {index ->
                    if (index == 0) {
                        Log.i("Home screen", "Photo: ${photos[0]} ")
                    }
                    PhotoContent(photos, index)
                }

                if (photos.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }

                }
            }

        }
    }

    /*LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columnCount),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(count = photos.itemCount, key = {index ->
            photos[index].id
        }) {index ->
            if (index == 0) {
                Log.i("Home screen", "Photo: ${photos[0]} ")
            }
            PhotoContent(photos, index)
        }

        if (photos.loadState.append is LoadState.Loading) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

        }
    }*/


}

@Composable
fun PhotoContent(
    photos: LazyPagingItems<Photo>,
    index: Int,
) {
    photos[index]?.let {photo ->
        PhotoListItem(
            photo = photo
        )
    }
}

