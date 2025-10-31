package com.kimothorick.plashr.collections.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kimothorick.plashr.R
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import com.kimothorick.plashr.ui.theme.PlashrTheme
import com.kimothorick.plashr.utils.tooling.ComponentPreviews

private const val MAX_NAME_LENGTH = 60
private const val MAX_DESCRIPTION_LENGTH = 250

/**
 * Represents the different states of the collection creation process.
 * This sealed interface is used to update the UI based on the current
 * status of the create operation, such as showing loading indicators,
 * success messages, or error details.
 */
sealed interface CreateCollectionState {
    @get:StringRes
    val messageResId: Int?

    /**
     * The initial state before any creation attempt has been made. The UI is ready for user input.
     */
    object Idle : CreateCollectionState {
        override val messageResId: Int? = null
    }

    /**
     * Represents the state where the collection creation is actively in progress.
     * The UI should show a loading indicator and disable user interactions with the form.
     */
    object InProgress : CreateCollectionState {
        override val messageResId: Int = R.string.creating_collection
    }

    /**
     * Represents a successful collection creation. The UI should display a success
     * message and provide an option to dismiss the bottom sheet.
     */
    object Success : CreateCollectionState {
        override val messageResId: Int = R.string.collection_created
    }

    /**
     * Represents a state where the collection creation has failed. The UI should display
     * a generic failure message along with a more specific error message if available.
     * It provides options for the user to either retry the operation or go back to edit the form.
     *
     * @param errorMessage An optional, detailed error message from the server or a network exception
     *                     to provide more context about the failure.
     */
    data class Failed(
        val errorMessage: String? = null,
    ) : CreateCollectionState {
        override val messageResId: Int = R.string.failed_to_create_collection
    }
}

/**
 * A composable that displays a modal bottom sheet for creating a new collection.
 *
 * This bottom sheet contains a form for the user to input a collection's name,
 * description, and privacy setting. It manages its internal state for these fields
 * and reflects the overall creation process status (idle, in-progress, success, or failure)
 * based on the [createState] parameter.
 *
 * The bottom sheet's interactions, such as dismissing by dragging or pressing the back
 * button, are disabled when the creation process is in progress.
 *
 * @param modifier The [Modifier] to be applied to the bottom sheet.
 * @param createState The current state of the collection creation process, which determines
 *                    the UI content (form, loading indicator, success/error message).
 * @param sheetState The state of the modal bottom sheet, used to control its visibility.
 * @param onDismiss Callback invoked when the user requests to dismiss the bottom sheet
 *                  (e.g., by tapping outside or pressing the back button).
 * @param onCreateCollection Callback invoked when the user clicks the 'Create' button. It provides
 *                           the entered name, description (nullable if empty), and privacy status.
 * @param onReset Callback to reset the creation state from a failed or success state back to
 *                the initial form, allowing the user to edit their input.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCollectionBottomSheet(
    modifier: Modifier = Modifier,
    createState: CreateCollectionState,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onCreateCollection: (name: String, description: String?, isPrivate: Boolean) -> Unit,
    onReset: () -> Unit,
) {
    var collectionName by rememberSaveable { mutableStateOf("") }
    var collectionDescription by rememberSaveable { mutableStateOf("") }
    var collectionIsPrivate by rememberSaveable { mutableStateOf(false) }
    val isInteractionDisabled = createState is CreateCollectionState.InProgress

    ModalBottomSheet(
        onDismissRequest = {
            if (!isInteractionDisabled) onDismiss()
        },
        sheetState = sheetState,
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        shape = MaterialTheme.shapes.extraLarge,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = !isInteractionDisabled,
        ),
        dragHandle = null,
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        sheetGesturesEnabled = !isInteractionDisabled,
    ) {
        CreateCollectionBottomSheetContent(
            creationState = createState,
            collectionName = collectionName,
            collectionDescription = collectionDescription,
            isPrivate = collectionIsPrivate,
            onNameChange = { newName ->
                if (newName.length <= MAX_NAME_LENGTH) collectionName = newName
            },
            onDescriptionChange = { newDescription ->
                if (newDescription.length <= MAX_DESCRIPTION_LENGTH) collectionDescription = newDescription
            },
            onIsPrivateChange = { collectionIsPrivate = it },
            onDismiss = onDismiss,
            onCreate = {
                onCreateCollection(
                    collectionName.trim(),
                    collectionDescription.trim().ifEmpty { null },
                    collectionIsPrivate,
                )
            },
            onBackToEdit = onReset,
        )
    }
}

/**
 * The internal content of the [CreateCollectionBottomSheet].
 *
 * This composable is responsible for rendering the UI based on the [creationState].
 * If the state is `Idle`, it displays a form for the user to enter collection details.
 * If the state is `InProgress`, `Success`, or `Failed`, it displays a status screen
 * reflecting the current state of the operation. This is a stateless composable
 * that hoists its state to the parent.
 *
 * @param creationState The current state of the collection creation process.
 * @param collectionName The current value for the collection's name.
 * @param collectionDescription The current value for the collection's description.
 * @param isPrivate The current value for the collection's privacy setting.
 * @param onNameChange Callback invoked when the user changes the collection name.
 * @param onDescriptionChange Callback invoked when the user changes the collection description.
 * @param onIsPrivateChange Callback invoked when the user toggles the privacy switch.
 * @param onDismiss Callback to dismiss the bottom sheet, typically used on success or cancellation.
 * @param onCreate Callback to trigger the creation of the collection.
 * @param onBackToEdit Callback to return to the editing form from a failed state.
 */
