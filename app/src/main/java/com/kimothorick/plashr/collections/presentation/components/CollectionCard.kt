package com.kimothorick.plashr.collections.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kimothorick.data.PreviewCollection
import com.kimothorick.data.PreviewUserCollection
import com.kimothorick.plashr.R
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.photoDetails.presentation.PhotoCollectionState
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.utils.getFormattedUpdatedAtPretty
import com.kimothorick.plashr.utils.tooling.ComponentPreviews
import com.valentinilk.shimmer.shimmer

/**
 * A sealed interface representing different types of collection data.
 * This is used to abstract away the specific data models for collections
 * coming from different API endpoints (e.g., general collections, user-specific
 * collections, search results) and allows them to be used interchangeably in UI
 * components like `CollectionCard`.
 */
sealed interface CollectionItemData {
    /**
     * Represents a standard collection item.
     * This class wraps the general `Collection` data model.
     *
     * @property data The core data model for a collection, containing details like title, user, photos, etc.
     */
    data class Collection(
        val data: com.kimothorick.plashr.data.models.collection.Collection,
    ) : CollectionItemData

    /**
     * Represents a collection item specific to a user's profile.
     * This class wraps the `UserCollection` data model, which is typically
     * retrieved from an endpoint listing a user's own collections.
     *
     * @property data The core data model for a user's collection.
     */
    data class UserCollection(
        val data: com.kimothorick.plashr.data.models.user.UserCollection,
    ) : CollectionItemData

    /**
     * Represents a collection item returned from a search query.
     * This class wraps the `SearchCollectionsResponse.Result` data model, which
     * is specific to the collections search API endpoint.
     *
     * @property data The core data model for a collection found via search.
     */
    data class SearchCollection(
        val data: com.kimothorick.plashr.data.models.collection.SearchCollectionsResponse.Result,
    ) : CollectionItemData
}

/**
 * A composable that displays a visual card representing a photo collection.
 * It shows the collection's cover photo, title, creator's information, and total photo count.
 * This card is designed to be versatile and can display different types of collection data
 * by accepting a [CollectionItemData] sealed interface instance.
 *
 * @param modifier The [Modifier] to be applied to the component.
 * @param collection The data for the collection to display, wrapped in a [CollectionItemData] sealed type.
 * This allows the card to handle data from various sources (e.g., general collections, user collections, search results).
 * @param onCollectionClicked A lambda function to be invoked when the user clicks on the card.
 */
