package com.kimothorick.plashr.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kimothorick.plashr.ui.components.InlineErrorView

/**
 * Handles the load states for pagination within a [LazyStaggeredGridScope].
 * It displays a loading indicator when appending new items, and an error view
 * with a retry option if an error occurs during append.
 *
 * This is an extension function on [LazyStaggeredGridScope] and should be called
 * within the body of a `LazyVerticalStaggeredGrid` or similar composable.
 *
 * @param T The type of items in the [LazyPagingItems].
 * @param pagingItems The [LazyPagingItems] instance managing the paged data.
 * @param getErrorMessage A lambda function that takes a [Throwable] and returns a
 * user-friendly error message string.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun <T : Any> LazyStaggeredGridScope.handleLoadStates(
    pagingItems: LazyPagingItems<T>,
    getErrorMessage: (Throwable) -> String,
) {
    pagingItems.apply {
        when (val appendState = loadState.append) {
            is LoadState.Loading -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    LoadingIndicator()
                }
            }

            is LoadState.Error -> {
                handleErrorState(
                    errorMessage = getErrorMessage(appendState.error),
                    onRetry = { pagingItems.retry() },
                )
            }

            else -> {}
        }
    }
}

/**
 * A [LazyStaggeredGridScope] extension function that displays an inline error view.
 * This is typically used to handle pagination errors, showing a message and a retry button
 * that spans the full width of the staggered grid.
 *
 * @param errorMessage The error message to display to the user.
 * @param onRetry The lambda function to be invoked when the user clicks the retry button.
 */
fun LazyStaggeredGridScope.handleErrorState(
    errorMessage: String,
    onRetry: () -> Unit,
) {
    item(span = StaggeredGridItemSpan.FullLine) {
        InlineErrorView(
            errorMessage = errorMessage,
            onRetry = onRetry,
        )
    }
}

/**
 * An extension function for [LazyGridScope] that handles the various load states for appending
 * items in a paginated list. It displays a loading indicator when more items are being fetched
 * and an error view with a retry button if fetching fails.
 *
 * This function should be called at the end of the `LazyGridScope` block to show the state
 * of the append operation (i.e., loading more items at the end of the list).
 *
 * @param T The type of items in the [LazyPagingItems].
 * @param pagingItems The [LazyPagingItems] instance whose load state is to be observed.
 * @param getErrorMessage A lambda function that takes a [Throwable] and returns a formatted
 * error message string to be displayed to the user.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun <T : Any> LazyGridScope.handleLoadStates(
    pagingItems: LazyPagingItems<T>,
    getErrorMessage: (Throwable) -> String,
) {
    pagingItems.apply {
        when (val appendState = loadState.append) {
            is LoadState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                        )
                    }
                }
            }

            is LoadState.Error -> {
                handleErrorState(
                    errorMessage = getErrorMessage(appendState.error),
                    onRetry = { pagingItems.retry() },
                )
            }

            else -> {}
        }
    }
}

/**
 * Displays an inline error item within a `LazyVerticalGrid`.
 *
 * This extension function adds an item to the grid that spans the entire width.
 * This item contains an [InlineErrorView], which shows an error message and a retry button.
 *
 * @param errorMessage The error message to be displayed.
 * @param onRetry The lambda function to be invoked when the user clicks the retry button.
 */
fun LazyGridScope.handleErrorState(
    errorMessage: String,
    onRetry: () -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        InlineErrorView(
            errorMessage = errorMessage,
            onRetry = onRetry,
        )
    }
}
