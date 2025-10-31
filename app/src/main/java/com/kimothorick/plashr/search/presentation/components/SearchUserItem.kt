package com.kimothorick.plashr.search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kimothorick.plashr.data.models.user.SearchUsersResponse
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.utils.tooling.ComponentPreviews
import com.valentinilk.shimmer.shimmer

/**
 * A composable that displays a single user item from a search result.
 * It shows the user's circular profile image, their full name, and their username.
 *
 * @param modifier The modifier to be applied to the component.
 * @param searchResult The user data object containing information like profile image, name, and username.
 */
@Composable
fun SearchUserItem(
    modifier: Modifier = Modifier,
    searchResult: SearchUsersResponse.Result,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = searchResult.profileImage?.large,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(
                    shape = CircleShape,
                ),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            searchResult.name?.let { name ->
                Text(
                    text = name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            searchResult.username?.let { username ->
                Text(
                    text = "@$username",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                )
            }
        }
    }
}

@ComponentPreviews
@Composable
fun SearchUserItemShimmerPreview() {
    PlashrTheme {
        Surface {
            SearchUserItemShimmer()
        }
    }
}

/**
 * A shimmer placeholder for the [SearchUserItem] composable.
 *
 * @param modifier The modifier to be applied to the component.
 */
@Composable
fun SearchUserItemShimmer(
    modifier: Modifier = Modifier,
) {
    val placeholderColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Placeholder for the profile image
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(placeholderColor),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(MaterialTheme.typography.bodyMedium.fontSize.value.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(placeholderColor),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(MaterialTheme.typography.bodySmall.fontSize.value.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(placeholderColor),
            )
        }
    }
}

@ComponentPreviews
@Composable
fun SearchUserItemPreview() {
    val user = SearchUsersResponse.Result(
        firstName = "John",
        id = "1",
        instagramUsername = "john.doe",
        lastName = "Doe",
        links = null,
        name = "John Doe",
        portfolioUrl = null,
        profileImage = null,
        totalCollections = 10,
        totalLikes = 100,
        totalPhotos = 50,
        twitterUsername = "john_doe",
        username = "johndoe",
    )
    PlashrTheme {
        Surface {
            SearchUserItem(searchResult = user)
        }
    }
}
