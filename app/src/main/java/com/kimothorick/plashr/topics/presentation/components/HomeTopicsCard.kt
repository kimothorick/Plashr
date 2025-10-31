package com.kimothorick.plashr.topics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.window.core.layout.WindowSizeClass
import coil3.compose.AsyncImage
import com.kimothorick.plashr.data.models.topics.Topic.CoverPhoto
import com.kimothorick.plashr.utils.WindowWidthClass
import com.kimothorick.plashr.utils.windowWidthSize
import com.valentinilk.shimmer.shimmer

/**
 * A Composable that displays a card for a topic on the home screen.
 * The card shows a cover photo with the topic title overlaid.
 * It is clickable and its size adapts to different window width classes.
 *
 * @param title The title of the topic to be displayed on the card.
 * @param photo The [CoverPhoto] object containing the image URL and a placeholder color.
 * @param onTopicClicked A lambda function to be invoked when the card is clicked.
 * @param modifier The [Modifier] to be applied to the card.
 * @param windowSizeClass The [WindowSizeClass] used to determine the card's dimensions for responsive layout.
 */
@Composable
fun HomeTopicsCard(
    title: String,
    photo: CoverPhoto,
    onTopicClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
) {
    val (cardWidth, cardHeight) = when (windowSizeClass.windowWidthSize()) {
        WindowWidthClass.COMPACT -> 160.dp to 110.dp
        WindowWidthClass.MEDIUM -> 180.dp to 110.dp
        WindowWidthClass.EXPANDED -> 200.dp to 110.dp
    }

    Box(
        modifier = modifier
            .size(cardWidth, cardHeight)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onTopicClicked),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onTopicClicked() },
            model = photo.urls!!.regular,
            contentScale = ContentScale.Crop,
            contentDescription = "",
            placeholder = remember(photo.color) {
                val color = Color(photo.color!!.toColorInt())
                ColorPainter(color)
            },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                modifier = modifier.align(Alignment.Center),
            )
        }
    }
}

/**
 * A composable that displays a shimmer effect as a placeholder for a [HomeTopicsCard].
 * This is typically used to indicate that content is being loaded.
 * The size of the shimmer card adapts to the available screen width.
 *
 * @param windowSizeClass The window size class used to determine the card's dimensions.
 */
@Composable
fun HomeTopicsCardShimmer(
    windowSizeClass: WindowSizeClass,
) {
    val (cardWidth, cardHeight) = when (windowSizeClass.windowWidthSize()) {
        WindowWidthClass.COMPACT -> 160.dp to 110.dp
        WindowWidthClass.MEDIUM -> 180.dp to 110.dp
        WindowWidthClass.EXPANDED -> 200.dp to 110.dp
    }

    Box(
        modifier = Modifier
            .shimmer()
            .size(cardWidth, cardHeight)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    )
}
