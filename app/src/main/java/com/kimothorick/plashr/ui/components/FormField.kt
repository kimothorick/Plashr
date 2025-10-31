package com.kimothorick.plashr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * A composable that provides a consistent layout for form fields,
 * displaying a label above the provided content.
 *
 * This component is useful for creating structured forms where each input
 * field (like a TextField, DropdownMenu, etc.) needs a corresponding label.
 * It uses a [Column] to vertically arrange the label and the content.
 *
 * @param label The text to be displayed as the label for the form field.
 * @param content The composable content of the form field, typically an input element.
 */
@Composable
fun FormField(
    label: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        content()
    }
}
