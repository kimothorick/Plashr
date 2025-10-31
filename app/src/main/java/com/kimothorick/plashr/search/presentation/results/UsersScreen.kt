package com.kimothorick.plashr.search.presentation

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kimothorick.plashr.R
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.user.SearchUsersResponse
import com.kimothorick.plashr.search.presentation.components.SearchUserItem
import com.kimothorick.plashr.ui.common.handleLoadStates
import com.kimothorick.plashr.ui.components.EmptyStateComponent
import com.kimothorick.plashr.ui.components.EndOfPagingComponent
import com.kimothorick.plashr.ui.components.ErrorView

/**
 * A composable that displays a grid of users fetched via pagination.
 *
 * This screen handles different loading states:
 * - **Loading:** Shows a shimmer effect.
 * - **Error:** Displays an error message with a retry button.
 * - **NotLoading (with data):** Shows a `LazyVerticalGrid` of user items.
 * - **NotLoading (empty):** Shows an empty state message.
 *
 * It also handles pagination append states, showing loading indicators or an end-of-list message.
 *
 * @param modifier The modifier to be applied to the root composable.
 * @param users A [LazyPagingItems] of user search results to display.
 * @param usersGridState The state object to be used for the `LazyVerticalGrid`, allowing control and observation of scroll position.
 * @param formatErrorMessage A lambda function that transforms a [Throwable] into a user-readable error string.
 * @param onUserSelected A callback function invoked when a user item is clicked, passing the user's username.
 * @param onRetry A callback function invoked when the user clicks the retry button in the error state.
 */
@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    users: LazyPagingItems<SearchUsersResponse.Result>,
    usersGridState: LazyGridState,
    formatErrorMessage: (Throwable) -> String,
    onUserSelected: (String) -> Unit,
    onRetry: () -> Unit,
) {
    when (val refreshLoadState = users.loadState.refresh) {
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
                    errorMessage = formatErrorMessage(refreshLoadState.error),
                    onRetry = {
                        onRetry()
                    },
                )
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = Constants.LayoutValues.COLLECTION_CARD_ADAPTIVE_MIN_SIZE),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                userScrollEnabled = users.loadState.refresh is LoadState.NotLoading,
                state = usersGridState,
                contentPadding = PaddingValues(top = 4.dp),
            ) {
                when (refreshLoadState) {
                    is LoadState.Loading -> {
                    }

                    is LoadState.NotLoading -> {
                        if (users.itemCount == 0) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                EmptyStateComponent(stringResource(R.string.empty_search_users_message))
                            }
                        } else {
                            items(
                                count = users.itemCount,
                                key = { index ->
                                    val users = users.peek(index)
                                    "user_${users?.id ?: "no_id"}_$index"
                                },
                            ) { index ->
                                users[index]?.let { user ->
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.clickable(
                                            onClick = {
                                                user.username?.let {
                                                    onUserSelected(it)
                                                }
                                            },
                                        ),
                                    ) {
                                        SearchUserItem(
                                            searchResult = user,
                                        )
                                        if (index < users.itemCount - 1) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(start = 60.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                            )
                                        }
                                    }
                                }
                            }
                            if (users.loadState.append.endOfPaginationReached) {
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
                    pagingItems = users,
                    getErrorMessage = {
                        formatErrorMessage(it)
                    },
                )
            }
        }
    }
}
