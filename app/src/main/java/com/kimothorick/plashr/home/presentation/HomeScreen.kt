package com.kimothorick.plashr.home.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowWidthSizeClass
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.home.presentation.components.PhotoListItem

/**
 * The Home screen.
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    Log.i("Home Screen", "Home Screen: Recreation")

    val photos = homeViewModel.getHomePhotos().collectAsLazyPagingItems()
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

    DisplayPhotos(photos = photos, columnCount = columnCount)

}

@Composable
fun DisplayPhotos(
    photos: LazyPagingItems<Photo>,
    columnCount: Int,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columnCount),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
   /*     items(count = photos.itemCount) { index ->

            if (index == 0) {
                Log.i("Home screen", "Photo: ${photos[0]} ")
            }
            PhotoContent(photos, index)

        }*/

        items(count = photos.itemCount) { index ->
            val item = photos[index]
            PhotoContent(photos, index)
        }

        photos.apply {
            when {
                loadState.refresh is LoadState.Loading -> {

                }
                loadState.append is LoadState.Loading -> {
                    Log.i("Network", "Page state: Loading more photos")
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
                loadState.refresh is LoadState.Error -> {

                }
                loadState.append is LoadState.Error -> {

                }
            }
        }

    }
    Log.i("Network", "photo count: ${photos.itemCount}")
}

@Composable
fun PhotoContent(
    photos: LazyPagingItems<Photo>,
    index: Int,
) {
    photos[index]?.let { photo ->
        PhotoListItem(
            photo = photo
        )
    }
}


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
/*when (photos.loadState.refresh) {
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
}*/


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