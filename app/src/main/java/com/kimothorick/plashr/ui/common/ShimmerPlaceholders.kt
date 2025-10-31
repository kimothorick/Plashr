package com.kimothorick.plashr.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

/**
 * A composable that displays a placeholder for a photo with a shimmering effect.
 * It's used to indicate that content is loading. The container is a square box
 * that fills the maximum width available to it.
 *
 * @param modifier The [Modifier] to be applied to the container.
 */
@Composable
fun ShimmeringPhotoContainer(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .shimmer()
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    )
}

/**
 * A composable that displays a placeholder for a single line of text with a shimmering effect.
 * It's used to indicate that text content is loading.
 *
 * @param modifier The [Modifier] to be applied to this placeholder.
 * @param height The height of the text line placeholder. Defaults to 20.dp.
 */
@Composable
private fun ShimmeringTextLinePlaceholder(
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 20.dp,
) {
    Box(
        modifier = modifier
            .height(
                height,
            )
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    )
}

/**
 * A composable that displays a shimmering square placeholder, typically used for icons while they are loading.
 *
 * @param modifier The [Modifier] to be applied to the placeholder.
 * @param size The size of the square placeholder. Defaults to 24.dp.
 */
@Composable
private fun ShimmeringIconPlaceholder(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 24.dp,
) {
    Box(
        modifier = modifier
            .size(
                size,
            )
            .shimmer()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    )
}

/**
 * A shimmering placeholder for a header row typically found in information sections.
 *
 * This composable displays a layout with a shimmering text line on the left and a
 * row of three shimmering icon placeholders on the right, separated by a spacer. It's
 * designed to represent a loading state for a header that might contain a title and action icons.
 *
 * @param modifier The modifier to be applied to the placeholder layout.
 */
@Composable
private fun ShimmeringInfoHeaderPlaceholder(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShimmeringTextLinePlaceholder(modifier = Modifier.fillMaxWidth(0.4f))
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(3) {
                ShimmeringIconPlaceholder()
            }
        }
    }
}

/**
 * A shimmer placeholder composable that mimics the layout of a user profile section.
 *
 * This placeholder displays a circular avatar shape on the left, followed by two lines of text
 * placeholders of different lengths to represent the user's name and additional information (e.g., username).
 * It's designed to be used while the actual user profile data is loading.
 *
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
private fun ShimmeringUserProfilePlaceholder(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(
                    48.dp,
                )
                .shimmer()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        )
        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            ShimmeringTextLinePlaceholder(modifier = Modifier.fillMaxWidth(0.4f))
            ShimmeringTextLinePlaceholder(modifier = Modifier.fillMaxWidth(0.2f), height = 16.dp)
        }
    }
}

/**
 * A private composable that displays a placeholder for a location.
 * It consists of a shimmering icon (presumably for a location pin) followed by a short
 * shimmering text line, arranged horizontally. This is typically used as a loading
 * indicator before the actual location data is available.
 *
 * @param modifier The modifier to be applied to the placeholder row.
 */
@Composable
private fun ShimmeringLocationPlaceholder(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        ShimmeringIconPlaceholder()
        ShimmeringTextLinePlaceholder(modifier = Modifier.fillMaxWidth(0.3f))
    }
}

/**
 * A shimmering placeholder for a section that displays EXIF data.
 * It shows several rows, each containing two shimmering text lines, simulating a key-value pair layout.
 *
 * @param modifier The [Modifier] to be applied to the placeholder.
 */
@Composable
private fun ShimmeringExifDataRowsPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(4) {
            Row(modifier = Modifier.fillMaxWidth()) {
                ShimmeringTextLinePlaceholder(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                ShimmeringTextLinePlaceholder(modifier = Modifier.weight(1f))
            }
        }
    }
}

/**
 * A composable that displays a placeholder for a row of tags,
 * typically used while the actual tag data is loading. It shows
 * a horizontal row of shimmering boxes to indicate a loading state.
 *
 * @param modifier The [Modifier] to be applied to this placeholder.
 */
@Composable
private fun ShimmeringTagsPlaceholder(
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false,
    ) {
        items(count = 5) {
            Box(
                modifier = Modifier
                    .height(
                        32.dp,
                    )
                    .width(80.dp)
                    .shimmer()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            )
        }
    }
}

/**
 * A composable that displays a shimmering placeholder for the "More Info" section of a photo details screen.
 * This is used to indicate that content is loading.
 *
 * It combines several smaller placeholder composables to represent various pieces of information,
 * such as a header, user profile, location, description, EXIF data, and tags.
 *
 * @param modifier The [Modifier] to be applied to the container.
 */
@Composable
fun ShimmeringMorePhotoInfoContainer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ShimmeringInfoHeaderPlaceholder()
        ShimmeringUserProfilePlaceholder()
        ShimmeringLocationPlaceholder()
        ShimmeringTextLinePlaceholder(modifier = Modifier.fillMaxWidth(0.7f))
        ShimmeringTextLinePlaceholder(modifier = Modifier.fillMaxWidth())
        ShimmeringExifDataRowsPlaceholder()
        ShimmeringTagsPlaceholder()
    }
}
