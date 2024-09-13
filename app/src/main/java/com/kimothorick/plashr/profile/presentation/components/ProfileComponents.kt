package com.kimothorick.plashr.profile.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kimothorick.plashr.MainViewModel
import com.kimothorick.plashr.R
import com.kimothorick.plashr.profile.domain.ProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Represents the different states of a login operation.
 */
enum class LoginState {
    /**
     * The initial state before any login attempt.
     */
    Initial,

    /**
     *  Indicates that the login operation is in progress.
     */
    LoggingIn,

    /**
     *  Signifies that the login attempt has failed.
     */
    LoginFailed,

    /**
     *  Indicates that the login attempt was successful.
     */
    LoginSuccess
}

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
     * This bottom sheet dynamically adjusts its content and appearance based on the current [LoginState].
     *
     * @param showBottomSheet A boolean flag indicating whether to show the bottom sheet. This parameter
     * is deprecated and the composable now automatically manages its visibility based on the `loginState`.
     * @param onDismiss A lambda function to be called when the bottom sheet is dismissed. This is typically used
     * to update the state of the calling composable.
     * @param continueWithUnsplash A lambda function to be called when the user chooses to continue with Unsplash
     * for login. This usually triggers the login process.
     * @param viewModel The [MainViewModel] instance used to observe the [LoginState] and trigger login actions.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginBottomSheet(
        showBottomSheet: Boolean,
        onDismiss: () -> Unit,
        continueWithUnsplash: () -> Unit,
        viewModel: MainViewModel
    ) {
        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()
        val loginState by viewModel.loginState.collectAsState()

        // Automatically show or hide the bottom sheet based on `loginState`
        LaunchedEffect(loginState) {
            when (loginState) {
                LoginState.LoginSuccess -> {
                    delay(1000)
                    sheetState.hide()
                    onDismiss()
                }

                LoginState.LoginFailed, LoginState.Initial -> sheetState.show()
                else -> {}
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                sheetState = sheetState,
                modifier = Modifier
                    .padding(
                        vertical = 28.dp, horizontal = 10.dp
                    )
                    .background(Color.Transparent),
                shape = MaterialTheme.shapes.extraLarge,
                sheetMaxWidth = 380.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Text(
                        text = when (loginState) {
                            LoginState.Initial -> "Add account"
                            LoginState.LoggingIn -> "Logging in"
                            LoginState.LoginFailed -> "Login failed"
                            LoginState.LoginSuccess -> "Login successful"
                        }, style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    when (loginState) {
                        LoginState.LoggingIn -> {
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .width(56.dp)
                                        .height(56.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            }
                        }

                        LoginState.LoginSuccess -> {
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                    Icon(
                                        imageVector = Icons.Outlined.CheckCircle,
                                        tint = Color.Green,
                                        contentDescription = "checkmark icon",
                                        modifier = Modifier.size(56.dp),
                                    )
                                }
                            }
                        }

                        else -> {}

                    }
                    Text(
                        text = when (loginState) {
                            LoginState.Initial -> "Login with your Unsplash account to save photos, " + "create collections, curate inspiration boards, and explore more " + "features.\n \n Don’t have an account? Register now."
                            LoginState.LoggingIn -> "Authorizing with Unsplash..."
                            LoginState.LoginFailed -> "Something went wrong. Please try again."
                            LoginState.LoginSuccess -> "You're logged in! Restarting to load your personalized experience..."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    when (loginState) {
                        LoginState.Initial, LoginState.LoginFailed -> {
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                Button(modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(16.dp),
                                    shape = MaterialTheme.shapes.small,
                                    onClick = {
                                        continueWithUnsplash()
                                    }) {
                                    if (loginState == LoginState.Initial) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.unsplash_logo),
                                            contentDescription = "Unsplash Icon"
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (loginState == LoginState.Initial) "Continue with unsplash" else "Retry",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }

                        else -> {}
                    }

                    when (loginState) {
                        LoginState.Initial, LoginState.LoginFailed -> {
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
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        onDismiss()
                                    }
                                }) {
                                Text(text = "Cancel")
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    /**
     * A composable that displays a bottom sheet for managing the user account.
     *
     * This bottom sheet allows users to view their profile information and log out of the application.
     * It dynamically updates its content to display a logout success message after the user logs out.
     *
     * @param showBottomSheet A boolean flag indicating whether to show the bottom sheet.
     * @param onDismiss A lambda function to be called when the bottom sheet isdismissed.
     * @param logout A lambda function to be called when the user clicks the "Logout" button.
     * @param mainViewModel The [MainViewModel] instance used to perform logout actions and update the UI.
     * @param profileViewModel The [ProfileViewModel] instance used to display the user's profile information.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ManageAccountBottomSheet(
        showBottomSheet: Boolean,
        onDismiss: () -> Unit,
        logout: () -> Unit,
        mainViewModel: MainViewModel,
        profileViewModel: ProfileViewModel
    ) {
        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState()
        var showLogoutMessage by remember {mutableStateOf(false)}

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                sheetState = sheetState,
                modifier = Modifier
                    .padding(
                        vertical = 28.dp, horizontal = 10.dp
                    )
                    .background(Color.Transparent),
                shape = MaterialTheme.shapes.extraLarge,
                sheetMaxWidth = 380.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Text(
                        text = if (showLogoutMessage) "Logout Successful" else "Manage Account",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (!showLogoutMessage) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(MaterialTheme.shapes.extraLarge),
                                    model = profileViewModel.profilePictureUrl.collectAsState(
                                        initial = null
                                    ).value,
                                    contentDescription = null,
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1f),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${
                                            profileViewModel.firstName.collectAsState(
                                                initial = null
                                            ).value
                                        } ${
                                            profileViewModel.lastName.collectAsState(
                                                initial = null
                                            ).value
                                        }",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = if (profileViewModel.username.collectAsState(initial = null).value == null) "" else "@${
                                            profileViewModel.username.collectAsState(
                                                initial = null
                                            ).value
                                        }",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                IconButton(onClick = {
                                    //TODO Add edit user profile functionality
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Edit,
                                        contentDescription = "Edit Icon"
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            shape = MaterialTheme.shapes.small,
                            onClick = {
                                logout()
                                mainViewModel.logout()
                                showLogoutMessage = true
                            }) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Logout,
                                contentDescription = "Logout Icon"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Logout", style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

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
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            tint = Color.Green,
                            contentDescription = "checkmark icon",
                            modifier = Modifier.size(56.dp),
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "You're logged out!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    }
                }


                LaunchedEffect(key1 = showLogoutMessage) {
                    if (showLogoutMessage) {
                        delay(1000)
                        sheetState.hide()
                        onDismiss()
                        showLogoutMessage = false
                    }
                }
            }

        }
    }

}


