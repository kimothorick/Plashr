package com.kimothorick.plashr.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimothorick.plashr.R
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.utils.tooling.ComponentPreviews

/**
 * A composable that displays a full-screen error state.
 * It includes an error icon, an optional title, a descriptive message,
 * and a "Try again" button to trigger a retry action.
 *
 * @param errorTitle An optional title for the error. If null, no title is displayed.
 * @param errorMessage The main descriptive message for the error.
 * @param onRetry A lambda function to be invoked when the "Try again" button is clicked.
 * @param modifier The [Modifier] to be applied to this composable.
 */
@Composable
fun ErrorView(
    errorTitle: String?,
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.error),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(80.dp),
            )
            if (errorTitle != null) {
                Text(
                    text = errorTitle,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = errorMessage,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = if (errorTitle != null) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }

        OutlinedButton(onClick = onRetry) {
            Text(text = "Try again", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * A Composable that displays a banner to indicate the current network connection status.
 * It's typically shown at the top or bottom of the screen.
 *
 * @param text The message to display inside the status banner, e.g., "Connecting..." or "No connection".
 * @param modifier The [Modifier] to be applied to this component.
 */
@Composable
fun ConnectionStatus(
    isConnected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = !isConnected,
        enter = slideInVertically { fullHeight -> -fullHeight },
        exit = slideOutVertically { fullHeight -> -fullHeight },
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.onBackground)
                .padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * A compact, inline composable for displaying an error message with a retry option.
 * This is typically used at the bottom of a list or within a component to indicate a
 * localized failure, such as failing to load more items.
 *
 * @param errorMessage The descriptive error message to display.
 * @param modifier The [Modifier] to be applied to this composable.
 * @param onRetry A lambda function that is invoked when the "Try again" text is clicked.
 */
@Composable
fun InlineErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.try_again),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                onRetry()
            },
        )
    }
}

//region Previews
@Preview
@Composable
fun ConnectionErrorPreview() {
    PlashrTheme {
        Surface { ConnectionStatus(isConnected = false, "No connection") }
    }
}

@ComponentPreviews
@Composable
fun InlineErrorPreview() {
    PlashrTheme {
        Surface {
            InlineErrorView(errorMessage = "Sorry, something went wrong.") {}
        }
    }
}

@ComponentPreviews
@Composable
fun ErrorViewPreview() {
    PlashrTheme {
        Surface {
            ErrorView(
                errorTitle = "You're offline",
                errorMessage = "Please check your internet connection and try again",
                onRetry = {},
            )
        }
    }
}

//endregion