@Composable
private fun CreateCollectionBottomSheetContent(
    creationState: CreateCollectionState,
    collectionName: String,
    collectionDescription: String,
    isPrivate: Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIsPrivateChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onCreate: () -> Unit,
    onBackToEdit: () -> Unit,
) {
    val statusMessage = creationState.messageResId?.let { stringResource(it) }
    val isOperationInProgress = creationState !is CreateCollectionState.Idle
    val errorMessage = (creationState as? CreateCollectionState.Failed)?.errorMessage

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (isOperationInProgress) {
            ProfileComponents().OperationStatusContent(
                isLoading = creationState is CreateCollectionState.InProgress,
                hasSucceeded = creationState is CreateCollectionState.Success,
                hasFailed = creationState is CreateCollectionState.Failed,
                title = statusMessage ?: "",
                message = errorMessage,
                successButtonText = stringResource(R.string.done),
                errorButtonText = stringResource(R.string.try_again),
                onSuccess = onDismiss,
                onError = onCreate,
            )
            if (creationState is CreateCollectionState.Failed) {
                OutlinedButton(
                    onClick = onBackToEdit,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(stringResource(R.string.back_to_edit), style = MaterialTheme.typography.labelLarge)
                }
            }
        } else {
            // Title
            Text(
                text = stringResource(R.string.create_new_collection),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            // Form Fields
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Name Field
                Column {
                    Text(
                        text = stringResource(R.string.name),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    OutlinedTextField(
                        value = collectionName,
                        onValueChange = onNameChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("collection_name_input"),
                        singleLine = true,
                        supportingText = {
                            Text(
                                text = "${collectionName.length} / $MAX_NAME_LENGTH",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        shape = MaterialTheme.shapes.small,
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.outline),
                    )
                }

                // Description Field
                Column {
                    Text(
                        text = stringResource(R.string.description_optional),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    OutlinedTextField(
                        value = collectionDescription,
                        onValueChange = onDescriptionChange,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 3,
                        supportingText = {
                            Text(
                                text = "${collectionDescription.length} / $MAX_DESCRIPTION_LENGTH",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        shape = MaterialTheme.shapes.small,
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.outline),
                    )
                }

                // Private Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Switch(
                        checked = isPrivate,
                        onCheckedChange = onIsPrivateChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        ),
                    )
                    Text(stringResource(R.string.make_collection_private), style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                ) { Text(stringResource(R.string.cancel)) }
                Button(
                    onClick = onCreate,
                    modifier = Modifier.weight(1f),
                    enabled = collectionName.isNotBlank(),
                    contentPadding = PaddingValues(16.dp),
                ) { Text(stringResource(R.string.create)) }
            }
        }
    }
}

//region Previews
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCollectionBottomSheetPreview(
    state: CreateCollectionState,
) {
    PlashrTheme {
        Surface {
            CreateCollectionBottomSheetContent(
                creationState = state,
                collectionName = "My Awesome Trip",
                collectionDescription = "Photos from the mountains.",
                isPrivate = false,
                onNameChange = {},
                onDescriptionChange = {},
                onIsPrivateChange = {},
                onDismiss = {},
                onCreate = {},
                onBackToEdit = {},
            )
        }
    }
}

@ComponentPreviews()
@Composable
private fun CreateCollectionBottomSheetPreview_Idle() {
    CreateCollectionBottomSheetPreview(CreateCollectionState.Idle)
}

@ComponentPreviews()
@Composable
private fun CreateCollectionBottomSheetPreview_InProgress() {
    CreateCollectionBottomSheetPreview(CreateCollectionState.InProgress)
}

@ComponentPreviews()
@Composable
private fun CreateCollectionBottomSheetPreview_Success() {
    CreateCollectionBottomSheetPreview(CreateCollectionState.Success)
}

@ComponentPreviews()
@Composable
private fun CreateCollectionBottomSheetPreview_Failed() {
    CreateCollectionBottomSheetPreview(CreateCollectionState.Failed("A network error occurred."))
}

// endregion
