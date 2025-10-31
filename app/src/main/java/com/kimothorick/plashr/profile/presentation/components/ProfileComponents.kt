package com.kimothorick.plashr.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kimothorick.plashr.R
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.profile.presentation.components.ManageAccountState.LOGGING_OUT
import com.kimothorick.plashr.profile.presentation.components.ManageAccountState.LOGOUT_FAILED
import com.kimothorick.plashr.profile.presentation.components.ManageAccountState.LOGOUT_SUCCESS
import com.kimothorick.plashr.profile.presentation.components.ManageAccountState.PROFILE_DETAILS
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.utils.tooling.ComponentPreviews
import com.kimothorick.plashr.utils.tooling.DevicePreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Represents the different states of the user login process.
 *
 * This enum is used to manage the UI displayed in the `LoginBottomSheet`,
 * reflecting the current status of the login operation.
 */
enum class LoginState {
    IDLE,
    IN_PROGRESS,
    SUCCESS,
    FAILED,
}

/**
 * Represents the different states for managing a user's account.
 *
 * This enum is used to control the UI displayed in the `ManageAccountBottomSheet`,
 * reflecting the current status of account management actions like logging out.
 *
 * - [PROFILE_DETAILS]: The default state, showing the user's profile information and options to edit or log out.
 * - [LOGGING_OUT]: Indicates that the logout process is currently in progress.
 * - [LOGOUT_SUCCESS]: Represents the successful completion of the logout process.
 * - [LOGOUT_FAILED]: Indicates that the logout attempt has failed.
 */
enum class ManageAccountState {
    PROFILE_DETAILS,
    LOGGING_OUT,
    LOGOUT_SUCCESS,
    LOGOUT_FAILED,
}

/**
 * A container class for various UI components related to the user profile screen.
 * This class groups together different composable functions that are used to build
 * the profile, login, and account management user interfaces. It includes components
 * for displaying profile icons, handling login flows via a bottom sheet, and managing
 * user account details.
 *
 * The class itself doesn't hold state but provides reusable UI building blocks.
 *
 * @see LoginBottomSheet
 * @see ManageAccountBottomSheet
 * @see ProfilePictureIcon
 */
