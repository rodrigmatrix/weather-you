package com.rodrigmatrix.weatheryou.data.remote

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import java.util.concurrent.ConcurrentHashMap

/** Shares a single in-flight operation among callers with the same key. */
internal class SingleFlight<K : Any, V> {
    private val inFlight = ConcurrentHashMap<K, CompletableDeferred<FlightResult<V>>>()

    suspend fun execute(key: K, operation: suspend () -> V): V {
        val result = CompletableDeferred<FlightResult<V>>()
        val existing = inFlight.putIfAbsent(key, result)
        if (existing != null) {
            return when (val sharedResult = existing.await()) {
                is FlightResult.Success -> sharedResult.value
                is FlightResult.Failure -> {
                    if (sharedResult.leaderCancelled) {
                        // Preserve the waiter's cancellation, but retry if only the leader was cancelled.
                        currentCoroutineContext().ensureActive()
                        execute(key, operation)
                    } else {
                        throw sharedResult.cause
                    }
                }
            }
        }

        try {
            val value = operation()
            result.complete(FlightResult.Success(value))
            return value
        } catch (failure: Throwable) {
            val leaderWasCancelled = failure is CancellationException && !currentCoroutineContext().isActive
            // Remove before waking followers, so retries cannot rejoin a completed failure.
            inFlight.remove(key, result)
            result.complete(FlightResult.Failure(failure, leaderWasCancelled))
            throw failure
        } finally {
            inFlight.remove(key, result)
        }
    }

    private sealed interface FlightResult<out V> {
        data class Success<V>(val value: V) : FlightResult<V>
        data class Failure(val cause: Throwable, val leaderCancelled: Boolean) : FlightResult<Nothing>
    }
}
