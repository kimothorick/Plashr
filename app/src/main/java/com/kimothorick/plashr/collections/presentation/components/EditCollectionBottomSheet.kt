package com.kimothorick.plashr.collections.presentation.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimothorick.data.PreviewCollection
import com.kimothorick.plashr.R
import com.kimothorick.plashr.collectionDetails.presentation.CollectionUiState
import com.kimothorick.plashr.common.Constants.LayoutValues.MAX_DESCRIPTION_LENGTH
import com.kimothorick.plashr.common.Constants.LayoutValues.MAX_NAME_LENGTH
import com.kimothorick.plashr.profile.presentation.components.ProfileComponents
import com.kimothorick.plashr.ui.theme.PlashrTheme

/**
 * Defines the type of action being performed on a collection within the `EditCollectionBottomSheet`.
 * This is used by [EditCollectionState] to determine the appropriate UI feedback (e.g., loading
 * messages, success messages) for the specific operation.
 */
enum class ActionType {
    DELETE,
    UPDATE,
}

/**
 * Represents the various states of the collection editing/deleting process.
 * This sealed interface is used to manage the UI state of the `EditCollectionBottomSheet`,
 * reflecting the current status of an update or delete operation.
 */
sealed interface EditCollectionState {
    @get:StringRes
    val messageResId: Int?

    /**
     * Represents the initial or resting state where no operation is in progress.
     * This is the default state of the bottom sheet before any user action.
     */
    object Idle : EditCollectionState {
        override val messageResId: Int? = null
    }

    /**
     * Represents the state where an update or delete operation is currently in progress.
     * This state is used to show a loading indicator and disable user interaction.
     *
     * @param actionType The type of action being performed ([ActionType.UPDATE] or [ActionType.DELETE]),
     * which determines the specific loading message displayed.
     */
    data class InProgress(
        val actionType: ActionType,
    ) : EditCollectionState {
        override val messageResId: Int = when (actionType) {
            ActionType.UPDATE -> R.string.saving_collection
            ActionType.DELETE -> R.string.deleting_collection
        }
    }

    /**
     * Represents the state when an update or delete operation has completed successfully.
     * This state is used to display a confirmation message to the user.
     *
     * @param actionType The type of action that succeeded ([ActionType.UPDATE] or [ActionType.DELETE]),
     * which determines the specific success message displayed.
     */
    data class Success(
        val actionType: ActionType,
    ) : EditCollectionState {
        override val messageResId: Int = when (actionType) {
            ActionType.UPDATE -> R.string.collection_saved
            ActionType.DELETE -> R.string.collection_deleted
        }
    }

    /**
     * Represents the state when an update or delete operation has failed.
     * This state is used to display an error message and provide options to retry or go back.
     *
     * @param actionType The type of action that failed ([ActionType.UPDATE] or [ActionType.DELETE]),
     * which determines the specific failure message displayed.
     * @param errorMessage An optional detailed error message from the data source, which can be
     * displayed to provide more context about the failure.
     */
    data class Failed(
        val actionType: ActionType,
        val errorMessage: String? = null,
    ) : EditCollectionState {
        override val messageResId: Int = when (actionType) {
            ActionType.UPDATE -> R.string.failed_to_save_collection
            ActionType.DELETE -> R.string.failed_to_delete_collection
        }
    }

    /**
     * Represents the state where the user has initiated the delete process and is presented
     * with a confirmation dialog. This state prompts the user to confirm their decision to
     * delete the collection.
     */
    object Delete : EditCollectionState {
        override val messageResId: Int = R.string.deleting_collection
    }
}

