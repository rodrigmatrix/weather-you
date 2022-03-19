package com.rodrigmatrix.weatheryou.presentation.extensions

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.lazy.LazyGridState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyUp
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.dpadFocusable(
    itemIndex: Int,
    scrollState: LazyListState
) = composed {
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.03f else 1f
    )
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent
    )
    this.onKeyEvent {
            if (it.type == KeyUp) {
                return@onKeyEvent false
            }
            when (it.key) {
                Key.DirectionUp -> {
                    coroutineScope.launch {
                        runCatching {
                            scrollState.animateScrollToItem(itemIndex - 1)
                        }
                    }
                }
                Key.DirectionDown -> {
                    coroutineScope.launch {
                        runCatching {
                            scrollState.animateScrollToItem(itemIndex + 1)
                        }
                    }
                }
            }
            return@onKeyEvent false
        }
        .focusable(interactionSource = interactionSource)
        .scale(scale)
        .border(
            width = 1.2.dp,
            borderColor,
            shape = RoundedCornerShape(24.dp)
        )
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun Modifier.dpadFocusable(
    itemIndex: Int,
    scrollState: LazyGridState
) = composed {
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.03f else 1f
    )
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent
    )
    this.onKeyEvent {
        if (it.type == KeyUp) {
            return@onKeyEvent false
        }
        when (it.key) {
            Key.DirectionUp -> {
                coroutineScope.launch {
                    runCatching {
                        scrollState.animateScrollToItem(itemIndex - 1)
                    }
                }
            }
            Key.DirectionDown -> {
                coroutineScope.launch {
                    runCatching {
                        scrollState.animateScrollToItem(itemIndex + 1)
                    }
                }
            }
        }
        return@onKeyEvent false
    }
        .focusable(interactionSource = interactionSource)
        .scale(scale)
        .border(
            width = 1.2.dp,
            borderColor,
            shape = RoundedCornerShape(24.dp)
        )
}


fun Modifier.dpadFocusable() = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.03f else 1f
    )
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent
    )
    this.focusable(interactionSource = interactionSource)
        .scale(scale)
        .border(
            width = 1.2.dp,
            borderColor,
            shape = RoundedCornerShape(24.dp)
        )
}