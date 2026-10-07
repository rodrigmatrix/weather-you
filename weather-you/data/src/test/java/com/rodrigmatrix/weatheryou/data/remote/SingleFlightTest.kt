package com.rodrigmatrix.weatheryou.data.remote

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

class SingleFlightTest {

    @Test
    fun `concurrent callers with the same key share one result`() = runBlocking {
        val singleFlight = SingleFlight<String, String>()
        val callCount = AtomicInteger()
        val operationStarted = CompletableDeferred<Unit>()
        val finishOperation = CompletableDeferred<Unit>()

        val first = async {
            singleFlight.execute("weather:51.5,-0.1") {
                callCount.incrementAndGet()
                operationStarted.complete(Unit)
                finishOperation.await()
                "weather-response"
            }
        }
        operationStarted.await()

        val second = async {
            singleFlight.execute("weather:51.5,-0.1") {
                callCount.incrementAndGet()
                "unexpected-second-response"
            }
        }
        yield()
        finishOperation.complete(Unit)

        assertEquals("weather-response", first.await())
        assertEquals("weather-response", second.await())
        assertEquals(1, callCount.get())
    }

    @Test
    fun `different keys run independent operations`() = runBlocking {
        val singleFlight = SingleFlight<String, String>()
        val first = CompletableDeferred<Unit>()
        val second = CompletableDeferred<Unit>()
        val finishFirst = CompletableDeferred<Unit>()

        val firstResult = async {
            singleFlight.execute("weather:london") {
                first.complete(Unit)
                finishFirst.await()
                "london"
            }
        }
        first.await()
        val secondResult = async {
            singleFlight.execute("weather:tokyo") {
                second.complete(Unit)
                "tokyo"
            }
        }

        withTimeout(1_000) { second.await() }
        finishFirst.complete(Unit)
        assertEquals("london", firstResult.await())
        assertEquals("tokyo", secondResult.await())
    }

    @Test
    fun `a failed operation is removed so a later caller can retry`() = runBlocking {
        val singleFlight = SingleFlight<String, String>()
        val failure = runCatching {
            singleFlight.execute("weather:51.5,-0.1") { throw IOException("temporary failure") }
        }.exceptionOrNull()

        assertEquals("temporary failure", failure?.message)
        assertEquals("recovered", singleFlight.execute("weather:51.5,-0.1") { "recovered" })
    }

    @Test
    fun `active waiters coalesce again when the operation leader is cancelled`() = runBlocking {
        val singleFlight = SingleFlight<String, String>()
        val firstOperationStarted = CompletableDeferred<Unit>()
        val firstOperationCancelled = CompletableDeferred<Unit>()
        val retryStarted = CompletableDeferred<Unit>()
        val finishRetry = CompletableDeferred<Unit>()
        val callCount = AtomicInteger()

        val leader = async {
            singleFlight.execute("weather:51.5,-0.1") {
                callCount.incrementAndGet()
                firstOperationStarted.complete(Unit)
                try {
                    awaitCancellation()
                } finally {
                    firstOperationCancelled.complete(Unit)
                }
            }
        }
        firstOperationStarted.await()

        val firstWaiter = async {
            singleFlight.execute("weather:51.5,-0.1") {
                callCount.incrementAndGet()
                retryStarted.complete(Unit)
                finishRetry.await()
                "retried-response"
            }
        }
        val secondWaiter = async {
            singleFlight.execute("weather:51.5,-0.1") {
                callCount.incrementAndGet()
                "unexpected-response"
            }
        }
        yield()
        leader.cancel()
        withTimeout(1_000) { firstOperationCancelled.await() }
        withTimeout(1_000) { retryStarted.await() }
        yield()
        finishRetry.complete(Unit)

        assertEquals("retried-response", withTimeout(1_000) { firstWaiter.await() })
        assertEquals("retried-response", withTimeout(1_000) { secondWaiter.await() })
        assertEquals(2, callCount.get())
    }

    @Test
    fun `cancelling one waiter does not cancel the leader or another waiter`() = runBlocking {
        val singleFlight = SingleFlight<String, String>()
        val operationStarted = CompletableDeferred<Unit>()
        val finishOperation = CompletableDeferred<Unit>()
        val callCount = AtomicInteger()

        val leader = async {
            singleFlight.execute("weather:51.5,-0.1") {
                callCount.incrementAndGet()
                operationStarted.complete(Unit)
                finishOperation.await()
                "weather-response"
            }
        }
        operationStarted.await()
        val cancelledWaiter = async {
            singleFlight.execute("weather:51.5,-0.1") { "unexpected-response" }
        }
        val remainingWaiter = async {
            singleFlight.execute("weather:51.5,-0.1") { "unexpected-response" }
        }
        yield()

        cancelledWaiter.cancelAndJoin()
        assertTrue(leader.isActive)
        finishOperation.complete(Unit)

        assertEquals("weather-response", leader.await())
        assertEquals("weather-response", remainingWaiter.await())
        assertEquals(1, callCount.get())
    }
}
