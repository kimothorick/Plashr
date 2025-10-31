package com.kimothorick.plashr.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.kimothorick.plashr.LayoutConfig
import com.kimothorick.plashr.R
import com.kimothorick.plashr.common.Constants.LayoutValues.CARD_IMAGE_HEIGHT
import com.kimothorick.plashr.data.models.photo.SearchPhotosResponse
import com.kimothorick.plashr.data.models.user.UserPhotoLikes
import com.kimothorick.plashr.home.presentation.components.PhotoItemData.CollectionPhoto
import com.kimothorick.plashr.home.presentation.components.PhotoItemData.Photo
import com.kimothorick.plashr.home.presentation.components.PhotoItemData.TopicPhoto
import com.kimothorick.plashr.home.presentation.components.PhotoItemData.UserLikes
import com.kimothorick.plashr.home.presentation.components.PhotoItemData.UserPhoto
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.valentinilk.shimmer.shimmer

/**
 * A sealed interface representing a unified data structure for different types of photo items
 * displayed in the app. This allows for consistent handling of photos from various API endpoints,
 * such as the main photo feed, topics, collections, user profiles, user likes, and search results.
 * Each implementing data class wraps a specific photo model.
 */
sealed interface PhotoItemData {
    data class Photo(
        val data: com.kimothorick.plashr.data.models.photo.Photo,
    ) : PhotoItemData

    data class TopicPhoto(
        val data: com.kimothorick.plashr.data.models.topics.TopicPhoto,
    ) : PhotoItemData

    data class CollectionPhoto(
        val data: com.kimothorick.plashr.data.models.collection.CollectionPhoto,
    ) : PhotoItemData

    data class UserPhoto(
        val data: com.kimothorick.plashr.data.models.user.UserPhoto,
    ) : PhotoItemData

    data class UserLikes(
        val data: UserPhotoLikes,
    ) : PhotoItemData

    data class SearchPhoto(
        val data: SearchPhotosResponse.Result,
    ) : PhotoItemData
}

/**
 * A composable that displays a photo item in a card-like format, suitable for lists or feeds.
 * It includes the user's profile picture and name, an optional photo description, and the main photo image.
 * This component is designed to be versatile, accepting a [PhotoItemData] sealed interface,
 * which allows it to render photos from different sources (e.g., main feed, topics, search results)
 * in a consistent manner.
 *
 * @param modifier The [Modifier] to be applied to the component's root [Column].
 * @param photoData The unified data object ([PhotoItemData]) containing all necessary information for the photo,
 *   such as image URLs, user details, and description.
 * @param onUserProfileClicked A lambda function to be invoked when the user's avatar or name is clicked.
 *   This is typically used to navigate to the user's profile screen.
 * @param onPhotoClicked A lambda function to be invoked when the main photo image is clicked.
 *   This is typically used to navigate to a detail view of the photo.
 */
@Composable
fun PhotoCardItem(
    modifier: Modifier = Modifier,
    photoData: PhotoItemData,
    onUserProfileClicked: () -> Unit = {},
    onPhotoClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val description = when (photoData) {
            is Photo -> photoData.data.description
            is TopicPhoto -> photoData.data.description
            is CollectionPhoto -> photoData.data.description
            is UserPhoto -> photoData.data.description
            is UserLikes -> photoData.data.description
            is PhotoItemData.SearchPhoto -> photoData.data.description
        }

        val profileImageUrl = when (photoData) {
            is Photo -> photoData.data.user?.profileImage?.medium
            is TopicPhoto -> photoData.data.user?.profileImage?.medium
            is CollectionPhoto -> photoData.data.user?.profileImage?.medium
            is UserPhoto -> photoData.data.user?.profileImage?.medium
            is UserLikes -> photoData.data.user?.profileImage?.medium
            is PhotoItemData.SearchPhoto -> photoData.data.user?.profileImage?.medium
        }

        val name = when (photoData) {
            is Photo -> photoData.data.user?.name
            is TopicPhoto -> photoData.data.user?.name
            is CollectionPhoto -> photoData.data.user?.name
            is UserPhoto -> photoData.data.user?.name
            is UserLikes -> photoData.data.user?.name
            is PhotoItemData.SearchPhoto -> photoData.data.user?.name
        }

        val username = when (photoData) {
            is Photo -> photoData.data.user?.username
            is TopicPhoto -> photoData.data.user?.username
            is CollectionPhoto -> photoData.data.user?.username
            is UserPhoto -> photoData.data.user?.username
            is UserLikes -> photoData.data.user?.username
            is PhotoItemData.SearchPhoto -> photoData.data.user?.username
        }

        UserRow(
            profileImageUrl = profileImageUrl,
            name = name,
            username = username,
            onUserClicked = onUserProfileClicked,
        )

        description?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        PhotoImage(photo = photoData, onPhotoClicked)
    }
}

