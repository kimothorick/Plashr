package com.kimothorick.plashr.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kimothorick.plashr.R

/**
 * A composable that displays a generic empty state message.
 * It shows a predefined image and a customizable text message, centered on the screen.
 * This is typically used to indicate that a search returned no results or a list is empty.
 *
 * @param emptyStateMessage The message to be displayed to the user.
 */
@Composable
fun EmptyStateComponent(
    emptyStateMessage: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(16.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.search_empty),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(80.dp),
        )
        Text(
            text = emptyStateMessage,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

/**
 * A composable that indicates the end of a paginated list has been reached.
 * It displays a centered text message, typically shown after the last item in a list
 * to inform the user that there is no more content to load.
 */
@Composable
fun EndOfPagingComponent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(R.string.end_of_pagination),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
