package com.rodrigmatrix.weatheryou.data.repository

import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FlowFirstOrFallbackTest {

    @Test
    fun `primary value avoids last known lookup`() = runBlocking {
        var fallbackCollected = false

        val result = flowOf("fresh")
            .firstOrNullOrFallback(flow {
                fallbackCollected = true
                emit("last-known")
            })

        assertEquals("fresh", result)
        assertFalse(fallbackCollected)
    }

    @Test
    fun `primary failure falls back to last known location`() = runBlocking {
        val failures = mutableListOf<Pair<String, String>>()

        val result = failingFlow<String>(IOException("fresh location unavailable"))
            .firstOrNullOrFallback(flowOf("last-known")) { stage, failure ->
                failures += stage to failure.javaClass.simpleName
            }

        assertEquals("last-known", result)
        assertEquals(listOf("current" to "IOException"), failures)
    }

    @Test
    fun `empty primary flow falls back to last known location`() = runBlocking {
        val result = flowOf<String>()
            .firstOrNullOrFallback(flowOf("last-known"))

        assertEquals("last-known", result)
    }

    @Test
    fun `both lookup failures return no value and report both stages`() = runBlocking {
        val failures = mutableListOf<Pair<String, String>>()

        val result = failingFlow<String>(IOException("fresh unavailable"))
            .firstOrNullOrFallback(failingFlow(IOException("last-known unavailable"))) { stage, failure ->
                failures += stage to failure.message.orEmpty()
            }

        assertNull(result)
        assertEquals(
            listOf("current" to "fresh unavailable", "last_known" to "last-known unavailable"),
            failures,
        )
    }

    @Test
    fun `telemetry failure does not prevent last known fallback`() = runBlocking {
        val result = failingFlow<String>(IOException("fresh unavailable"))
            .firstOrNullOrFallback(flowOf("last-known")) { _, _ ->
                error("analytics unavailable")
            }

        assertEquals("last-known", result)
    }

    @Test
    fun `cancellation is propagated without trying fallback`() = runBlocking {
        var fallbackCollected = false

        val failure = runCatching {
            failingFlow<String>(CancellationException("cancelled"))
                .firstOrNullOrFallback(flow {
                    fallbackCollected = true
                    emit("last-known")
                })
        }.exceptionOrNull()

        assertTrue(failure is CancellationException)
        assertFalse(fallbackCollected)
    }

    private fun <T> failingFlow(failure: Throwable): Flow<T> = flow {
        throw failure
    }
}
