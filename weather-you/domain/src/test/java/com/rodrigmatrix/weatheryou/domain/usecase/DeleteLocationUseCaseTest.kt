package com.rodrigmatrix.weatheryou.domain.usecase

import app.cash.turbine.test
import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class DeleteLocationUseCaseTest {

    private val weatherRepository = mockk<WeatherRepository>(relaxed = true)
    private val useCase = DeleteLocationUseCase(weatherRepository)

    @Test
    fun `invoke Should verify deleteLocation call`() = runBlocking {
        // Given
        val id = 123

        // When
        val result = useCase(id)

        // Then
        result.test {
            verify {
                weatherRepository.deleteLocation(id)
            }
            awaitComplete()
        }
    }
}