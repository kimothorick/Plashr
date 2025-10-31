package com.kimothorick.plashr.collections.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kimothorick.plashr.R
import com.kimothorick.plashr.collections.presentation.components.CollectionCard
import com.kimothorick.plashr.collections.presentation.components.CollectionCardShimmer
import com.kimothorick.plashr.collections.presentation.components.CollectionItemData
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.Collection
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.ErrorView

/**
 * A Composable function that acts as the main entry point for the Collections screen.
 * It is responsible for observing the collections data from the [CollectionsScreenViewModel]
 * and passing it down to the content composable.
 *
 * @param collectionsViewModel The ViewModel that holds the business logic and state for the collections screen.
 * @param modifier The [Modifier] to be applied to the screen.
 * @param onCollectionClicked A callback lambda that is invoked when a collection is clicked,
 *                            providing the ID of the selected collection.
 */
@Composable
fun CollectionsScreen(
    collectionsViewModel: CollectionsScreenViewModel,
    modifier: Modifier = Modifier,
    onCollectionClicked: (String) -> Unit,
) {
    val collections = collectionsViewModel.collectionsFlow.collectAsLazyPagingItems()

    CollectionsScreenContent(
        viewModel = collectionsViewModel, // Pass ViewModel here
        collections = collections,
        modifier = modifier,
        onCollectionSelected = onCollectionClicked,
    )
}

/**
 * The main content of the collections screen.
 *
 * This composable is responsible for displaying a grid of collections with support for pull-to-refresh,
 * loading states, error handling, and pagination. It uses a `LazyVerticalGrid` to efficiently display
 * the collection items.
 *
 * It handles the different `LoadState` from the `LazyPagingItems`:
 * - `LoadState.Loading`: Shows a shimmer effect for initial loading.
 * - `LoadState.NotLoading`: Displays the list of collections.
 * - `LoadState.Error`: Shows an error message with a retry button.
 *
 * @param viewModel The view model for the collections screen, used to generate error messages.
 * @param modifier The modifier to be applied to the root composable.
 * @param collections The paginated list of collections to display.
 * @param onCollectionSelected A callback invoked when a collection card is clicked, providing the collection's ID.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CollectionsScreenContent(
    viewModel: CollectionsScreenViewModel,
    modifier: Modifier = Modifier,
    collections: LazyPagingItems<Collection>,
    onCollectionSelected: (String) -> Unit,
) {
    val refreshState = collections.loadState.refresh
    val isScrollingEnabled = refreshState is LoadState.NotLoading
    val pullRefreshState = rememberPullToRefreshState()
    val hapticFeedback = LocalHapticFeedback.current
    val isPullToRefreshIndicatorVisible = refreshState !is LoadState.Loading

    val refreshCollections: () -> Unit = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
        collections.refresh()
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = {
            refreshCollections()
        },
        state = pullRefreshState,
        indicator = {
            LoadingIndicator(
                state = pullRefreshState,
                isRefreshing = isPullToRefreshIndicatorVisible,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        },
    ) {
        when (refreshState) {
            is LoadState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState(),
                        ),
                    verticalArrangement = Arrangement.Center,
                ) {
                    ErrorView(
                        errorTitle = null,
                        errorMessage = viewModel.generateErrorMessage(refreshState.error),
                        onRetry = {
                            collections.retry()
                        },
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = Constants.LayoutValues.COLLECTION_CARD_ADAPTIVE_MIN_SIZE),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    userScrollEnabled = isScrollingEnabled,
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        CollectionsHeader()
                    }

                    when (refreshState) {
                        is LoadState.Loading -> {
                            items(Constants.LayoutValues.INITIAL_SHIMMER_COUNT) {
                                CollectionCardShimmer(modifier = Modifier.padding(8.dp))
                            }
                        }

                        is LoadState.NotLoading -> {
                            items(
                                count = collections.itemCount,
                                key = { index ->
                                    val collection = collections.peek(index)
                                    "collection_${collection?.id ?: "no_id"}_$index"
                                },
                            ) { index ->
                                collections[index]?.let { collection ->
                                    CollectionCard(
                                        collection = CollectionItemData.Collection(collection),
                                        onCollectionClicked = { onCollectionSelected(collection.id) },
                                    )
                                }
                            }
                            if (collections.loadState.append.endOfPaginationReached) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    EndOfPagingComponent()
                                }
                            }
                        }

                        else -> {}
                    }
                    // Handle additional load states for collections
                    handleLoadStates(
                        pagingItems = collections,
                        getErrorMessage = {
                            viewModel.generateErrorMessage(it)
                        },
                    )
                }
            }
        }
    }
}

/**
 * A composable function that displays the header for the collections screen.
 * It shows the title "Collections".
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
private fun CollectionsHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.collections),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