/**
 * A composable that displays a modal bottom sheet for editing or deleting a collection.
 *
 * This bottom sheet provides a form to modify a collection's name, description, and privacy status.
 * It also handles the user flow for deleting the collection, including a confirmation step. The UI
 * state is driven by [editCollectionState], which shows loading, success, or error states for
 * update and delete operations.
 *
 * The bottom sheet's state (name, description, privacy) is managed internally using `rememberSaveable`
 * and is initialized from [initialCollectionState]. Interactions are disabled while an operation
 * is in progress to prevent inconsistent states.
 *
 * @param modifier The modifier to be applied to the bottom sheet.
 * @param initialCollectionState The initial state of the collection being edited, used to pre-fill the form fields.
 *   This is typically a [CollectionUiState.Success] containing the collection data.
 * @param editCollectionState The current state of the edit/delete operation (e.g., [EditCollectionState.Idle],
 *   [EditCollectionState.InProgress], [EditCollectionState.Success], [EditCollectionState.Failed]).
 *   This controls the UI to show forms, loading indicators, or result messages.
 * @param sheetState The state of the modal bottom sheet, used to control its visibility (e.g., showing or hiding).
 * @param onDismiss A callback invoked when the user requests to dismiss the bottom sheet (e.g., by swiping down or
 *   pressing the back button).
 * @param onSave A callback invoked when the user clicks the "Save" button. It provides the updated name,
 *   description, and privacy status.
 * @param onDelete A callback invoked when the user initiates the deletion process, typically to transition to the
 *   confirmation state ([EditCollectionState.Delete]).
 * @param onConfirmDelete A callback invoked when the user confirms the deletion of the collection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCollectionBottomSheet(
    modifier: Modifier = Modifier,
    initialCollectionState: CollectionUiState,
    editCollectionState: EditCollectionState,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String?, isPrivate: Boolean) -> Unit,
    onDelete: () -> Unit,
    onConfirmDelete: () -> Unit = {},
    reset: () -> Unit = {},
    errorMessage: String? = null,
) {
    val initialCollection = (initialCollectionState as? CollectionUiState.Success)?.collection

    var name by rememberSaveable(initialCollection?.title) { mutableStateOf(initialCollection?.title ?: "") }
    var description by rememberSaveable(initialCollection?.description) { mutableStateOf(initialCollection?.description ?: "") }
    var isPrivate by rememberSaveable(initialCollection?.private) { mutableStateOf(initialCollection?.private ?: false) }
    val isSaving = editCollectionState is EditCollectionState.InProgress

    ModalBottomSheet(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        },
        sheetState = sheetState,
        modifier = modifier
            .navigationBarsPadding()
            .imePadding(),
        shape = MaterialTheme.shapes.extraLarge,
        sheetMaxWidth = 420.dp,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = !isSaving,
            shouldDismissOnClickOutside = !isSaving,
        ),
        dragHandle = null,
        containerColor = Color.Transparent,
        contentColor = contentColorFor(
            MaterialTheme.colorScheme.surface,
        ),
        sheetGesturesEnabled = !isSaving,
    ) {
        EditCollectionBottomSheetContent(
            editCollectionState = editCollectionState,
            collectionName = name,
            collectionDescription = description,
            isPrivate = isPrivate,
            isInteractionDisabled = isSaving,
            onNameChange = { newName ->
                if (newName.length <= MAX_NAME_LENGTH) {
                    name = newName
                }
            },
            onDescriptionChange = { newDescription ->
                if (newDescription.length <= MAX_DESCRIPTION_LENGTH) {
                    description = newDescription
                }
            },
            onIsPrivateChange = { isPrivate = it },
            onDismiss = onDismiss,
            onSave = {
                onSave(
                    name.trim(),
                    description.trim().ifEmpty { null },
                    isPrivate,
                )
            },
            onDelete = onDelete,
            onConfirmDelete = onConfirmDelete,
            reset = reset,
            errorMessage = errorMessage,
        )
    }
}

/**
 * The core UI content for the [EditCollectionBottomSheet].
 *
 * This composable is responsible for rendering the form fields for editing a collection's details
 * (name, description, privacy) and the action buttons. It also displays the status of an ongoing
 * operation (loading, success, or failure) based on the [editCollectionState].
 *
 * This is a stateless composable, meaning it receives all its data as parameters and communicates
 * user interactions through callbacks.
 *
 * @param editCollectionState The current state of the edit/delete operation, which determines
 *   whether to show the edit form or a status message (e.g., in-progress, success, failed).
 * @param collectionName The current value of the collection name input field.
 * @param collectionDescription The current value of the collection description input field.
 * @param isPrivate The current value of the privacy switch.
 * @param isInteractionDisabled A boolean to disable UI elements (like text fields and buttons)
 *   while an operation is in progress.
 * @param onNameChange A callback invoked when the user types in the name field.
 * @param onDescriptionChange A callback invoked when the user types in the description field.
 * @param onIsPrivateChange A callback invoked when the user toggles the privacy switch.
 * @param onDismiss A callback invoked when the sheet should be dismissed (e.g., after a successful update).
 * @param onSave A callback invoked when the user taps the "Save" button.
 * @param onDelete A callback invoked when the user taps the "Delete" button, to initiate the delete confirmation flow.
 * @param onConfirmDelete A callback invoked when the user confirms the deletion.
 * @param reset A callback to reset the UI state, typically used to go back to the edit form from an error state.
 * @param errorMessage An optional string containing a detailed error message to be displayed in the failed state.
 */
