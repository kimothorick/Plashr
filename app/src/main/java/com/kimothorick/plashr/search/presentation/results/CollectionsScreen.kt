package com.kimothorick.plashr.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kimothorick.plashr.R
import com.kimothorick.plashr.collections.presentation.components.CollectionCard
import com.kimothorick.plashr.collections.presentation.components.CollectionItemData
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.SearchCollectionsResponse
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EmptyStateComponent
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.ErrorView

/**
 * A composable screen that displays a grid of collections.
 *
 * This screen handles different loading states for paged collection data:
 * - **Loading:** Shows a shimmer effect.
 * - **Error:** Displays an error message with a retry button.
 * - **Empty:** Shows a message indicating no collections were found.
 * - **Success:** Displays a `LazyVerticalGrid` of `CollectionCard`s.
 *
 * It also manages pagination states, showing loading indicators for appended items and a
 * message when the end of the list is reached.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param collections The `LazyPagingItems` containing the collection data to display.
 * @param gridState The state object for the `LazyVerticalGrid` to control and observe scroll position.
 * @param formatErrorMessage A lambda function to convert a `Throwable` into a human-readable error string.
 * @param onCollectionClicked A callback function invoked when a collection card is clicked, providing the collection's ID.
 * @param onRetry A callback function invoked when the user clicks the retry button in the error view.
 */
@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    collections: LazyPagingItems<SearchCollectionsResponse.Result>,
    gridState: LazyGridState,
    formatErrorMessage: (Throwable) -> String,
    onCollectionClicked: (String) -> Unit,
    onRetry: () -> Unit,
) {
    when (val collectionsRefreshState = collections.loadState.refresh) {
        is LoadState.Error -> {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState(),
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ErrorView(
                    errorTitle = null,
                    errorMessage = formatErrorMessage(collectionsRefreshState.error),
                    onRetry = {
                        onRetry()
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
                userScrollEnabled = collections.loadState.refresh is LoadState.NotLoading,
                state = gridState,
                contentPadding = PaddingValues(top = 4.dp),
            ) {
                when (collectionsRefreshState) {
                    is LoadState.Loading -> {
                    }

                    is LoadState.NotLoading -> {
                        if (collections.itemCount == 0) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                EmptyStateComponent(stringResource(R.string.empty_search_collections_message))
                            }
                        } else {
                            items(
                                count = collections.itemCount,
                                key = { index ->
                                    val collection = collections.peek(index)
                                    "collection_${collection?.id ?: "no_id"}_$index"
                                },
                            ) { index ->
                                collections[index]?.let { collection ->
                                    CollectionCard(
                                        collection = CollectionItemData.SearchCollection(collection),
                                        onCollectionClicked = { onCollectionClicked(collection.id.toString()) },
                                    )
                                }
                            }
                            if (collections.loadState.append.endOfPaginationReached) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    EndOfPagingComponent()
                                }
                            }
                        }
                    }

                    else -> {}
                }

                // Handle additional load states for collections
                handleLoadStates(
                    pagingItems = collections,
                    getErrorMessage = {
                        formatErrorMessage(it)
                    },
                )
            }
        }
    }
}
