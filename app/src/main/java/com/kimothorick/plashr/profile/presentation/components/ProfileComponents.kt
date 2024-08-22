package com.kimothorick.plashr.profile.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kimothorick.plashr.R

/**
 * A class containing composable components related to user profiles.
 */
class ProfileComponents {
    /**
     * A composable that displays a placeholder icon for a profile picture.
     *
     * @param modifier Modifier used to adjust the layout or appearance of the icon.
     * @param profilePictureSize The size of the profile picture icon in dp.
     */
    @Composable
    fun ProfilePictureIcon(
        modifier: Modifier, profilePictureSize: Int
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = "Profile Picture Placeholder",
            modifier = modifier
                .size(profilePictureSize.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }

    /**
     * A composable that displays a bottom sheet for user login.
     *
     * @param showBottomSheet A boolean flag indicating whether to show the bottom sheet.
     * @param onDismiss A lambda function to be called when the bottom sheet is dismissed.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginBottomSheet(
        showBottomSheet: Boolean, onDismiss: () -> Unit
    ) {
        val sheetState = rememberModalBottomSheetState()

        // Launch effect to show/hide based on state
        LaunchedEffect(showBottomSheet) {
            if (showBottomSheet) {
                sheetState.show()
            } else {
                sheetState.hide()
            }
        }
        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    onDismiss()
                },
                sheetState = sheetState,
                modifier = Modifier
                    .padding(
                        vertical = 28.dp, horizontal = 10.dp
                    )
                    .background(Color.Transparent),
                shape = MaterialTheme.shapes.extraLarge,
                sheetMaxWidth = 380.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Text(
                        text = "Add account", style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Sign in with your Unsplash account to save photos, create collections, curate inspiration boards, " + "and explore more features.\n" + "\n" + "Don’t have an account? Register now",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                            Log.i(
                                "Login", "LoginBottomSheet: continue with unsplash clicked."
                            )
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.unsplash_logo),
                            contentDescription = "Unsplash Icon"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Continue with unsplash",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(modifier = Modifier
                        .fillMaxWidth()
                        .shadow(0.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                        ),
                        contentPadding = PaddingValues(16.dp),
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                            onDismiss()
                        }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}