/**
 * A composable that displays a user's information in a horizontal row.
 * It includes the user's circular profile image, their full name, and their username.
 * The entire row is clickable.
 *
 * @param profileImageUrl The URL for the user's profile image. Can be null.
 * @param name The user's full name. Can be null.
 * @param username The user's username. Displayed with an "@" prefix. Can be null.
 * @param onUserClicked A lambda function that is invoked when the row is clicked.
 * @param modifier The [Modifier] to be applied to the root [Row].
 */
@Composable
fun UserRow(
    profileImageUrl: String?,
    name: String?,
    username: String?,
    onUserClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClicked() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(36.dp)
                .clip(MaterialTheme.shapes.extraLarge),
            model = profileImageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.user_profile_image),
        )
        Column(
            modifier = Modifier.padding(start = 8.dp),
        ) {
            name?.let { name ->
                Text(
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )
            }
            username?.let { username ->
                Text(
                    text = "@$username",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

/**
 * A composable that displays a photo with loading and error states.
 * It uses the `rememberAsyncImagePainter` from Coil to handle image loading.
 *
 * - While the image is loading, it displays a colored placeholder box. The color is derived
 *   from the `photo` data's color property.
 * - On successful load, it displays the image, which is clickable.
 * - If the image fails to load, it displays the placeholder with a semi-transparent overlay
 *   and a refresh icon, allowing the user to retry loading the image.
 *
 * @param photo The unified [PhotoItemData] object containing the photo's URL and placeholder color.
 * @param onPhotoClick A lambda function to be invoked when the loaded image is clicked.
 * @param modifier The [Modifier] to be applied to the composable.
 */
@Composable
fun PhotoImage(
    photo: PhotoItemData,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val photoUrl = when (photo) {
        is Photo -> photo.data.urls?.regular
        is TopicPhoto -> photo.data.urls?.regular
        is CollectionPhoto -> photo.data.urls?.regular
        is UserPhoto -> photo.data.urls?.regular
        is UserLikes -> photo.data.urls?.regular
        is PhotoItemData.SearchPhoto -> photo.data.urls?.regular
    }

    val placeholderColorHex = when (photo) {
        is Photo -> photo.data.color
        is TopicPhoto -> photo.data.color
        is CollectionPhoto -> photo.data.color
        is UserPhoto -> photo.data.color
        is UserLikes -> photo.data.color
        is PhotoItemData.SearchPhoto -> photo.data.color
    }

    val placeholderColor = placeholderColorHex?.let { Color(it.toColorInt()) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current).data(photoUrl).build(),
    )
    val imagePainterState by painter.state.collectAsState()
    when (imagePainterState) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading,
        -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(CARD_IMAGE_HEIGHT)
                    .padding(start = 43.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        color = placeholderColor!!,
                    ),
            )
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                modifier = modifier
                    .fillMaxWidth()
                    .height(CARD_IMAGE_HEIGHT)
                    .padding(start = 43.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onPhotoClick() },
                contentScale = ContentScale.Crop,
                contentDescription = "",
            )
        }

        is AsyncImagePainter.State.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CARD_IMAGE_HEIGHT)
                    .padding(start = 43.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(color = placeholderColor!!.copy(0.5f))
                    .clickable(onClick = { painter.restart() }),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = stringResource(R.string.open_on_browser),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

/**
 * A composable that displays a single photo, intended for use in a grid or staggered grid layout.
 * It intelligently handles loading, success, and error states for the image.
 *
 * The composable maintains the original aspect ratio of the photo.
 * - **Loading:** While the image is being fetched, it shows a placeholder `Box` with a
 *   background color derived from the photo's metadata, preserving the final image's dimensions.
 * - **Success:** Upon successful loading, it displays the clickable image.
 * - **Error:** If the image fails to load, it displays the placeholder with a refresh icon,
 *   allowing the user to retry the image request.
 *
 * The appearance, such as the use of corner radius, can be configured via `photoLayoutConfig`.
 *
 * @param photoData The unified data object ([PhotoItemData]) containing the photo's URL,
 *   dimensions, and placeholder color.
 * @param onPhotoClick A lambda function to be invoked when the photo is successfully loaded and clicked.
 *   Defaults to an empty lambda.
 * @param photoLayoutConfig Configuration object that defines layout properties like whether to
 *   apply corner radius to the photo.
 */
@Composable
fun PhotoListItem(
    photoData: PhotoItemData,
    onPhotoClick: () -> Unit = {},
    photoLayoutConfig: LayoutConfig,
) {
    val photoUrl = when (photoData) {
        is Photo -> photoData.data.urls?.regular
        is TopicPhoto -> photoData.data.urls?.regular
        is CollectionPhoto -> photoData.data.urls?.regular
        is UserPhoto -> photoData.data.urls?.regular
        is UserLikes -> photoData.data.urls?.regular
        is PhotoItemData.SearchPhoto -> photoData.data.urls?.regular
    }

    val photoColor = when (photoData) {
        is Photo -> photoData.data.color
        is TopicPhoto -> photoData.data.color
        is CollectionPhoto -> photoData.data.color
        is UserPhoto -> photoData.data.color
        is UserLikes -> photoData.data.color
        is PhotoItemData.SearchPhoto -> photoData.data.color
    }

    val photoWidth = when (photoData) {
        is Photo -> photoData.data.width
        is TopicPhoto -> photoData.data.width
        is CollectionPhoto -> photoData.data.width
        is UserPhoto -> photoData.data.width
        is UserLikes -> photoData.data.width
        is PhotoItemData.SearchPhoto -> photoData.data.width
    }

    val photoHeight = when (photoData) {
        is Photo -> photoData.data.height
        is TopicPhoto -> photoData.data.height
        is CollectionPhoto -> photoData.data.height
        is UserPhoto -> photoData.data.height
        is UserLikes -> photoData.data.height
        is PhotoItemData.SearchPhoto -> photoData.data.height
    }

    val photoBackgroundColor = photoColor?.let { Color(it.toColorInt()) }

    val aspectRatio = remember(photoWidth, photoHeight) {
        if (photoWidth != null && photoHeight != null) {
            photoWidth.toFloat() / photoHeight.toFloat()
        } else {
            1f
        }
    }
    val useCornerRadius = photoLayoutConfig.photoCornerRadius

    val photoPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current).data(photoUrl).build(),
    )

    val painterState by photoPainter.state.collectAsState()
    when (painterState) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading,
        -> {
            Box(
                modifier = Modifier
                    .aspectRatio(aspectRatio)
                    .then(
                        if (useCornerRadius) {
                            Modifier.clip(MaterialTheme.shapes.medium)
                        } else {
                            Modifier
                        },
                    )
                    .fillMaxWidth()
                    .background(
                        color = photoBackgroundColor!!,
                    ),
            )
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = photoPainter,
                modifier = Modifier
                    .aspectRatio(
                        aspectRatio,
                    )
                    .then(
                        if (useCornerRadius) {
                            Modifier.clip(MaterialTheme.shapes.medium)
                        } else {
                            Modifier
                        },
                    )
                    .clickable(onClick = onPhotoClick),
                contentScale = ContentScale.Crop,
                contentDescription = "",
            )
        }

        is AsyncImagePainter.State.Error -> {
            Box(
                modifier = Modifier
                    .aspectRatio(aspectRatio)
                    .then(
                        if (useCornerRadius) {
                            Modifier.clip(MaterialTheme.shapes.medium)
                        } else {
                            Modifier
                        },
                    )
                    .fillMaxWidth()
                    .background(color = photoBackgroundColor!!.copy(0.5f))
                    .clickable(onClick = { photoPainter.restart() }),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = "Open on browser",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

/**
 * A composable that displays a placeholder for a [PhotoCardItem] while content is loading.
 * It mimics the layout of a `PhotoCardItem`, including a shimmer effect to indicate an
 * active loading state. This component consists of a `UserRowShimmer` and a rectangular
 * box representing the photo area.
 *
 * This composable is also a `@Preview`, allowing it to be rendered in the Android Studio
 * design view for development and testing purposes.
 *
 */
@Preview
@Composable
fun PhotoCardItemShimmer() {
    PlashrTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                UserRowShimmer()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(CARD_IMAGE_HEIGHT)
                        .padding(start = 43.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                )
            }
        }
    }
}

