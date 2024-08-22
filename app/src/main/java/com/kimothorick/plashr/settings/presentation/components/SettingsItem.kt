package com.kimothorick.plashr.settings.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.kimothorick.plashr.settings.data.SettingOption

/**
 * A Composable function that displays a single setting item.
 *
 * This function renders a [ListItem] with a title,optional icon, and optional description.
 * It handles clicks on the item by invoking the provided `onClick` lambda function.
 *
 * @param option The [SettingOption] object representing the setting to display.
 * @param onClick A lambda function that is invoked when the setting item is clicked, passing theclicked [SettingOption] as an argument.
 */
@Composable
fun SettingItem(option: SettingOption, onClick: (SettingOption) -> Unit) {
    ListItem(modifier = Modifier.clickable {onClick(option)}, headlineContent = {
        Text(
            text = option.title, style = MaterialTheme.typography.titleSmall
        )
    }, leadingContent = if (option.icon != null) {
        {
            // Handle different icon types (ImageVector or Drawable resource ID)
            if (option.icon is ImageVector) {
                Icon(
                    imageVector = option.icon, contentDescription = option.description
                )
            } else {
                // Assuming option.icon is a Drawable resource ID
                val bitmap = LocalContext.current.resources.getDrawable(option.icon as Int, null)
                    ?.toBitmap()
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                        contentDescription = option.description
                    )
                }
            }
        }
    } else {
        null
    }, supportingContent = {
        // Display the setting description if it's not null
        if (option.description != null) {
            Text(text = option.description, style = MaterialTheme.typography.bodyMedium)
        }
    })
}