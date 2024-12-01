package com.rodrigmatrix.weatheryou.components.particle

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.LongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis

@Composable
fun fps(): LongState {
    val fps = remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        var prevTime = withInfiniteAnimationFrameMillis { it }
        while (true) {
            withFrameMillis { frameTime ->
                fps.longValue = 1000 / (frameTime - prevTime).coerceAtLeast(1L)
                prevTime = frameTime
            }
        }
    }
    return fps
}