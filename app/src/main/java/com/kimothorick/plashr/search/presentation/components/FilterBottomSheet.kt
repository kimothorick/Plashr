package com.kimothorick.plashr.search.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimothorick.plashr.R
import com.kimothorick.plashr.search.presentation.components.ContentFilter.High
import com.kimothorick.plashr.search.presentation.components.ContentFilter.Low
import com.kimothorick.plashr.search.presentation.components.OrderBy.Latest
import com.kimothorick.plashr.search.presentation.components.OrderBy.Relevant
import com.kimothorick.plashr.search.presentation.components.Orientation.Landscape
import com.kimothorick.plashr.search.presentation.components.Orientation.Portrait
import com.kimothorick.plashr.search.presentation.components.Orientation.Squarish
import com.kimothorick.plashr.ui.theme.PlashrTheme

/**
 * A composable that displays a modal bottom sheet for filtering photos.
 *
 * This sheet provides options to filter by order, content safety, color, and orientation.
 * It maintains its own state for the selected filters, which are initially set by `initialFilters`.
 * When the user is done, they can apply the filters, which triggers the `onApplyFilters` callback.
 * The sheet can be dismissed via swipe or back press, which calls the `onDismiss` lambda.
 *
 * @param onDismiss A lambda to be invoked when the bottom sheet is dismissed.
 * @param initialFilters The initial filter settings to be displayed when the sheet opens.
 * @param onApplyFilters A callback that provides the selected `FilterOptions` when the user clicks the "Apply" button.
 * @param sheetState The state of the modal bottom sheet, controlling its visibility and behavior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    initialFilters: FilterOptions,
    onApplyFilters: (FilterOptions) -> Unit,
    sheetState: SheetState,
) {
    var currentFilters by remember { mutableStateOf(initialFilters) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.navigationBarsPadding(),
        shape = MaterialTheme.shapes.extraLarge,
        sheetMaxWidth = 420.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(
            MaterialTheme.colorScheme.surface,
        ),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier.padding(vertical = 0.dp),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.filter_photos),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                TextButton(
                    onClick = {
                        currentFilters = FilterOptions()
                    },
                ) {
                    Text(stringResource(R.string.reset))
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 8.dp),
            ) {
                // Order by
                item {
                    FilterSection(title = stringResource(R.string.order_by)) {
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            OrderBy.entries.forEachIndexed { index, orderBy ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = OrderBy.entries.size),
                                    onClick = { currentFilters = currentFilters.copy(orderBy = orderBy) },
                                    selected = currentFilters.orderBy == orderBy,
                                ) {
                                    Text(orderBy.name)
                                }
                            }
                        }
                    }
                }

                // Content filter
                item {
                    FilterSection(title = stringResource(R.string.content_filter)) {
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            ContentFilter.entries.forEachIndexed { index, contentFilter ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = ContentFilter.entries.size),
                                    onClick = { currentFilters = currentFilters.copy(contentFilter = contentFilter) },
                                    selected = currentFilters.contentFilter == contentFilter,
                                ) {
                                    Text(contentFilter.name)
                                }
                            }
                        }
                    }
                }

                // Color
                item {
                    FilterSection(title = stringResource(R.string.color)) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 8.dp),
                        ) {
                            items(ColorFilter.entries) { colorFilter ->
                                ColorChip(
                                    colorFilter = colorFilter,
                                    isSelected = currentFilters.colorFilter == colorFilter,
                                    onClick = {
                                        currentFilters = currentFilters.copy(
                                            colorFilter = if (currentFilters.colorFilter == colorFilter) null else colorFilter,
                                        )
                                    },
                                )
                            }
                        }
                    }
                }

                // Orientation
                item {
                    FilterSection(title = stringResource(R.string.orientation)) {
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            Orientation.entries.forEachIndexed { index, orientation ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = Orientation.entries.size),
                                    onClick = { currentFilters = currentFilters.copy(orientation = orientation) },
                                    selected = currentFilters.orientation == orientation,
                                ) {
                                    Text(orientation.name)
                                }
                            }
                        }
                    }
                }
            }

            // Apply Button
            Button(
                onClick = { onApplyFilters(currentFilters) },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Text(stringResource(R.string.apply))
            }
        }
    }
}

/**
 * A composable that provides a consistent layout for a section within the filter sheet.
 * It displays a title and provides a slot for the filter controls.
 *
 * @param title The text to be displayed as the title of the section.
 * @param content The composable content that represents the filter controls for this section,
 *                such as a `SingleChoiceSegmentedButtonRow` or a `LazyRow` of chips.
 *                This content is placed within a `ColumnScope`.
 */
