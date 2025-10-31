package com.kimothorick.plashr.ui.mainscreen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kimothorick.plashr.R
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.ui.theme.Typography
import com.kimothorick.plashr.utils.tooling.ComponentPreviews
import me.saket.cascade.CascadeDropdownMenu

/**
 * A large top app bar for the main screen of the Plashr app.
 *
 * This component displays the app icon and a user profile icon. The profile icon, upon interaction,
 * reveals a dropdown menu with options like "Login", "View Profile", and "Settings".
 * The options displayed depend on whether the user is currently authorized.
 * Long-pressing the profile icon can trigger a "Manage Account" bottom sheet.
 *
 * @param modifier The [Modifier] to be applied to this TopAppBar.
 * @param username The username of the currently logged-in user. Used for the "View Profile" action. Can be null if not logged in.
 * @param profilePictureUrl The URL for the user's profile picture. If null, a default icon is shown.
 * @param isAppAuthorized A boolean indicating whether the user is logged in and authorized.
 * @param onSettingsClicked A lambda to be invoked when the "Settings" menu item is clicked.
 * @param onLoginClicked A lambda to be invoked when the "Login" menu item is clicked.
 * @param onViewProfileClicked A lambda that takes the username as a [String] and is invoked when the "View Profile" menu item is clicked.
 * @param showManageAccountBottomSheet A lambda to be invoked on a long press of the user profile icon.
 * @param scrollBehavior A [TopAppBarScrollBehavior] which defines how the top app bar collapses and expands when the content is scrolled.
 * @param isAppIconHidden A boolean to control the visibility of the app icon in the navigation slot.
 */
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlashrMainScreenLargeTopAppBar(
    modifier: Modifier = Modifier,
    username: String? = null,
    profilePictureUrl: String? = null,
    isAppAuthorized: Boolean,
    onSettingsClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onViewProfileClicked: (String) -> Unit,
    showManageAccountBottomSheet: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    isAppIconHidden: Boolean,
) {
    var isUserMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var userProfileIcon by remember { mutableStateOf<@Composable () -> Unit>({}) }

    LaunchedEffect(isAppAuthorized, profilePictureUrl) {
        userProfileIcon = if (isAppAuthorized && profilePictureUrl != null) {
            {
                AsyncImage(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    isUserMenuVisible = true
                                },
                                onLongPress = {
                                    showManageAccountBottomSheet()
                                },
                            )
                        },
                    model = profilePictureUrl,
                    contentDescription = null,
                )
            }
        } else {
            {
                ProfileComponents().ProfilePictureIcon(
                    iconSize = 24,
                    modifier = Modifier.size(30.dp),
                    onClick = {
                        isUserMenuVisible = true
                    },
                    onLongClick = {},
                )
            }
        }
    }

    TopAppBar(
        title = {},
        navigationIcon = {
            if (isAppIconHidden) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = stringResource(R.string.app_icon),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp),
                )
            }
        },
        actions = {
            IconButton(
                onClick = {},
            ) {
                userProfileIcon()
            }

            CascadeDropdownMenu(
                expanded = isUserMenuVisible,
                onDismissRequest = { isUserMenuVisible = false },
                shape = MaterialTheme.shapes.medium,
            ) {
                if (!isAppAuthorized) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(R.string.login), style = MaterialTheme.typography.bodyMedium)
                        },
                        onClick = {
                            onLoginClicked()
                            isUserMenuVisible = false
                        },
                    )
                } else {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(R.string.view_profile), style = MaterialTheme.typography.bodyMedium)
                        },
                        onClick = {
                            if (username != null) {
                                onViewProfileClicked(username)
                            }
                            isUserMenuVisible = false
                        },
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.settings), style = MaterialTheme.typography.bodyMedium)
                    },
                    onClick = {
                        onSettingsClicked()
                        isUserMenuVisible = false
                    },
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier.wrapContentHeight(),
    )
}

/**
 * A search bar Composable for the Plashr app, using Material 3's `SearchBar`.
 *
 * This component provides a text input field for users to enter search queries.
 * It includes a leading search icon, a clear button that appears when text is entered,
 * and an optional filter button. The search is triggered when the user submits the query,
 * which also hides the keyboard and clears focus. The search bar itself is not expandable
 * in this implementation; it's always visible in its compact form.
 *
 * @param query The current text in the search bar.
 * @param onQueryChanged A callback that is invoked when the user types in the search bar.
 * The updated query string is passed as a parameter.
 * @param onSearchTriggered A callback that is invoked when the user triggers a search action,
 * for example, by pressing the search icon on the keyboard. The current query string is passed as a parameter.
 * @param showFilterButton A boolean to control the visibility of the filter icon button. Defaults to `false`.
 * @param onFilterClick A lambda to be invoked when the filter icon button is clicked. Defaults to an empty lambda.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlashrSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    showFilterButton: Boolean = false,
    onFilterClick: () -> Unit = {},
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = MaterialTheme.shapes.extraLarge,
                ),
                query = query,
                onQueryChange = onQueryChanged,
                onSearch = {
                    onSearchTriggered(it)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    isExpanded = false
                },
                expanded = isExpanded,
                onExpandedChange = { isExpanded = false },
                placeholder = {
                    Text(
                        stringResource(R.string.search_photos_users_collections),
                        style = Typography.bodyMedium,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                trailingIcon = {
                    Row {
                        if (query.isNotEmpty()) {
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                onClick = {
                                    onQueryChanged("")
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.clear_search_query),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        if (showFilterButton) {
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                onClick = { onFilterClick() },
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.FilterList,
                                    contentDescription = stringResource(R.string.filter_search_results),
                                )
                            }
                        }
                    }
                },
            )
        },
        expanded = isExpanded,
        onExpandedChange = { isExpanded = false },
    ) {
    }
}

//region Previews
@ComponentPreviews
@Composable
fun PlashrSearchBarPreview() {
    PlashrTheme {
        PlashrSearchBar(
            query = "Dogs",
            onQueryChanged = {},
            onSearchTriggered = {},
            showFilterButton = true,
        )
    }
}
//endregion
