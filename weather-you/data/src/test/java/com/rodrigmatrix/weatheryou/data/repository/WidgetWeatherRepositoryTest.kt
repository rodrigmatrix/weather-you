package com.rodrigmatrix.weatheryou.data.repository

import com.google.firebase.analytics.FirebaseAnalytics
import com.rodrigmatrix.weatheryou.data.local.UserLocationDataSource
import com.rodrigmatrix.weatheryou.data.local.WeatherLocalDataSource
import com.rodrigmatrix.weatheryou.data.local.model.WeatherEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherLocationEntity
import com.rodrigmatrix.weatheryou.data.local.model.WeatherWidgetLocationEntity
import com.rodrigmatrix.weatheryou.data.mapper.toWeatherLocation
import com.rodrigmatrix.weatheryou.data.remote.WeatherYouRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.repository.SearchRepository
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import app.cash.turbine.test
import java.io.IOException
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetWeatherRepositoryTest {

    @Test
    fun `widget weather emits the room cache without calling the remote source`() = runBlocking {
        val remoteDataSource = mockk<WeatherYouRemoteDataSource>(relaxed = true)
        val localDataSource = mockk<WeatherLocalDataSource>()
        val widgetLocation = WeatherWidgetLocationEntity(
            id = "42",
            latitude = -23.55,
            longitude = -46.63,
            isCurrentLocation = false,
            name = "Sao Paulo",
            countryCode = "BR",
            timeZone = "America/Sao_Paulo",
        )
        io.mockk.every { localDataSource.getWidgetLocation("42") } returns flowOf(widgetLocation)
        io.mockk.every { localDataSource.getWeather(widgetLocation.latitude, widgetLocation.longitude) } returns
            flowOf(cachedWeather())

        val repository = WeatherRepositoryImpl(
            weatherYouRemoteDataSource = remoteDataSource,
            weatherLocalDataSource = localDataSource,
            userLocationDataSource = mockk<UserLocationDataSource>(relaxed = true),
            searchRepository = mockk<SearchRepository>(relaxed = true),
            applicationContext = mockk(relaxed = true),
            firebaseAnalytics = mockk<FirebaseAnalytics>(relaxed = true),
        )

        repository.getWidgetWeather("42").test {
            val cached = awaitItem()
            assertNotNull(cached)
            assertEquals("Sao Paulo", cached?.name)
            assertEquals(-23.55, cached?.latitude)
            awaitComplete()
        }

        verify(exactly = 0) { remoteDataSource.getWeather(any<Double>(), any<Double>(), any(), any()) }
    }

    @Test
    fun `widget refresh targets coalesce repeated coordinates and current location`() {
        val locations = listOf(
            widgetLocation("1", -23.55, -46.63),
            widgetLocation("2", -23.55, -46.63),
            widgetLocation("3", 51.50, -0.12),
            widgetLocation("4", -23.55, -46.63, current = true),
            widgetLocation("5", -23.55, -46.63, current = true),
        )

        val distinct = locations.distinctWeatherRefreshLocations()

        assertEquals(listOf("1", "3", "4"), distinct.map { it.id })
    }

    @Test
    fun `refresh does not call remote source for fresh cached widget weather`() = runBlocking {
        val remoteDataSource = mockk<WeatherYouRemoteDataSource>(relaxed = true)
        val localDataSource = mockk<WeatherLocalDataSource>()
        val widgetLocation = widgetLocation("42", -23.55, -46.63)
        every { localDataSource.getWidgetWeatherList() } returns flowOf(listOf(widgetLocation))
        every { localDataSource.getWeather(-23.55, -46.63) } returns flowOf(cachedWeather())

        val repository = WeatherRepositoryImpl(
            weatherYouRemoteDataSource = remoteDataSource,
            weatherLocalDataSource = localDataSource,
            userLocationDataSource = mockk<UserLocationDataSource>(relaxed = true),
            searchRepository = mockk<SearchRepository>(relaxed = true),
            applicationContext = mockk(relaxed = true),
            firebaseAnalytics = mockk<FirebaseAnalytics>(relaxed = true),
        )

        repository.fetchWidgetLocationsList(forceUpdate = false).first()

        verify(exactly = 1) { localDataSource.getWeather(-23.55, -46.63) }
        verify(exactly = 0) { remoteDataSource.getWeather(any<Double>(), any<Double>(), any(), any()) }
    }

    @Test
    fun `refresh continues to other widget locations after one location fails`() = runBlocking {
        val firstLocation = widgetLocation("1", -23.55, -46.63)
        val secondLocation = widgetLocation("2", 40.71, -74.00)
        val expiredAt = DateTime.parse("2000-01-01T00:00:00Z").toString("yyyy-MM-dd HH:mm:ss")
        val remoteDataSource = mockk<WeatherYouRemoteDataSource>()
        val localDataSource = mockk<WeatherLocalDataSource>()
        every { localDataSource.getWidgetWeatherList() } returns flowOf(listOf(firstLocation, secondLocation))
        every { localDataSource.getWeather(firstLocation.latitude, firstLocation.longitude) } returns flowOf(
            cachedWeather().copy(
                latitude = firstLocation.latitude,
                longitude = firstLocation.longitude,
                expirationDate = expiredAt,
            )
        )
        every { localDataSource.getWeather(secondLocation.latitude, secondLocation.longitude) } returns flowOf(
            cachedWeather().copy(
                latitude = secondLocation.latitude,
                longitude = secondLocation.longitude,
                expirationDate = expiredAt,
            )
        )
        every {
            remoteDataSource.getWeather(
                firstLocation.latitude,
                firstLocation.longitude,
                firstLocation.countryCode,
                firstLocation.timeZone,
            )
        } returns kotlinx.coroutines.flow.emptyFlow()
        every {
            remoteDataSource.getWeather(
                secondLocation.latitude,
                secondLocation.longitude,
                secondLocation.countryCode,
                secondLocation.timeZone,
            )
        } returns flowOf(
            cachedWeather().copy(
                latitude = secondLocation.latitude,
                longitude = secondLocation.longitude,
                expirationDate = DateTime.now().plusHours(2).toString("yyyy-MM-dd HH:mm:ss"),
            ).toWeatherLocation(id = -1, orderIndex = -1)
        )
        every { localDataSource.upsertWeather(any()) } returns flowOf(Unit)

        val repository = WeatherRepositoryImpl(
            weatherYouRemoteDataSource = remoteDataSource,
            weatherLocalDataSource = localDataSource,
            userLocationDataSource = mockk<UserLocationDataSource>(relaxed = true),
            searchRepository = mockk<SearchRepository>(relaxed = true),
            applicationContext = mockk(relaxed = true),
            firebaseAnalytics = mockk<FirebaseAnalytics>(relaxed = true),
        )

        val result = runCatching {
            repository.fetchWidgetLocationsList(forceUpdate = false).first()
        }

        assertTrue("Expected an IOException after both refresh attempts; got ${result.exceptionOrNull()}", result.exceptionOrNull() is IOException)
        verify(exactly = 1) {
            remoteDataSource.getWeather(
                firstLocation.latitude,
                firstLocation.longitude,
                firstLocation.countryCode,
                firstLocation.timeZone,
            )
        }
        verify(exactly = 1) {
            remoteDataSource.getWeather(
                secondLocation.latitude,
                secondLocation.longitude,
                secondLocation.countryCode,
                secondLocation.timeZone,
            )
        }
    }

    @Test
    fun `ensuring widget location does not retry when there is no cached location`() = runBlocking {
        val localDataSource = mockk<WeatherLocalDataSource>()
        every { localDataSource.getWidgetLocation("42") } returns flowOf(null)
        every { localDataSource.getAllLocations() } returns flowOf(emptyList())
        every { localDataSource.getCurrentLocation() } returns flowOf(null)

        val remoteDataSource = mockk<WeatherYouRemoteDataSource>(relaxed = true)
        val repository = WeatherRepositoryImpl(
            weatherYouRemoteDataSource = remoteDataSource,
            weatherLocalDataSource = localDataSource,
            userLocationDataSource = mockk<UserLocationDataSource>(relaxed = true),
            searchRepository = mockk<SearchRepository>(relaxed = true),
            applicationContext = mockk(relaxed = true),
            firebaseAnalytics = mockk<FirebaseAnalytics>(relaxed = true),
        )

        repository.ensureWidgetLocation("42").first()

        verify(exactly = 0) { localDataSource.saveWidgetLocation(any()) }
        verify(exactly = 0) { remoteDataSource.getWeather(any<Double>(), any<Double>(), any(), any()) }
    }

    @Test
    fun `ensuring widget location stores the cached default only when no choice exists`() = runBlocking {
        val localDataSource = mockk<WeatherLocalDataSource>()
        every { localDataSource.getWidgetLocation("42") } returns flowOf(null)
        every { localDataSource.getAllLocations() } returns flowOf(
            listOf(
                WeatherLocationEntity(
                    id = 7,
                    latitude = -23.55,
                    longitude = -46.63,
                    orderIndex = 0,
                    name = "Sao Paulo",
                    countryCode = "BR",
                    timeZone = "America/Sao_Paulo",
                )
            )
        )
        every { localDataSource.getCurrentLocation() } returns flowOf(null)
        every { localDataSource.getWeather(-23.55, -46.63) } returns flowOf(cachedWeather())
        every { localDataSource.saveWidgetLocation(any()) } returns flowOf(Unit)

        val remoteDataSource = mockk<WeatherYouRemoteDataSource>(relaxed = true)
        val repository = WeatherRepositoryImpl(
            weatherYouRemoteDataSource = remoteDataSource,
            weatherLocalDataSource = localDataSource,
            userLocationDataSource = mockk<UserLocationDataSource>(relaxed = true),
            searchRepository = mockk<SearchRepository>(relaxed = true),
            applicationContext = mockk(relaxed = true),
            firebaseAnalytics = mockk<FirebaseAnalytics>(relaxed = true),
        )

        repository.ensureWidgetLocation("42").first()

        val savedLocation = slot<WeatherWidgetLocationEntity>()
        verify(exactly = 1) { localDataSource.saveWidgetLocation(capture(savedLocation)) }
        assertEquals("42", savedLocation.captured.id)
        assertEquals("Cached name", savedLocation.captured.name)
        assertEquals(-23.55, savedLocation.captured.latitude, 0.0)
        assertEquals(-46.63, savedLocation.captured.longitude, 0.0)
        verify(exactly = 0) { remoteDataSource.getWeather(any<Double>(), any<Double>(), any(), any()) }
    }

    private fun widgetLocation(
        id: String,
        latitude: Double,
        longitude: Double,
        current: Boolean = false,
    ) = WeatherWidgetLocationEntity(
        id = id,
        latitude = latitude,
        longitude = longitude,
        isCurrentLocation = current,
        name = id,
        countryCode = "BR",
        timeZone = "America/Sao_Paulo",
    )

    private fun cachedWeather() = WeatherEntity(
        name = "Cached name",
        latitude = -23.55,
        longitude = -46.63,
        isCurrentLocation = false,
        currentWeather = 21.0,
        currentCondition = "Clear",
        maxTemperature = 25.0,
        lowestTemperature = 18.0,
        feelsLike = 21.0,
        currentTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),
        isDaylight = true,
        expirationDate = DateTime.now().plusHours(1).toString("yyyy-MM-dd HH:mm:ss"),
        timeZone = "America/Sao_Paulo",
        precipitationProbability = 0.0,
        precipitationType = "Clear",
        humidity = 55.0,
        dewPoint = 10.0,
        windSpeed = 2.0,
        windDirection = 180.0,
        uvIndex = 3.0,
        sunrise = DateTime.now().withTimeAtStartOfDay().plusHours(6).toString("yyyy-MM-dd HH:mm:ss"),
        sunset = DateTime.now().withTimeAtStartOfDay().plusHours(18).toString("yyyy-MM-dd HH:mm:ss"),
        visibility = 10.0,
        pressure = 1013.0,
        countryCode = "BR",
        minWeekTemperature = 15.0,
        maxWeekTemperature = 28.0,
        cloudCover = 0.0,
        pressureTrend = "Steady",
        windGust = 3.0,
        days = emptyList(),
        hours = emptyList(),
    )
}