@Composable
fun EditCollectionBottomSheetContent(
    editCollectionState: EditCollectionState,
    collectionName: String,
    collectionDescription: String,
    isPrivate: Boolean,
    isInteractionDisabled: Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIsPrivateChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
    reset: () -> Unit = {},
    errorMessage: String?,
) {
    val stateMessage = editCollectionState.messageResId?.let { stringResource(it) }

    val showOperationStatus =
        editCollectionState is EditCollectionState.InProgress ||
            editCollectionState is EditCollectionState.Success ||
            editCollectionState is EditCollectionState.Failed

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp),
    ) {
        if (showOperationStatus) {
            val actionType = when (editCollectionState) {
                is EditCollectionState.InProgress -> editCollectionState.actionType
                is EditCollectionState.Success -> editCollectionState.actionType
                is EditCollectionState.Failed -> editCollectionState.actionType
                else -> ActionType.UPDATE
            }

            val errorMessage = errorMessage
            ProfileComponents().OperationStatusContent(
                isLoading = editCollectionState is EditCollectionState.InProgress,
                hasSucceeded = editCollectionState is EditCollectionState.Success,
                hasFailed = editCollectionState is EditCollectionState.Failed,
                title = stateMessage ?: "",
                message = errorMessage,
                successButtonText = if (actionType == ActionType.UPDATE) {
                    stringResource(R.string.done)
                } else {
                    stringResource(
                        R.string.close,
                    )
                },
                errorButtonText = stringResource(R.string.try_again),
                onSuccess = {
                    if (actionType == ActionType.UPDATE) {
                        onDismiss()
                    }
                },
                onError = {
                    if (actionType == ActionType.UPDATE) {
                        onSave()
                    } else {
                        onConfirmDelete()
                    }
                },
            )
            if ((actionType == ActionType.UPDATE || actionType == ActionType.DELETE) && editCollectionState is EditCollectionState.Failed) {
                OutlinedButton(
                    onClick = reset,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(stringResource(R.string.back_to_edit), style = MaterialTheme.typography.labelLarge)
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.edit_collection),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )

                Box(modifier = Modifier.weight(1f, fill = false)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        // Name Field
                        Column(modifier = Modifier.fillMaxWidth()) {
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
                                enabled = !isInteractionDisabled,
                                supportingText = {
                                    Text(
                                        text = "${collectionName.length} / $MAX_NAME_LENGTH",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.End,
                                    )
                                },
                                shape = MaterialTheme.shapes.small,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                ),
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Description Field
                        Column(modifier = Modifier.fillMaxWidth()) {
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
                                enabled = !isInteractionDisabled,
                                supportingText = {
                                    Text(
                                        text = "${collectionDescription.length} / $MAX_DESCRIPTION_LENGTH",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.End,
                                    )
                                },
                                shape = MaterialTheme.shapes.small,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                ),
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Switch(
                                checked = isPrivate,
                                onCheckedChange = onIsPrivateChange,
                                enabled = !isInteractionDisabled,
                                colors = SwitchDefaults.colors(
                                    uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                                ),
                            )
                            Text(text = stringResource(R.string.make_collection_private))
                        }
                    }
                }
                // Action Buttons
                ActionButtons(
                    editCollectionState = editCollectionState,
                    isSaveButtonEnabled = collectionName.isNotBlank(),
                    onSave = onSave,
                    onDelete = onDelete,
                    onDeletionConfirmed = onConfirmDelete,
                    onCancelDeletion = reset,
                    onDismissEdit = onDismiss,
                )
            }
        }
    }
}