/**
 * A composable that displays a placeholder for a [UserRow] while its content is loading.
 * It mimics the layout of the `UserRow`, showing a circular shape for the profile picture
 * and two rectangular bars for the user's name and username.
 *
 * This component is intended to be used within a shimmer effect to indicate a loading state.
 * It is also a `@Preview` for easy visualization in Android Studio's design view.
 */
@Preview
@Composable
fun UserRowShimmer() {
    PlashrTheme {
        Surface {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .clip(MaterialTheme.shapes.extraLarge),
                ) {}
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .height(14.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .height(14.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    )
                }
            }
        }
    }
}

/**
 * A composable that displays a placeholder for a [PhotoListItem] while its content is loading.
 * It renders a simple box with a background color that can maintain a specific aspect ratio,
 * mimicking the dimensions of the photo that will eventually be loaded.
 *
 * This is typically used within a shimmer effect to provide a loading indicator for photos
 * in a grid or staggered grid layout.
 *
 * @param aspectRatio The aspect ratio for the placeholder. If null, the placeholder will attempt
 *   to fill the available size, constrained by a default height of 200.dp.
 * @param modifier The [Modifier] to be applied to the root [Column].
 */
@Composable
fun PhotoListItemShimmer(
    aspectRatio: Float?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (aspectRatio != null) {
                        Modifier.aspectRatio(aspectRatio)
                    } else {
                        Modifier
                    },
                )
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .height(200.dp),
        )
    }
}

@Preview
@Composable
fun PhotoListItemShimmerPreview() {
    PlashrTheme {
        Surface {
            PhotoListItemShimmer(aspectRatio = 16f / 9f)
        }
    }
}
