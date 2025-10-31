package com.kimothorick.plashr.profile.presentation.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kimothorick.plashr.R
import com.kimothorick.plashr.collections.presentation.components.CollectionCard
import com.kimothorick.plashr.collections.presentation.components.CollectionCardShimmer
import com.kimothorick.plashr.collections.presentation.components.CollectionItemData
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EmptyStateComponent
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.InlineErrorView

/**
 * A composable function that displays a grid of user collections with pagination.
 *
 * This screen handles different loading states:
 * - **Loading:** Shows a shimmer effect.
 * - **Error:** Displays an error message with a retry button.
 * - **Empty:** Shows a message indicating no collections were found.
 * - **Success:** Displays the collections in a `LazyVerticalStaggeredGrid`.
 *
 * It also handles pagination append errors and displays an "end of list" indicator.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param onCollectionSelected A callback function that is invoked when a collection card is clicked,
 * providing the ID of the selected collection.
 * @param collections The paginated list of user collections to display.
 * @param lazyStaggeredGridState The state for the `LazyVerticalStaggeredGrid`, used to control and
 * observe the scroll position.
 * @param getErrorMessage A function to convert a `Throwable` into a user-friendly error string.
 * @param onRetry A callback function to be invoked when the user clicks the retry button in case
 * of a loading error.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCollectionsScreen(
    modifier: Modifier = Modifier,
    onCollectionSelected: (id: String) -> Unit,
    collections: LazyPagingItems<UserCollection>,
    lazyStaggeredGridState: LazyStaggeredGridState,
    getErrorMessage: (Throwable) -> String,
    onRetry: () -> Unit = {},
) {
    when (val refreshState = collections.loadState.refresh) {
        is LoadState.Error -> {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                InlineErrorView(
                    errorMessage = getErrorMessage(refreshState.error),
                    onRetry = {
                        onRetry()
                    },
                )
            }
        }

        else -> {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 357.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.padding(horizontal = 8.dp),
                state = lazyStaggeredGridState,
                contentPadding = PaddingValues(top = 4.dp),
            ) {
                when (collections.loadState.refresh) {
                    is LoadState.Loading -> {
                        items(Constants.LayoutValues.INITIAL_SHIMMER_COUNT) {
                            CollectionCardShimmer(modifier = Modifier.padding(8.dp))
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (collections.itemCount == 0) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                EmptyStateComponent(stringResource(R.string.empty_user_collections_message))
                            }
                        } else {
                            items(
                                count = collections.itemCount,
                                key = { index ->
                                    val collectionItem = collections.peek(index)
                                    "collections_${collectionItem?.id ?: "no_id"}_$index"
                                },
                            ) { index ->
                                val collection = collections[index]
                                if (collection != null) {
                                    CollectionCard(
                                        collection = CollectionItemData.UserCollection(data = collection),
                                        onCollectionClicked = {
                                            collection.id?.let { onCollectionSelected(it) }
                                        },
                                    )
                                }
                            }

                            if (collections.loadState.append.endOfPaginationReached) {
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    EndOfPagingComponent()
                                }
                            }
                        }
                    }

                    else -> {}
                }

                handleLoadStates(
                    pagingItems = collections,
                    getErrorMessage = {
                        getErrorMessage(it)
                    },
                )
            }
        }
    }
}
