package com.kimothorick.plashr.photoDetails.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.kimothorick.plashr.R
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage

/**
 * A composable that displays a photo in a full-screen, zoomable view.
 *
 * This screen provides an immersive experience for viewing a single photo. It includes a
 * top app bar with a back button that, along with the system bars, can be toggled on and off
 * by tapping on the image. The image itself is loaded asynchronously and supports pinch-to-zoom
 * and pan gestures.
 *
 * @param modifier The [Modifier] to be applied to the component.
 * @param photoURL The URL of the photo to be displayed.
 * @param onBackClicked A lambda function to be invoked when the back button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhotoFullPreviewScreen(
    modifier: Modifier = Modifier,
    photoURL: String,
    onBackClicked: () -> Unit,
) {
    var areControlsVisible by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            if (areControlsVisible) {
                TopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.clip(shape = MaterialTheme.shapes.large),
                            onClick = {
                                onBackClicked()
                            },
                            colors =
                                IconButtonColors(
                                    containerColor = colorResource(id = R.color.translucent_nav_icon_background),
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = Color.Transparent,
                                ),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                            )
                        }
                    },
                    modifier = Modifier.wrapContentHeight(),
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                )
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        PaddingValues(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            bottom =
                                WindowInsets.navigationBars.asPaddingValues()
                                    .calculateBottomPadding(),
                        ),
                    ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ZoomableAsyncImage(
                model = photoURL,
                contentDescription = null,
                modifier = modifier.fillMaxSize(),
                onClick = {
                    areControlsVisible = !areControlsVisible
                },
            )
        }
    }
}