class ProfileComponents {
    /**
     * A circular icon used as a placeholder for a user's profile picture.
     * This is typically displayed when the user is not logged in or their
     * profile picture is unavailable. It shows a generic person icon within a
     * circular background.
     *
     * @param iconSize The size of the circular icon container in Dp.
     * @param modifier The [Modifier] to be applied to the component.
     * @param onClick A lambda to be invoked when the icon is clicked.
     * @param onLongClick A lambda to be invoked when the icon is long-clicked.
     */
    @Composable
    fun ProfilePictureIcon(
        iconSize: Int,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        onLongClick: () -> Unit,
    ) {
        Box(
            modifier = modifier
                .size(iconSize.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.onSurfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.PersonOutline,
                contentDescription = stringResource(R.string.profile_picture),
                modifier = Modifier
                    .size(24.dp)
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick,
                    ),
                tint = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }

    /**
     * A composable function that displays the initial content for the login flow.
     * This view is shown when the `LoginState` is `IDLE`. It provides users with
     * the option to log in via Unsplash, information about creating an account,
     * and an option to cancel the login process.
     *
     * It includes:
     * - A title "Add Account".
     * - A descriptive message with a clickable link to the Unsplash registration page.
     * - A "Continue with Unsplash" button to initiate the login process.
     * - A "Cancel" button to dismiss the bottom sheet.
     *
     * @param modifier The [Modifier] to be applied to the component.
     * @param onContinueWithUnsplash A lambda function to be invoked when the "Continue with Unsplash" button is clicked.
     * @param onDismiss A lambda function to be invoked when the "Cancel" button is clicked.
     */
    @Composable
    private fun LoginIdleContent(
        modifier: Modifier = Modifier,
        onContinueWithUnsplash: () -> Unit,
        onDismiss: () -> Unit,
    ) {
        val annotatedLoginPrompt = buildAnnotatedString {
            append(stringResource(R.string.login_idle_message))
            append("\n\n")
            append(stringResource(R.string.login_idle_no_account_prompt))
            withLink(
                LinkAnnotation.Url(
                    Constants.PLASHR_UNSPLASH_REFERRAL_URL,
                    TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                        ),
                    ),
                ),
            ) {
                append(stringResource(R.string.login_idle_register_action))
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.add_account),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = annotatedLoginPrompt,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                shape = MaterialTheme.shapes.small,
                onClick = onContinueWithUnsplash,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.unsplash_logo),
                    contentDescription = stringResource(R.string.unsplash_icon),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.continue_with_unsplash),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(0.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                ),
                contentPadding = PaddingValues(16.dp),
                shape = MaterialTheme.shapes.small,
                onClick = onDismiss,
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }

    /**
     * A versatile composable for displaying the status of an ongoing operation,
     * such as loading, success, or failure. It dynamically shows a relevant icon
     * (loading indicator, success check, or error icon), a title, an optional message,
     * and a context-sensitive button for user action.
     *
     * This component is designed to be used within a bottom sheet or a dialog to provide
     * clear feedback to the user during asynchronous tasks like login, logout, or data submission.
     *
     * The UI state is controlled by the boolean flags `isLoading`, `hasSucceeded`, and `hasFailed`.
     * Only one of these should be `true` at any given time to display the correct state.
     *
     * @param isLoading When `true`, displays a [ContainedLoadingIndicator].
     * @param hasSucceeded When `true`, displays a success icon. A button with [successButtonText]
     *   will be shown if the text and [onSuccess] callback are provided.
     * @param hasFailed When `true`, displays an error icon. A button with [errorButtonText]
     *   will be shown if the text and [onError] callback are provided.
     * @param title The main text to be displayed, indicating the nature of the operation (e.g., "Signing In...").
     * @param message An optional, more detailed message to provide extra context to the user.
     * @param successButtonText The text for the button to be displayed on a successful operation.
     * @param errorButtonText The text for the button to be displayed on a failed operation.
     * @param onSuccess A lambda to be invoked when the success button is clicked.
     * @param onError A lambda to be invoked when the error button is clicked.
     */
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun OperationStatusContent(
        isLoading: Boolean,
        hasSucceeded: Boolean,
        hasFailed: Boolean,
        title: String,
        message: String?,
        successButtonText: String? = null,
        errorButtonText: String? = null,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when {
                isLoading -> ContainedLoadingIndicator(modifier = Modifier.size(48.dp))
                hasSucceeded -> Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    tint = Color(0xFF4CAF50),
                    contentDescription = stringResource(R.string.success),
                    modifier = Modifier.size(48.dp),
                )

                hasFailed -> Icon(
                    imageVector = Icons.Filled.Cancel,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = stringResource(R.string.error_icon),
                    modifier = Modifier.size(48.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (message != null) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Button
            when {
                hasSucceeded && successButtonText != null && onSuccess != null -> Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.small,
                    onClick = onSuccess,
                ) {
                    Text(
                        text = successButtonText,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                hasFailed && errorButtonText != null && onError != null -> Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.small,
                    onClick = onError,
                ) {
                    Text(
                        text = errorButtonText,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }

    /**
     * A modal bottom sheet that handles the entire user login flow.
     *
     * This composable displays different content based on the [LoginState], guiding the
     * user through the process of logging in with Unsplash. It manages its own visibility
     * and internal sheet state, and prevents dismissal while an operation is in progress.
     *
     * The content changes as the [loginState] transitions:
     * - [LoginState.IDLE]: Shows options to log in or cancel.
     * - [LoginState.IN_PROGRESS]: Displays a loading indicator.
     * - [LoginState.SUCCESS]: Shows a success message and a "Done" button.
     * - [LoginState.FAILED]: Displays an error message with a "Try Again" option.
     *
     * @param isVisible Controls the visibility of the bottom sheet.
     * @param onDismiss A lambda to be invoked when the sheet is requested to be dismissed
     *   (e.g., by tapping the scrim or pressing the back button). Dismissal is blocked
     *   when `loginState` is `IN_PROGRESS`.
     * @param onLoginWithUnsplash A lambda to be invoked when the user initiates the
     *   login process or retries after a failure.
     * @param loginState The current state of the login process, which determines the
     *   content displayed within the sheet.
     * @param modifier The [Modifier] to be applied to the bottom sheet.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginBottomSheet(
        isVisible: Boolean,
        onDismiss: () -> Unit,
        onLoginWithUnsplash: () -> Unit,
        loginState: LoginState,
        modifier: Modifier = Modifier,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        LaunchedEffect(isVisible, loginState, sheetState) {
            if (isVisible) {
                coroutineScope.launch {
                    sheetState.expand()
                }
            }
        }

        if (isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    if (loginState != LoginState.IN_PROGRESS) {
                        onDismiss()
                    }
                },
                sheetState = sheetState,
                modifier = modifier
                    .navigationBarsPadding()
                    .background(Color.Transparent),
                shape = MaterialTheme.shapes.extraLarge,
                sheetMaxWidth = 420.dp,
                properties = ModalBottomSheetProperties(
                    shouldDismissOnBackPress = loginState != LoginState.IN_PROGRESS,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        )
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (loginState) {
                        LoginState.IDLE -> LoginIdleContent(
                            onContinueWithUnsplash = onLoginWithUnsplash,
                            onDismiss = onDismiss,
                        )

                        LoginState.IN_PROGRESS -> OperationStatusContent(
                            isLoading = true,
                            hasSucceeded = false,
                            hasFailed = false,
                            title = stringResource(R.string.signing_in),
                            message = stringResource(R.string.please_wait_while_we_sign_you_in),
                        )

                        LoginState.SUCCESS -> OperationStatusContent(
                            isLoading = false,
                            hasSucceeded = true,
                            hasFailed = false,
                            title = stringResource(R.string.successful),
                            message = stringResource(R.string.you_re_all_set_let_s_find_some_beautiful_photos),
                            successButtonText = stringResource(R.string.done),
                            onSuccess = onDismiss,
                        )

                        LoginState.FAILED -> OperationStatusContent(
                            isLoading = false,
                            hasSucceeded = false,
                            hasFailed = true,
                            title = stringResource(R.string.something_went_wrong),
                            message = stringResource(R.string.we_couldn_t_complete_the_login_process_please_try_again),
                            errorButtonText = stringResource(R.string.try_again),
                            onError = onLoginWithUnsplash,
                        )
                    }
                }
            }
        }
    }

    /**
     * A modal bottom sheet for managing a logged-in user's account.
     *
     * This composable provides a user interface for viewing basic profile information,
     * editing the profile, and logging out. The content displayed within the sheet
     * is determined by the [manageAccountState], which reflects the current stage of
     * an operation (e.g., displaying profile details, showing a logout progress indicator,
     * or confirming logout success/failure).
     *
     * The sheet is not dismissible while a logout operation is in progress
     * ([ManageAccountState.LOGGING_OUT]).
     *
     * @param isVisible Controls the visibility of the bottom sheet.
     * @param onDismiss A lambda to be invoked when the sheet is requested to be dismissed.
     *   This is ignored when `manageAccountState` is `LOGGING_OUT`.
     * @param onLogout A lambda to be invoked when the user clicks the "Logout" button.
     * @param onEditProfile A lambda to be invoked when the user clicks the edit profile icon.
     * @param profilePictureUrl The URL of the user's profile picture. Can be null.
     * @param firstName The user's first name. Can be null.
     * @param lastName The user's last name. Can be null.
     * @param username The user's username. Can be null.
     * @param modifier The [Modifier] to be applied to the bottom sheet.
     * @param manageAccountState The current state of the account management flow, which
     *   dictates the content shown (e.g., profile details, logging out, success, failure).
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ManageAccountBottomSheet(
        isVisible: Boolean,
        onDismiss: () -> Unit,
        onLogout: () -> Unit,
        onEditProfile: () -> Unit,
        profilePictureUrl: String?,
        firstName: String?,
        lastName: String?,
        username: String?,
        modifier: Modifier = Modifier,
        manageAccountState: ManageAccountState,
    ) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        if (isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    if (manageAccountState != LOGGING_OUT) {
                        onDismiss()
                    }
                },
                sheetState = sheetState,
                modifier = modifier
                    .navigationBarsPadding()
                    .background(Color.Transparent),
                shape = MaterialTheme.shapes.extraLarge,
                sheetMaxWidth = 420.dp,
                properties = ModalBottomSheetProperties(
                    shouldDismissOnBackPress = manageAccountState != LOGGING_OUT,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        )
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (manageAccountState) {
                        PROFILE_DETAILS -> {
                            Text(
                                text = stringResource(R.string.manage_account),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(MaterialTheme.shapes.extraLarge),
                                        model = profilePictureUrl,
                                        contentDescription = null,
                                    )
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .weight(1f),
                                        verticalArrangement = Arrangement.Center,
                                    ) {
                                        Text(
                                            text = "$firstName $lastName",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.titleSmall,
                                        )
                                        Text(
                                            text = "@$username",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            onEditProfile()
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Edit,
                                            contentDescription = stringResource(R.string.edit),
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(16.dp),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    onLogout()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                                    contentDescription = stringResource(R.string.logout),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.logout),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(0.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                ),
                                contentPadding = PaddingValues(16.dp),
                                shape = MaterialTheme.shapes.small,
                                onClick = { onDismiss() },
                            ) {
                                Text(text = stringResource(R.string.cancel))
                            }
                        }

                        LOGGING_OUT -> OperationStatusContent(
                            isLoading = true,
                            hasSucceeded = false,
                            hasFailed = false,
                            title = stringResource(R.string.logging_out),
                            message = stringResource(R.string.logging_out_description),
                        )

                        LOGOUT_SUCCESS -> OperationStatusContent(
                            isLoading = false,
                            hasSucceeded = true,
                            hasFailed = false,
                            title = stringResource(R.string.logged_out),
                            message = stringResource(R.string.logged_out_description_success),
                            successButtonText = stringResource(R.string.done),
                            onSuccess = { onDismiss() },
                        )

                        LOGOUT_FAILED -> OperationStatusContent(
                            isLoading = false,
                            hasSucceeded = false,
                            hasFailed = true,
                            title = stringResource(R.string.logout_failed),
                            message = stringResource(R.string.logout_failed_description),
                            errorButtonText = stringResource(R.string.retry_button),
                            onError = {
                                onLogout()
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                LaunchedEffect(manageAccountState) {
                    if (manageAccountState == LOGOUT_SUCCESS) {
                        delay(1500)
                        onDismiss()
                    }
                }
            }
        }
    }

    //region LoginState Components Previews
    @ComponentPreviews
    @Composable
    private fun LoginIdleContentPreview() {
        PlashrTheme {
            Surface {
                LoginIdleContent(
                    onContinueWithUnsplash = {},
                    onDismiss = {},
                )
            }
        }
    }

    @ComponentPreviews
    @Composable
    private fun OperationStatusContentInProgressPreview() {
        PlashrTheme {
            Surface {
                OperationStatusContent(
                    isLoading = true,
                    hasSucceeded = false,
                    hasFailed = false,
                    title = "Processing...",
                    message = "Please wait.",
                )
            }
        }
    }

    @ComponentPreviews
    @Composable
    private fun OperationStatusContentSuccessPreview() {
        PlashrTheme {
            Surface {
                OperationStatusContent(
                    isLoading = false,
                    hasSucceeded = true,
                    hasFailed = false,
                    title = "All Good!",
                    message = "Operation completed successfully.",
                    successButtonText = "OK",
                    onSuccess = {},
                )
            }
        }
    }

    @ComponentPreviews
    @Composable
    private fun OperationStatusContentFailedPreview() {
        PlashrTheme {
            Surface {
                OperationStatusContent(
                    isLoading = false,
                    hasSucceeded = false,
                    hasFailed = true,
                    title = "Oops!",
                    message = "Something went wrong.",
                    errorButtonText = "Try Again",
                    onError = {},
                )
            }
        }
    }

    //endregion

    //region Previews for LoginBottomSheet using LoginState
    @DevicePreview(name = "LoginBottomSheet - IDLE")
    @Composable
    fun LoginBottomSheetIdlePreview() {
        PlashrTheme {
            Surface {
                LoginBottomSheet(
                    isVisible = true,
                    onDismiss = {},
                    onLoginWithUnsplash = {},
                    loginState = LoginState.IDLE,
                )
            }
        }
    }

    @DevicePreview(name = "LoginBottomSheet - IN PROGRESS")
    @Composable
    fun LoginBottomSheetInProgressPreview() {
        PlashrTheme {
            Surface {
                LoginBottomSheet(
                    isVisible = true,
                    onDismiss = {},
                    onLoginWithUnsplash = {},
                    loginState = LoginState.IN_PROGRESS,
                )
            }
        }
    }

    @DevicePreview(name = "LoginBottomSheet - SUCCESS")
    @Composable
    fun LoginBottomSheetSuccessPreview() {
        PlashrTheme {
            Surface {
                LoginBottomSheet(
                    isVisible = true,
                    onDismiss = {},
                    onLoginWithUnsplash = {},
                    loginState = LoginState.SUCCESS,
                )
            }
        }
    }

    @DevicePreview(name = "LoginBottomSheet - FAILED")
    @Composable
    fun LoginBottomSheetFailedPreview() {
        PlashrTheme {
            Surface {
                LoginBottomSheet(
                    isVisible = true,
                    onDismiss = {},
                    onLoginWithUnsplash = {},
                    loginState = LoginState.FAILED,
                )
            }
        }
    }
    //endregion

    //region Previews for ManageAccountBottomSheet

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(name = "ManageAccountBottomSheet")
    @Composable
    fun ManageAccountBottomSheetPreview() {
        PlashrTheme {
            Surface {
                ManageAccountBottomSheet(
                    isVisible = true,
                    onDismiss = {},
                    onLogout = {},
                    onEditProfile = {},
                    profilePictureUrl = null,
                    firstName = "Rick",
                    lastName = "Kimotho",
                    username = "kimothorick",
                    manageAccountState = PROFILE_DETAILS,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(name = "ManageAccountBottomSheet")
    @Composable
    fun LogoutBottomSheetPreview() {
        PlashrTheme {
            Surface {
                ManageAccountBottomSheet(
                    isVisible = true,
                    onDismiss = {},
                    onLogout = {},
                    onEditProfile = {},
                    profilePictureUrl = null,
                    firstName = "Rick",
                    lastName = "Kimotho",
                    username = "kimothorick",
                    manageAccountState = LOGGING_OUT,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(name = "ManageAccountBottomSheet")
    @Composable
    fun LogoutSuccessfulBottomSheetPreview() {
        PlashrTheme {
            Surface {
                ManageAccountBottomSheet(
                    isVisible = true,
                    onDismiss = {},
                    onLogout = {},
                    onEditProfile = {},
                    profilePictureUrl = null,
                    firstName = "Rick",
                    lastName = "Kimotho",
                    username = "kimothorick",
                    manageAccountState = LOGOUT_SUCCESS,
                )
            }
        }
    }

    //endregion
}