/**
 * A composable that displays the action buttons for the edit collection bottom sheet.
 *
 * This component's appearance and behavior adapt based on the [editCollectionState].
 * - In the standard edit state, it shows "Delete", "Cancel", and "Save" buttons.
 * - When deletion is initiated ([EditCollectionState.Delete]), it shows a confirmation
 *   prompt "Are you sure?" along with "Cancel" and "Delete" buttons.
 *
 * All buttons are disabled if an operation is currently in progress.
 *
 * @param editCollectionState The current state of the edit/delete operation, which determines
 *   which set of buttons and prompts to display.
 * @param isSaveButtonEnabled A boolean indicating whether the "Save" button should be enabled,
 *   typically based on form validation (e.g., if the collection name is not blank).
 * @param onSave Callback invoked when the "Save" button is clicked.
 * @param onDelete Callback invoked to initiate the deletion confirmation step.
 * @param onDeletionConfirmed Callback invoked when the user confirms the deletion.
 * @param onCancelDeletion Callback invoked when the user cancels the deletion confirmation.
 * @param onDismissEdit Callback invoked when the user cancels the edit operation.
 * @param modifier The modifier to be applied to the `Row` container.
 */
@Composable
private fun ActionButtons(
    editCollectionState: EditCollectionState,
    isSaveButtonEnabled: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDeletionConfirmed: () -> Unit,
    onCancelDeletion: () -> Unit,
    onDismissEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOperationInProgress = editCollectionState is EditCollectionState.InProgress

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (editCollectionState == EditCollectionState.Delete) {
            Text(text = "Are you sure?")
        } else {
            TextButton(
                onClick = {
                    onDelete()
                },
                enabled = !isOperationInProgress,
            ) {
                Text(
                    text = stringResource(id = R.string.delete_collection),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(
                onClick = {
                    if (editCollectionState == EditCollectionState.Delete) {
                        onCancelDeletion()
                    } else {
                        onDismissEdit()
                    }
                },
                enabled = !isOperationInProgress,
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Button(
                onClick = {
                    if (editCollectionState == EditCollectionState.Delete) {
                        onDeletionConfirmed()
                    } else {
                        onSave()
                    }
                },
                enabled = if (editCollectionState == EditCollectionState.Delete) {
                    !isOperationInProgress
                } else {
                    isSaveButtonEnabled && !isOperationInProgress
                },
                colors = if (editCollectionState == EditCollectionState.Delete) {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                } else {
                    ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                },
            ) {
                Text(
                    text = stringResource(
                        id = if (editCollectionState == EditCollectionState.Delete) {
                            R.string.delete
                        } else {
                            R.string.save
                        },
                    ),
                )
            }
        }
    }
}

//region Previews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditCollectionBottomSheetPreview(
    editCollectionState: EditCollectionState,
) {
    var editCollectionState = editCollectionState
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
        skipHiddenState = true,
    )

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    PlashrTheme {
        EditCollectionBottomSheet(
            initialCollectionState = CollectionUiState.Success(PreviewCollection, ""),
            editCollectionState = editCollectionState,
            onDismiss = {},
            onSave = { name, description, isPrivate ->
                editCollectionState = EditCollectionState.InProgress(ActionType.UPDATE)
            },
            onDelete = {},
            reset = {},
            errorMessage = "Network connection timed out!",
            sheetState = sheetState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewIDLE() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.Idle,
    )
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewINPROGRESS() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.InProgress(ActionType.UPDATE),
    )
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewSUCCESS() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.Success(ActionType.UPDATE),
    )
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewUPDATEFAILED() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.Failed(ActionType.UPDATE),
    )
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewDELETE() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.Delete,
    )
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewDELETEINPROGRESS() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.InProgress(ActionType.DELETE),
    )
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewDELETEFAILED() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.Failed(ActionType.DELETE),
    )
}

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditCollectionBottomSheetPreviewDELETESUCCESS() {
    EditCollectionBottomSheetPreview(
        editCollectionState = EditCollectionState.Success(ActionType.DELETE),
    )
}

//endregion
