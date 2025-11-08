package com.rodrigmatrix.weatheryou.core.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.withFrameNanos
import kotlinx.coroutines.isActive

@Composable
fun produceParticleTick(): Long {
    val particleTick by produceState(initialValue = 0L) {
        val startTime = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameTime ->
                value = (frameTime - startTime) / 1_000_000
            }
        }
    }
    return particleTick
}