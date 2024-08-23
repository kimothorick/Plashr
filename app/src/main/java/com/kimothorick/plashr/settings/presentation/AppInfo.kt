package com.kimothorick.plashr.settings.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kimothorick.plashr.R
import com.kimothorick.plashr.settings.data.SettingAction
import com.kimothorick.plashr.settings.data.SettingCategory
import com.kimothorick.plashr.settings.data.SettingOption
import com.kimothorick.plashr.settings.presentation.components.SettingsCategory
import com.kimothorick.plashr.utils.OpenLink

/**
 * The App Info screen.
 *
 * This composable displays information about the app, including version, developer credits,
 *and links to external resources like GitHub and the Play Store.
 *
 * @param navController The [NavHostController] used to manage navigation.
 * @param context The application context.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(navController: NavHostController, context: Context) {
    val rickInstagramURL = stringResource(id = R.string.rick_instagram_handle)
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val appInfoCategories = listOf(
        SettingCategory(
            title = "App", settings = listOf(
                SettingOption(
                    title = "App Version",
                    description = "v1.0.0",
                    icon = Icons.Outlined.Info,
                    iconDescription = "Info icon"
                ), SettingOption(
                    title = "Rate app",
                    description = "Give us a rating on the Play Store",
                    icon = Icons.Outlined.ThumbUp,
                    iconDescription = "Thumb up icon"
                ), SettingOption(
                    title = "Github",
                    description = "View source code on Github",
                    icon = R.drawable.github_mark,
                    iconDescription = "Github icon"
                ), SettingOption(
                    title = "Report bug",
                    description = "Even the best apps have bugs. Report yours here!",
                    icon = Icons.Outlined.BugReport,
                    iconDescription = "Report bug icon"
                )
            )
        ), SettingCategory(
            title = null, settings = listOf(
                SettingOption(
                    title = "Licences", description = "Open source licences"
                )
            )
        ), SettingCategory(
            title = "Developer credits", settings = listOf(
                SettingOption(
                    title = "Instagram",
                    description = "@kimothorick",
                    icon = R.drawable.instagram_logo,
                    iconDescription = "Instagram logo",
                    action = SettingAction.OpenInstagram
                )
            )
        )
    )
    // Centralized function to handle setting clicks
    val handleSettingClick: (SettingOption) -> Unit = { option ->
        when (option.action) {
            SettingAction.OpenInstagram -> {
                Log.i("AppInfoScreen", "AppInfoScreen: Instagram icon clicked")
                OpenLink(url = rickInstagramURL, context = context)
            }

            else -> {}
        }
    }

    Scaffold(topBar = {
        LargeTopAppBar(modifier = Modifier.wrapContentHeight(),

            title = { Text(text = "App Info ") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back arrow"
                    )
                }
            }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .padding(top = 24.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(appInfoCategories) { category ->
                SettingsCategory(category = category, handleSettingClick)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}