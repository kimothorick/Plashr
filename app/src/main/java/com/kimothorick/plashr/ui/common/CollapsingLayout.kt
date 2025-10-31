package com.kimothorick.plashr.ui.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * A layout that provides a collapsing header effect. It consists of a `header` and a `body`.
 * The `header` collapses as the `body` is scrolled upwards, and reappears when the `body` is scrolled downwards.
 * This is achieved by using the nested scroll mechanism.
 *
 * @param header The content to be displayed in the collapsing header section.
 * @param body The main content of the screen, which is scrollable. The header collapses based on the scroll events within this body.
 * @param modifier The modifier to be applied to the root container of this layout.
 * @param onProgress A callback that provides the current collapse progress as a Float between 0.0 and 1.0.
 *                   0.0 means the header is fully expanded, and 1.0 means it is fully collapsed.
 */
@Composable
internal fun CollapsingLayout(
    header: @Composable BoxScope.() -> Unit,
    body: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    onProgress: (Float) -> Unit = {},
) {
    var collapsingTopHeight by remember { mutableFloatStateOf(0f) }
    var headerOffset by rememberSaveable { mutableFloatStateOf(0f) }

    LaunchedEffect(headerOffset, collapsingTopHeight) {
        if (collapsingTopHeight > 0) {
            onProgress((-headerOffset / collapsingTopHeight).coerceIn(0f, 1f))
        }
    }

    fun calculateOffset(
        delta: Float,
    ): Offset {
        val oldOffset = headerOffset
        val newOffset = (oldOffset + delta).coerceIn(-collapsingTopHeight, 0f)
        headerOffset = newOffset
        return Offset(0f, newOffset - oldOffset)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset =
                when {
                    available.y > 0 && headerOffset == 0f -> Offset.Zero
                    available.y < 0 && headerOffset > -collapsingTopHeight -> calculateOffset(available.y)
                    else -> Offset.Zero
                }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset =
                when {
                    available.y > 0 && headerOffset < 0f -> calculateOffset(available.y)
                    else -> Offset.Zero
                }
        }
    }

    val headerScrollableState = rememberScrollableState { delta ->
        0f
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        Box(
            modifier = Modifier
                .onSizeChanged { size ->
                    collapsingTopHeight = size.height.toFloat()
                }
                .offset { IntOffset(x = 0, y = headerOffset.roundToInt()) }
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = headerScrollableState,
                    enabled = true,
                ),
            content = header,
        )
        Box(
            modifier = Modifier.offset {
                IntOffset(
                    x = 0,
                    y = (collapsingTopHeight + headerOffset).roundToInt(),
                )
            },
            content = body,
        )
    }
}
