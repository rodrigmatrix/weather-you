package com.rodrigmatrix.weatheryou.domain.usecase

import app.cash.turbine.test
import com.rodrigmatrix.weatheryou.domain.exception.LocationLimitException
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val MAX_LOCATIONS_KEY = "locations_limit"

internal class AddLocationUseCaseTest {

    private val weatherRepository = mockk<WeatherRepository>(relaxed = true)
    private val getRemoteConfigLongUseCase = mockk<GetRemoteConfigLongUseCase>()
    private val useCase = AddLocationUseCase(weatherRepository, getRemoteConfigLongUseCase)

    @Test
    fun `given success request Should verify calls`() = runBlocking {
        // Given
        val locationsSize = 5
        val name = "Location"
        val latitude = 0.0
        val longitude = 0.0
        every {
            weatherRepository.getLocationsSize()
        } returns flow { emit(locationsSize) }
        every {
            getRemoteConfigLongUseCase(MAX_LOCATIONS_KEY)
        } returns 6

        // When
        val result = useCase(name, latitude, longitude)

        // Then
        result.test {
            verifyOrder {
                weatherRepository.getLocationsSize()
                getRemoteConfigLongUseCase(MAX_LOCATIONS_KEY)
                weatherRepository.addLocation(name, latitude, longitude)
            }
            awaitComplete()
        }
    }

    @Test
    fun `given location limit Should verify calls and throw error`() = runBlocking {
        // Given
        val locationsSize = 5
        val name = "Location"
        val latitude = 0.0
        val longitude = 0.0
        every {
            weatherRepository.getLocationsSize()
        } returns flow { emit(locationsSize) }
        every {
            getRemoteConfigLongUseCase(MAX_LOCATIONS_KEY)
        } returns 5

        // When
        val result = useCase(name, latitude, longitude)

        // Then
        result.test {
            verifyOrder {
                weatherRepository.getLocationsSize()
                getRemoteConfigLongUseCase(MAX_LOCATIONS_KEY)
            }
            assertTrue(awaitError() is LocationLimitException)
        }
    }
}