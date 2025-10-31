package com.kimothorick.plashr.profile.presentation.editprofile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimothorick.plashr.R
import com.kimothorick.plashr.ui.components.FormField
import com.kimothorick.plashr.ui.theme.extendedColors
import kotlinx.coroutines.delay

/**
 * A composable screen that allows the user to edit their profile information.
 *
 * This screen displays a form with fields for first name, last name, email, username, bio,
 * location, portfolio URL, and Instagram username. It uses a [EditProfileScreenViewModel]
 * to manage the state of the form fields and to handle saving the profile data.
 *
 * The UI consists of a [Scaffold] with a [TopAppBar] that includes a back button and a save button.
 * The main content is a [LazyColumn] containing the input fields.
 *
 * The screen also displays status banners at the top to indicate loading, success, or error states
 * when fetching or saving the profile. These banners are animated and can be dismissed.
 *
 * @param modifier The modifier to be applied to the screen's root component.
 * @param onBackClicked A lambda function to be invoked when the back button in the top app bar is clicked.
 * @param viewModel The instance of [EditProfileScreenViewModel] that provides state and handles business logic for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    viewModel: EditProfileScreenViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.editProfileScreenState.collectAsState()
    val profileSavedMessage = stringResource(R.string.profile_saved)
    val profileUpdatingMessage = stringResource(R.string.updating_account)
    val profileUpdateErrorMessage = stringResource(R.string.error_updating_profile)
    val isStatusBannerVisible = uiState !is EditProfileScreenState.Idle

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .imePadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_profile),
                        fontSize = 20.sp,
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveProfile(
                                updatingProfileMessage = profileUpdatingMessage,
                                updateProfileErrorMessage = profileUpdateErrorMessage,
                                profileUpdateSuccessMessage = profileSavedMessage,
                            )
                        },
                    ) {
                        Text(text = stringResource(R.string.save), color = MaterialTheme.colorScheme.onSurface)
                    }
                },
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .animateContentSize(),
        ) {
            AnimatedVisibility(
                visible = isStatusBannerVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                val bannerBackgroundColor = when (uiState) {
                    is EditProfileScreenState.Error -> extendedColors.errorColor
                    is EditProfileScreenState.Success -> extendedColors.successColor
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                when (uiState) {
                    is EditProfileScreenState.Success -> {
                        if ((uiState as EditProfileScreenState.Success).loadType == LoadType.PROFILE_UPDATE) {
                            StatusTopBanner(
                                showIcon = true,
                                message = stringResource(R.string.profile_saved),
                                messageColor = Color.White,
                                backgroundColor = bannerBackgroundColor,
                                onDismiss = {
                                    viewModel.setEditProfileScreenState(
                                        EditProfileScreenState.Idle,
                                    )
                                },
                                autoDismissDelayMillis = 3000L,
                            )
                        }
                    }

                    is EditProfileScreenState.Error -> {
                        val error = uiState as EditProfileScreenState.Error
                        val profileUpdateErrorMessage = when (error.errorType) {
                            ErrorType.LOADING_ERROR -> stringResource(R.string.error_fetching_profile)
                            ErrorType.SAVING_ERROR -> stringResource(R.string.error_updating_profile)
                            ErrorType.DEFAULT -> stringResource(R.string.sorry_something_went_wrong)
                        }

                        ErrorView(
                            errorMessage = profileUpdateErrorMessage,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = bannerBackgroundColor),
                            onTryAgainClicked = { error.retryAction() },
                        )
                    }

                    is EditProfileScreenState.Loading -> {
                        val message = if ((uiState as EditProfileScreenState.Loading).loadType == LoadType.PROFILE_UPDATE) {
                            stringResource(R.string.updating_account)
                        } else {
                            stringResource(R.string.loading_your_profile)
                        }
                        StatusTopBanner(
                            showIcon = false,
                            message = message,
                            messageColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            backgroundColor = bannerBackgroundColor,
                            autoDismissDelayMillis = null,
                        )
                    }

                    else -> {}
                }
            }

            LazyColumn(
                state = rememberLazyListState(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    FormField(
                        label = stringResource(R.string.first_name),
                    ) {
                        OutlinedTextField(
                            value = viewModel.firstName,
                            onValueChange = { viewModel.onFirstNameChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = viewModel.firstNameHasErrors &&
                                uiState !is EditProfileScreenState.Loading,
                            supportingText = {
                                if (viewModel.firstNameHasErrors &&
                                    uiState !is EditProfileScreenState.Loading
                                ) {
                                    Text(stringResource(R.string.first_name_cannot_be_empty))
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                        )
                    }
                }

                item {
                    FormField(
                        label = stringResource(R.string.last_name),
                    ) {
                        OutlinedTextField(
                            value = viewModel.lastName,
                            onValueChange = { viewModel.onLastNameChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                        )
                    }
                }

                item {
                    FormField(
                        label = stringResource(R.string.email),
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = viewModel.email,
                            onValueChange = { newEmail -> viewModel.onEmailChanged(newEmail) },
                            isError = viewModel.emailHasErrors && uiState !is EditProfileScreenState.Loading,
                            supportingText = {
                                if (viewModel.emailHasErrors && uiState !is EditProfileScreenState.Loading) {
                                    if (viewModel.email.isBlank()) {
                                        Text(stringResource(R.string.email_cannot_be_empty))
                                    } else {
                                        Text(stringResource(R.string.email_invalid_format))
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                            ),
                        )
                    }
                }

                item {
                    FormField(
                        label = stringResource(R.string.username),
                    ) {
                        OutlinedTextField(
                            value = viewModel.username,
                            onValueChange = { viewModel.onUsernameChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            isError = viewModel.usernameHasErrors && uiState !is EditProfileScreenState.Loading,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                            supportingText = {
                                if (viewModel.usernameHasErrors &&
                                    uiState !is EditProfileScreenState.Loading
                                ) {
                                    if (viewModel.username.isBlank()) {
                                        Text(stringResource(R.string.username_cannot_be_empty))
                                    } else {
                                        Text(stringResource(R.string.username_invalid_format))
                                    }
                                } else {
                                    Text(stringResource(R.string.edit_username_supporting_text))
                                }
                            },
                        )
                    }
                }

                item {
                    FormField(
                        label = stringResource(R.string.bio),
                    ) {
                        val maxBioChars = 250
                        OutlinedTextField(
                            value = viewModel.bio,
                            onValueChange = { newBioText ->
                                if (newBioText.length <= maxBioChars) {
                                    viewModel.onBioChanged(newBioText)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
                            minLines = 4,
                            maxLines = 4,
                            supportingText = {
                                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = stringResource(
                                            R.string.bio_character_count,
                                            viewModel.bio.length,
                                            maxBioChars,
                                        ),
                                        textAlign = TextAlign.End,
                                    )
                                }
                            },
                        )
                    }
                }

                item {
                    FormField(
                        label = stringResource(R.string.location),
                    ) {
                        OutlinedTextField(
                            value = viewModel.location,
                            onValueChange = { viewModel.onLocationChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                        )
                    }
                }

                item {
                    FormField(
                        label = stringResource(R.string.portfolio_url),
                    ) {
                        OutlinedTextField(
                            value = viewModel.portfolioUrl,
                            onValueChange = { viewModel.onPortfolioUrlChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Next,
                            ),
                        )
                    }
                }

                item {
                    FormField(
                        label = stringResource(R.string.instagram_username),
                    ) {
                        OutlinedTextField(
                            value = viewModel.instagramUsername,
                            onValueChange = { viewModel.onInstagramUsernameChanged(it) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable that displays a banner at the top of the screen, typically used for showing status
 * messages like loading, success, or errors. The banner can be dismissed automatically after a
 * specified delay or manually via a close icon.
 *
 * @param showIcon Whether to display a close (dismiss) icon on the banner. If true, a close
 * button will be shown on the right side.
 * @param message The status message to display in the center of the banner.
 * @param messageColor The color of the text message.
 * @param backgroundColor The background color of the banner.
 * @param onDismiss A lambda function to be invoked when the banner is dismissed, either
 * manually by clicking the close icon or automatically after the delay.
 * @param autoDismissDelayMillis The delay in milliseconds after which the banner should
 * automatically dismiss itself by calling [onDismiss]. If set to null, the banner will not
 * auto-dismiss.
 */
@Composable
fun StatusTopBanner(
    showIcon: Boolean,
    message: String,
    messageColor: Color,
    backgroundColor: Color,
    onDismiss: () -> Unit = {},
    autoDismissDelayMillis: Long? = null,
) {
    if (autoDismissDelayMillis != null) {
        LaunchedEffect(message, autoDismissDelayMillis) {
            delay(autoDismissDelayMillis)
            onDismiss()
        }
    }

    val messageStartPadding = if (showIcon) 32.dp else 0.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = messageColor,
            modifier = Modifier
                .weight(1f)
                .padding(start = messageStartPadding),
            textAlign = TextAlign.Center,
        )
        if (showIcon) {
            IconButton(onClick = { onDismiss() }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = messageColor,
                )
            }
        }
    }
}

/**
 * A composable that displays an error message and a "Try Again" button.
 * This is typically used in a banner or a dedicated error state view.
 *
 * @param errorMessage The specific error message to be displayed to the user.
 * @param modifier The modifier to be applied to the `Column` container of this view.
 * @param onTryAgainClicked A lambda function that will be invoked when the user clicks the "Try Again" text.
 */
@Composable
fun ErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onTryAgainClicked: () -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.try_again),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                onTryAgainClicked()
            },
        )
    }
}
