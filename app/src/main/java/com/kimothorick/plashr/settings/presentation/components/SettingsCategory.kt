package com.kimothorick.plashr.settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kimothorick.plashr.settings.data.SettingCategory
import com.kimothorick.plashr.settings.data.SettingOption

/**
 * A Composable function that displays a category of settings.
 *
 * This function renders a [Column] that displays the categorytitle (if available) and a list of
 * individual setting items using the [SettingItem] composable.
 *
 * @param category The [SettingCategory] object containing the title and list of settings to display.
 * @param onSettingClick A lambda function that is invoked when a setting item isclicked, passing the clicked [SettingOption] as an argument.
 */
@Composable
fun SettingsCategory(category: SettingCategory, onSettingClick: (SettingOption) -> Unit) {
    Column {
        // Display the category title if it's not null
        category.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
        }
        // Iterate through the settings and display each one
        category.settings.forEach {option ->
            SettingItem(option, onSettingClick)
        }
    }
}