@SuppressLint("ResourceType")
@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    collection: CollectionItemData,
    onCollectionClicked: () -> Unit,
) {
    val title: String?
    val username: String?
    val userProfileImage: String?
    val totalPhotos: Int?
    val coverPhoto: String?
    val isPrivate: Boolean?
    when (collection) {
        is CollectionItemData.Collection -> {
            val collectionData = collection.data
            title = collectionData.title
            username = collectionData.user?.username
            userProfileImage = collectionData.user?.profileImage?.large
            totalPhotos = collectionData.totalPhotos
            coverPhoto = collectionData.coverPhoto?.urls?.regular
            isPrivate = collectionData.private
        }

        is CollectionItemData.UserCollection -> {
            val collectionData = collection.data
            title = collectionData.title
            username = collectionData.user?.username
            userProfileImage = collectionData.user?.profileImage?.large
            totalPhotos = collectionData.totalPhotos
            coverPhoto = collectionData.coverPhoto?.urls?.regular
            isPrivate = collectionData.private
        }

        is CollectionItemData.SearchCollection -> {
            val collectionData = collection.data
            title = collectionData.title
            username = collectionData.user?.username
            userProfileImage = collectionData.user?.profileImage?.large
            totalPhotos = collectionData.totalPhotos
            coverPhoto = collectionData.coverPhoto?.urls?.regular
            isPrivate = collectionData.private
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
    )
    Box(
        modifier = modifier
            .width(357.dp)
            .height(162.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onCollectionClicked()
            },
    ) {
        AsyncImage(
            model = coverPhoto,
            contentDescription = "Collection Cover Photo",
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .clip(MaterialTheme.shapes.large),
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (isPrivate == true) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Text(
                    text = title ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = userProfileImage,
                    contentDescription = stringResource(R.string.collection_owner_profile_picture),
                    modifier = Modifier
                        .size(24.dp)
                        .clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop,
                )

                Spacer(Modifier.width(4.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.curated_by),
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        maxLines = 1,
                        lineHeight = 12.sp,
                    )
                    Text(
                        text = username ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = pluralStringResource(
                        R.plurals.photo_count,
                        totalPhotos!!,
                        totalPhotos,
                    ),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(0.5f, fill = false)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

/**
 * A composable that displays a shimmer loading placeholder for a [CollectionCard].
 * This is used to indicate that collection data is being loaded, providing a good user experience
 * by showing a skeleton of the content that is about to appear. The layout and dimensions
 * of this shimmer match the final `CollectionCard` to prevent layout shifts.
 *
 * @param modifier The [Modifier] to be applied to the component.
 */
@Composable
fun CollectionCardShimmer(
    modifier: Modifier = Modifier,
) {
    val shimmerBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    Column(
        modifier = modifier
            .width(357.dp)
            .height(162.dp)
            .clip(MaterialTheme.shapes.medium)
            .shimmer()
            .background(shimmerBackgroundColor)
            .padding(vertical = 16.dp, horizontal = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(12.dp)
                .clip(MaterialTheme.shapes.small)
                .background(placeholderColor),
        )
        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(placeholderColor),
            ) {}

            Spacer(Modifier.width(4.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .height(10.dp)
                        .background(placeholderColor),
                )

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .height(10.dp)
                        .background(placeholderColor),
                )
            }
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .height(12.dp)
                    .background(placeholderColor),
            )
        }
    }
}

/**
 * A composable that displays a card for a user's collection, intended for use in a list
 * where a user can add or remove a specific photo from their collections.
 *
 * This card visually indicates whether a photo is already in the collection.
 * It also shows an in-progress state when the add/remove operation is running.
 *
 * @param modifier The modifier to be applied to the card.
 * @param photoCollectionState The current state of the add/remove photo operation (e.g., Idle, InProgress).
 *                             This is used to display a progress indicator on the specific collection being modified.
 * @param collection The [UserCollection] data to display.
 * @param photoCollected A boolean indicating if the photo is currently part of this collection.
 *                       If `true`, a green overlay is shown.
 * @param onCollectionClicked A lambda function to be invoked when the user clicks the card,
 *                            typically to trigger adding or removing the photo.
 * @param totalPhotoCount The total number of photos in the collection.
 * @param lastUpdated A string representation of when the collection was last updated.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddPhotoToCollectionCard(
    modifier: Modifier = Modifier,
    photoCollectionState: PhotoCollectionState,
    collection: UserCollection,
    photoCollected: Boolean?,
    onCollectionClicked: () -> Unit,
    totalPhotoCount: Int?,
    lastUpdated: String?,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onCollectionClicked()
            },
    ) {
        if (collection.coverPhoto?.urls?.regular != null) {
            AsyncImage(
                model = collection.coverPhoto.urls.regular,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.small)
                .background(
                    color = if (photoCollected == true) {
                        colorResource(R.color.collect_green_70)
                    } else {
                        Color.Black.copy(0.5f)
                    },
                ),
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = if (collection.private == true) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = collection.title ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = pluralStringResource(
                        R.plurals.photo_count,
                        totalPhotoCount!!,
                        totalPhotoCount,
                    ),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .weight(0.5f, fill = false)
                        .fillMaxWidth(),
                )
                Text(
                    text = lastUpdated.getFormattedUpdatedAtPretty(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(0.5f, fill = false)
                        .fillMaxWidth(),
                )
            }
        }

        if (photoCollectionState is PhotoCollectionState.InProgress && collection.id == photoCollectionState.collectionId) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.2f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularWavyProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    stroke = Stroke(width = 8f, cap = StrokeCap.Round),
                )
            }
        }
    }
}

/**
 * A composable that displays a shimmer loading placeholder for an [AddPhotoToCollectionCard].
 * This is used to indicate that the list of user collections is being loaded, providing a
 * skeleton UI that matches the dimensions and layout of the final card.
 *
 * @param modifier The [Modifier] to be applied to the component.
 */
@Composable
fun AddPhotoToCollectionCardShimmer(
    modifier: Modifier = Modifier,
) {
    val shimmerBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.small)
            .shimmer()
            .background(shimmerBackgroundColor),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(placeholderColor),
                )
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.7f)
                        .clip(MaterialTheme.shapes.small)
                        .background(placeholderColor),
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(0.3f)
                        .clip(MaterialTheme.shapes.small)
                        .background(placeholderColor),
                )
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(0.4f)
                        .clip(MaterialTheme.shapes.small)
                        .background(placeholderColor),
                )
            }
        }
    }
}

// region PREVIEWS

@ComponentPreviews
@Composable
fun CollectionCardPreview(
    modifier: Modifier = Modifier,
) {
    CollectionCard(
        modifier = modifier.clip(MaterialTheme.shapes.large),
        collection = CollectionItemData.Collection(PreviewCollection),
        onCollectionClicked = {},
    )
}

@ComponentPreviews
@Composable
fun CollectionCardShimmerPreview() {
    PlashrTheme {
        Surface {
            CollectionCardShimmer()
        }
    }
}

@ComponentPreviews
@Composable
fun AddPhotoToCollectionCardShimmerPreview() {
    PlashrTheme {
        Surface {
            AddPhotoToCollectionCardShimmer()
        }
    }
}

@Composable
fun AddPhotoToCollectionCardPreview(
    state: PhotoCollectionState,
    photoCollected: Boolean,
) {
    PlashrTheme {
        Surface {
            AddPhotoToCollectionCard(
                photoCollectionState = state,
                collection = PreviewUserCollection,
                photoCollected = photoCollected,
                onCollectionClicked = {},
                totalPhotoCount = 10,
                lastUpdated = "2023-09-01",
            )
        }
    }
}

@ComponentPreviews
@Composable
fun AddPhotoToCollectionCardIdlePreview() {
    AddPhotoToCollectionCardPreview(
        state = PhotoCollectionState.Idle,
        photoCollected = false,
    )
}

@ComponentPreviews
@Composable
fun AddPhotoToCollectionCardInProgressPreview() {
    val collectionId = PreviewUserCollection.id
    AddPhotoToCollectionCardPreview(
        state = PhotoCollectionState.InProgress(collectionId!!),
        photoCollected = false,
    )
}

@ComponentPreviews
@Composable
fun AddPhotoToCollectionCardSuccessPreview() {
    AddPhotoToCollectionCardPreview(
        state = PhotoCollectionState.Idle,
        photoCollected = true,
    )
}

// endregion
