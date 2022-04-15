package com.rodrigmatrix.weatheryou.domain.usecase

import app.cash.turbine.test
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import com.rodrigmatrix.weatheryou.domain.stub.createWeatherLocation
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FetchLocationsUseCaseTest {

    private val weatherRepository = mockk<WeatherRepository>(relaxed = true)
    private val useCase = FetchLocationsUseCase(weatherRepository)

    @Test
    fun `invoke Should verify fetchLocationsList call`() = runBlocking {
        // Given
        val locationsList = listOf(
            createWeatherLocation(name = "Sao Paulo"),
            createWeatherLocation(name = "Sydney")
        )
        every {
            weatherRepository.fetchLocationsList()
        } returns flow { emit(locationsList) }

        // When
        val result = useCase()

        // Then
        result.test {
            verify {
                weatherRepository.fetchLocationsList()
            }
            assertEquals(locationsList, awaitItem())
            awaitComplete()
        }
    }
}