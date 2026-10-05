package com.rodrigmatrix.weatheryou.core.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.State
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.isActive

@Composable
fun produceParticleTick(enabled: Boolean = true): State<Long> {
    return produceState(initialValue = 0L, enabled) {
        if (!enabled) return@produceState
        val startTime = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameTime ->
                value = (frameTime - startTime) / 1_000_000
            }
        }
    }
}
