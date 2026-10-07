package com.rodrigmatrix.weatheryou.data.remote.weatherkit

import com.rodrigmatrix.weatheryou.data.mapper.WeatherKitConditionMapper
import com.rodrigmatrix.weatheryou.data.mapper.WeatherKitRemoteMapper
import com.rodrigmatrix.weatheryou.data.model.weatherkit.CurrentWeather
import com.rodrigmatrix.weatheryou.data.model.weatherkit.Day
import com.rodrigmatrix.weatheryou.data.model.weatherkit.ForecastDaily
import com.rodrigmatrix.weatheryou.data.model.weatherkit.Metadata
import com.rodrigmatrix.weatheryou.data.model.weatherkit.WeatherKitLocationResponse
import com.rodrigmatrix.weatheryou.data.service.WeatherKitService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger

class WeatherKitRemoteDataSourceImplTest {

    @Test
    fun `concurrent collections for the same location make one service request`() = runBlocking {
        val service = FakeWeatherKitService()
        val dataSource = WeatherKitRemoteDataSourceImpl(
            weatherKitService = service,
            weatherKitRemoteMapper = WeatherKitRemoteMapper(WeatherKitConditionMapper()),
        )
        val first = async {
            dataSource.getWeather(51.5072, -0.1276, "gb", "Europe/London").first()
        }
        withTimeout(1_000) { service.requestStarted.await() }

        val second = async {
            dataSource.getWeather(51.5072, -0.1276, "gb", "Europe/London").first()
        }
        yield()
        service.finishRequest.complete(Unit)

        assertEquals(51.5072, first.await().latitude, 0.0001)
        assertEquals(51.5072, second.await().latitude, 0.0001)
        assertEquals(1, service.callCount.get())
    }

    @Test
    fun `different locations make independent service requests`() = runBlocking {
        val service = FakeWeatherKitService()
        val dataSource = WeatherKitRemoteDataSourceImpl(
            weatherKitService = service,
            weatherKitRemoteMapper = WeatherKitRemoteMapper(WeatherKitConditionMapper()),
        )
        val london = async {
            dataSource.getWeather(51.5072, -0.1276, "gb", "Europe/London").first()
        }
        withTimeout(1_000) { service.requestStarted.await() }
        val tokyo = async {
            dataSource.getWeather(35.6762, 139.6503, "jp", "Asia/Tokyo").first()
        }
        withTimeout(1_000) {
            while (service.callCount.get() < 2) yield()
        }
        service.finishRequest.complete(Unit)

        assertEquals(51.5072, london.await().latitude, 0.0001)
        assertEquals(35.6762, tokyo.await().latitude, 0.0001)
        assertEquals(2, service.callCount.get())
    }

    @Test
    fun `failed service request is removed so the next request can retry`() = runBlocking {
        val service = FakeWeatherKitService(failFirstRequest = true)
        val dataSource = WeatherKitRemoteDataSourceImpl(
            weatherKitService = service,
            weatherKitRemoteMapper = WeatherKitRemoteMapper(WeatherKitConditionMapper()),
        )

        val firstFailure = runCatching {
            dataSource.getWeather(51.5072, -0.1276, "gb", "Europe/London").first()
        }.exceptionOrNull()
        service.finishRequest.complete(Unit)
        val retry = dataSource.getWeather(51.5072, -0.1276, "gb", "Europe/London").first()

        assertEquals("temporary service failure", firstFailure?.message)
        assertEquals(51.5072, retry.latitude, 0.0001)
        assertEquals(2, service.callCount.get())
    }

    @Test
    fun `locale with only a language is sent as a valid language tag`() = runBlocking {
        val previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.forLanguageTag("en"))
        try {
            val service = FakeWeatherKitService()
            service.finishRequest.complete(Unit)
            val dataSource = WeatherKitRemoteDataSourceImpl(
                weatherKitService = service,
                weatherKitRemoteMapper = WeatherKitRemoteMapper(WeatherKitConditionMapper()),
            )

            dataSource.getWeather(51.5072, -0.1276, "gb", "Europe/London").first()

            assertEquals("en", service.requestedLocale)
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    private class FakeWeatherKitService(
        private val failFirstRequest: Boolean = false,
    ) : WeatherKitService {
        val callCount = AtomicInteger()
        val requestStarted = CompletableDeferred<Unit>()
        val finishRequest = CompletableDeferred<Unit>()
        var requestedLocale: String? = null

        override suspend fun getWeather(
            locale: String,
            latitude: Double,
            longitude: Double,
            dataSets: String,
            timezone: String,
            countryCode: String,
        ): WeatherKitLocationResponse {
            val callNumber = callCount.incrementAndGet()
            requestedLocale = locale
            requestStarted.complete(Unit)
            if (failFirstRequest && callNumber == 1) throw IOException("temporary service failure")
            finishRequest.await()
            return WeatherKitLocationResponse(
                currentWeather = CurrentWeather(
                    temperature = 21.0,
                    metadata = Metadata(expireTime = "2099-01-01T00:00:00Z"),
                ),
                forecastDaily = ForecastDaily(
                    days = listOf(Day(temperatureMax = 25.0, temperatureMin = 10.0)),
                ),
            )
        }
    }
}
