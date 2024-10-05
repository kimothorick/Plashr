package com.kimothorick.plashr.home.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kimothorick.plashr.data.models.photo.Photo
import com.kimothorick.plashr.data.models.user.User
import com.kimothorick.plashr.ui.theme.PlashrTheme

@Composable
fun PhotoCardItem(
    photo: Photo,
    onUserClicked: () -> Unit = {},
    onPhotoClicked: () -> Unit = {},
    onLikePhotoClicked: (liked: Boolean) -> Unit = {},
    onCollectPhotoClicked: (collected: Boolean) -> Unit = {},
    onSharePhotoClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UserRow(photo, onUserClicked)

        photo.description?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        PhotoImage(photo, onPhotoClicked)

        InteractionRow(photo, onLikePhotoClicked, onCollectPhotoClicked, onSharePhotoClicked)
    }
}

@Composable
fun UserRow(photo: Photo, onUserClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(32.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .clickable {onUserClicked()},
            model = photo.user!!.profile_image!!.medium,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable(enabled = false, onClick = {onUserClicked()})
        ) {
            photo.user.name?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            photo.user.username?.let {
                Text(
                    text = "@$it",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun PhotoImage(photo: Photo, onPhotoClicked: () -> Unit) {

    AsyncImage(modifier = Modifier
        .fillMaxWidth()
        .height(296.dp)
        .padding(start = 40.dp)
        .clip(MaterialTheme.shapes.medium)
        .clickable {onPhotoClicked()},
        model = photo.urls!!.regular,
        contentScale = ContentScale.Crop,
        contentDescription = "",
        placeholder = remember(photo.color) {
            val color = Color(android.graphics.Color.parseColor(photo.color))
            ColorPainter(color)
        })
}

@Composable
fun InteractionRow(
    photo: Photo,
    onLikePhotoClicked: (liked: Boolean) -> Unit,
    onCollectPhotoClicked: (collected: Boolean) -> Unit,
    onSharePhotoClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LikeButton(photo, onLikePhotoClicked)

        Text(
            text = photo.likes.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(4.dp))

        CollectButton(photo, onCollectPhotoClicked)

        Spacer(modifier = Modifier.weight(1f))

        ShareButton(onSharePhotoClicked)
    }
}

@Composable
fun LikeButton(photo: Photo, onLikePhotoClicked: (liked: Boolean) -> Unit) {
    IconButton(
        onClick = {onLikePhotoClicked(photo.liked_by_user != true)}, modifier = Modifier.size(32.dp)
    ) {
        Icon(
            imageVector = if (photo.liked_by_user == true) Icons.Rounded.Favorite
            else Icons.Outlined.FavoriteBorder,
            contentDescription = "Like Photo Button",
            tint = if (photo.liked_by_user == true) Color(android.graphics.Color.parseColor("#EA4336")) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        )
    }
}

@Composable
fun CollectButton(photo: Photo, onCollectPhotoClicked: (collected: Boolean) -> Unit) {
    when (photo.current_user_collections?.size) {
        0 -> {
            IconButton(
                onClick = {onCollectPhotoClicked(false)}, modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Collect Photo Button",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        else -> {
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = {onCollectPhotoClicked(true)}, modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Bookmark,
                    contentDescription = "Collect Photo Button",
                    tint = Color(android.graphics.Color.parseColor("#00B761")),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                )
            }
        }
    }
}

@Composable
fun ShareButton(onSharePhotoClicked: () -> Unit) {
    IconButton(
        onClick = {onSharePhotoClicked()}, modifier = Modifier.size(32.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = "Share Photo button",
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PhotoListItem(
    photo: Photo
) {
    AsyncImage(modifier = Modifier
        .aspectRatio(
            photo.width!!.toFloat() / photo.height!!.toFloat()
        )
        .clip(
            MaterialTheme.shapes.medium
        ),
        model = photo.urls!!.regular,
        contentScale = ContentScale.Crop,
        contentDescription = "",
        placeholder = remember(photo.color) {
            val color = Color(android.graphics.Color.parseColor(photo.color))
            ColorPainter(color)
        })
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PHONE)
@Composable
fun PhotoCardItemPreview() {
    PlashrTheme {
        PhotoCardItem(
            photo = Photo(
                id = "1",
                null,
                null,
                null,
                null,
                "#000000",
                null,
                null,
                24,
                false,
                "Through photography, the beauty of Mother Nature can be frozen in time." + " This category celebrates the magic of our planet and beyond — from the immensity of the great outdoors, to miraculous moments in your own backyard.",
                null,
                null,
                null,/*listOf(
                    Photo.CurrentUserCollection(
                        id = null,
                        title = null,
                        published_at = null,
                        last_collected_at = null,
                        updated_at = null,
                        cover_photo = null,
                        user = null
                    )
                ),*/
                null,
                null,
                Photo.Urls(
                    null,
                    null,
                    "https://web-images3.pixpa.com/lLlWMdXI8csKLLbkcSEw2G6StDefqr8uWaadqL7vc4k/rs:fit:1200:0/q:80/aHR0cHM6Ly9waXhwYWNvbS1pbWcucGl4cGEuY29tL2NvbS9hcnRpY2xlcy8xNTI1ODkxODc5LTM3OTcyMC13YXJyZW4td29uZy0yNDIyODYtdW5zcGxhc2hqcGcuanBn",
                    null,
                    null
                ),
                null,
                user = User(
                    id = "1",
                    username = "kimothorick",
                    name = "Rick Kimotho",
                    portfolio_url = "portfolio_url",
                    bio = "bio",
                    location = "location",
                    total_likes = 1,
                    total_photos = 1,
                    total_collections = 1,
                    instagram_username = "instagram_username",
                    twitter_username = "twitter_username",
                    profile_image = User.ProfileImage(
                        small = "small",
                        medium = "https://web-images3.pixpa.com/lLlWMdXI8csKLLbkcSEw2G6StDefqr8uWaadqL7vc4k/rs:fit:1200:0/q:80/aHR0cHM6Ly9waXhwYWNvbS1pbWcucGl4cGEuY29tL2NvbS9hcnRpY2xlcy8xNTI1ODkxODc5LTM3OTcyMC13YXJyZW4td29uZy0yNDIyODYtdW5zcGxhc2hqcGcuanBn",
                        large = "large"
                    ),
                    updated_at = null,
                    first_name = null,
                    last_name = null,
                    followed_by_user = null,
                    followers_count = null,
                    following_count = null,
                    downloads = null,
                    social = null,
                    badge = null,
                    links = null,
                    tags = null,
                )

            )
        )
    }
}

@Preview(device = Devices.PHONE, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PhotoCardItemDarkPreview() {
    PlashrTheme {
        PhotoCardItem(
            photo = Photo(
                id = "1",
                null,
                null,
                null,
                null,
                "#000000",
                null,
                null,
                24,
                false,
                "Through photography, the beauty of Mother Nature can be frozen in time." + " This category celebrates the magic of our planet and beyond — from the immensity of the great outdoors, to miraculous moments in your own backyard.",
                null,
                null,
                null,/*listOf(
                    Photo.CurrentUserCollection(
                        id = null,
                        title = null,
                        published_at = null,
                        last_collected_at = null,
                        updated_at = null,
                        cover_photo = null,
                        user = null
                    )
                ),*/
                null,
                null,
                Photo.Urls(
                    null,
                    null,
                    "https://web-images3.pixpa.com/lLlWMdXI8csKLLbkcSEw2G6StDefqr8uWaadqL7vc4k/rs:fit:1200:0/q:80/aHR0cHM6Ly9waXhwYWNvbS1pbWcucGl4cGEuY29tL2NvbS9hcnRpY2xlcy8xNTI1ODkxODc5LTM3OTcyMC13YXJyZW4td29uZy0yNDIyODYtdW5zcGxhc2hqcGcuanBn",
                    null,
                    null
                ),
                null,
                user = User(
                    id = "1",
                    username = "kimothorick",
                    name = "Rick Kimotho",
                    portfolio_url = "portfolio_url",
                    bio = "bio",
                    location = "location",
                    total_likes = 1,
                    total_photos = 1,
                    total_collections = 1,
                    instagram_username = "instagram_username",
                    twitter_username = "twitter_username",
                    profile_image = User.ProfileImage(
                        small = "small",
                        medium = "https://web-images3.pixpa.com/lLlWMdXI8csKLLbkcSEw2G6StDefqr8uWaadqL7vc4k/rs:fit:1200:0/q:80/aHR0cHM6Ly9waXhwYWNvbS1pbWcucGl4cGEuY29tL2NvbS9hcnRpY2xlcy8xNTI1ODkxODc5LTM3OTcyMC13YXJyZW4td29uZy0yNDIyODYtdW5zcGxhc2hqcGcuanBn",
                        large = "large"
                    ),
                    updated_at = null,
                    first_name = null,
                    last_name = null,
                    followed_by_user = null,
                    followers_count = null,
                    following_count = null,
                    downloads = null,
                    social = null,
                    badge = null,
                    links = null,
                    tags = null,
                )

            )
        )
    }
}