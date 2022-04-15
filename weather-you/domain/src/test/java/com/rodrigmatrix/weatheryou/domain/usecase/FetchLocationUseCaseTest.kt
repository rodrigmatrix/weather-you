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


internal class FetchLocationUseCaseTest {

    private val weatherRepository = mockk<WeatherRepository>(relaxed = true)
    private val useCase = FetchLocationUseCase(weatherRepository)

    @Test
    fun `invoke Should verify fetchLocation call`() = runBlocking {
        // Given
        val latitude = 1.0
        val longitude = 2.0
        val location = createWeatherLocation(name = "Sao Paulo")
        every {
            weatherRepository.fetchLocation(latitude, longitude)
        } returns flow { emit(location) }

        // When
        val result = useCase(latitude, longitude)

        // Then
        result.test {
            verify {
                weatherRepository.fetchLocation(latitude, longitude)
            }
            assertEquals(location, awaitItem())
            awaitComplete()
        }
    }
}