@Composable
fun FilterSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
        )
        content()
    }
}

/**
 * A composable that displays a filter chip for a specific color.
 *
 * This chip shows a colored circle as its leading icon and the color's name as its label.
 * When selected, it displays a checkmark as a trailing icon and uses distinct colors
 * to indicate its selected state.
 *
 * @param colorFilter The [ColorFilter] enum entry this chip represents, containing the color and label.
 * @param isSelected Whether the chip is currently selected.
 * @param onClick A lambda to be invoked when the chip is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorChip(
    colorFilter: ColorFilter,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        shape = CircleShape,
        label = { Text(stringResource(colorFilter.label)) },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(colorFilter.color, CircleShape)
                    .border(
                        width = 1.dp,
                        color = if (colorFilter.color == Color.White) Color.LightGray else Color.Transparent,
                        shape = CircleShape,
                    ),
            )
        },
        trailingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.onSurface,
            selectedLabelColor = MaterialTheme.colorScheme.surface,
            selectedLeadingIconColor = MaterialTheme.colorScheme.surface,
            selectedTrailingIconColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

/**
 * Represents the set of available filter options for a photo search.
 *
 * This data class is used to hold the state of the filters selected by the user
 * in the [FilterBottomSheet]. It includes default values for each filter, which
 * represent the initial or reset state.
 *
 * @property orderBy The sorting order for the search results. Defaults to [OrderBy.Relevant].
 * @property contentFilter The safety level for the content. Defaults to [ContentFilter.Low].
 * @property colorFilter The dominant color to filter by. Defaults to `null` (no color filter).
 * @property orientation The orientation of the photos. Defaults to `null` (any orientation).
 */
data class FilterOptions(
    val orderBy: OrderBy = Relevant,
    val contentFilter: ContentFilter = Low,
    val colorFilter: ColorFilter? = null,
    val orientation: Orientation? = null,
)

/**
 * Defines the sorting order for search results.
 * This is used in the [FilterBottomSheet] to allow the user to choose
 * how the photos are ordered.
 *
 * @property Latest Sorts the results from newest to oldest.
 * @property Relevant Sorts the results by the most relevant matches first.
 */
enum class OrderBy { Latest, Relevant }

/**
 * Defines the content safety level for search results.
 * This is used in the [FilterBottomSheet] to filter out potentially sensitive content.
 *
 * @property Low Filters results for all content, including "high-risk" content.
 * @property High Filters results to exclude content that might be unsuitable for some audiences.
 */
enum class ContentFilter { Low, High }

/**
 * Defines the orientation of the photos to be searched for.
 * This is used in the [FilterBottomSheet] to allow the user to filter
 * results based on the aspect ratio of the images.
 *
 * @property Landscape Filters for images that are wider than they are tall.
 * @property Portrait Filters for images that are taller than they are wide.
 * @property Squarish Filters for images that have a roughly square aspect ratio.
 */
enum class Orientation { Landscape, Portrait, Squarish }

/**
 * Defines the available color filters for a photo search.
 *
 * Each filter is associated with a specific color and a string resource for its name,
 * used in the [FilterBottomSheet] to allow users to filter images by a dominant color.
 * The `color` property is used for displaying a visual representation in the UI,
 * while the `label` provides a user-facing, translatable name.
 *
 * @param label The string resource ID for the user-facing name of the color.
 * @param color The [Color] value used for display purposes in the UI, e.g., in a [ColorChip].
 */
enum class ColorFilter(
    @StringRes val label: Int,
    val color: Color,
) {
    BlackAndWhite(R.string.black_and_white, Color.White),
    Black(R.string.black, Color.Black),
    White(
        R.string.white,
        Color.White,
    ),
    Yellow(R.string.yellow, Color.Yellow),
    Orange(R.string.orange, Color(0xFFFFA500)),
    Red(R.string.red, Color.Red),
    Purple(
        R.string.purple,
        Color(0xFF800080),
    ),
    Magenta(R.string.magenta, Color.Magenta),
    Green(R.string.green, Color.Green),
    Teal(
        R.string.teal,
        Color(0xFF008080),
    ),
    Blue(R.string.blue, Color.Blue),
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FilterBottomSheetPreview() {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
        skipHiddenState = true,
    )

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    PlashrTheme {
        FilterBottomSheet(
            onDismiss = {},
            initialFilters = FilterOptions(),
            onApplyFilters = {},
            sheetState = sheetState,
        )
    }
}
