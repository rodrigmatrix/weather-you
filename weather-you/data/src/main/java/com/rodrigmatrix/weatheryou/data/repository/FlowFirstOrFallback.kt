package com.rodrigmatrix.weatheryou.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/** Returns the first value from [fallback] when the primary flow is empty or fails. */
internal suspend fun <T> Flow<T>.firstOrNullOrFallback(
    fallback: Flow<T>,
    onFailure: (stage: String, failure: Exception) -> Unit = { _, _ -> },
): T? {
    val primaryValue = try {
        firstOrNull()
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (failure: Exception) {
        reportFailure("current", failure, onFailure)
        null
    }
    if (primaryValue != null) return primaryValue

    return try {
        fallback.firstOrNull()
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (failure: Exception) {
        reportFailure("last_known", failure, onFailure)
        null
    }
}

private fun reportFailure(
    stage: String,
    failure: Exception,
    onFailure: (stage: String, failure: Exception) -> Unit,
) {
    try {
        onFailure(stage, failure)
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (_: Exception) {
        // A telemetry failure must not prevent the last-known location fallback.
    }
}
