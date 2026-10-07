package com.rodrigmatrix.weatheryou.data.repository

import com.rodrigmatrix.weatheryou.data.mapper.FamousCitiesMapper
import com.rodrigmatrix.weatheryou.data.remote.search.SearchLocalDataSource
import com.rodrigmatrix.weatheryou.data.remote.search.SearchRemoteDataSource
import com.rodrigmatrix.weatheryou.domain.model.Location
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class SearchRepositoryImplTest {

    @Test
    fun `remote search failure emits local fallback results`() = runBlocking {
        val localResult = Location(
            city = "London",
            state = "England",
            lat = 51.5072,
            long = -0.1276,
            country = "United Kingdom",
            countryCode = "GB",
        )
        val repository = SearchRepositoryImpl(
            searchLocalDataSource = FakeLocalDataSource(listOf(localResult)),
            searchRemoteDataSource = FailingRemoteDataSource(),
            famousCitiesMapper = FamousCitiesMapper(),
        )

        val result = repository.searchLocation("London").first()

        assertEquals(
            listOf(
                SearchAutocompleteLocation(
                    name = "London - England United Kingdom",
                    lat = 51.5072,
                    long = -0.1276,
                    countryCode = "GB",
                    timezone = "",
                )
            ),
            result,
        )
    }

    private class FailingRemoteDataSource : SearchRemoteDataSource {
        override fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>> = flow {
            throw IOException("Remote search unavailable")
        }

        override fun getTimezone(lat: Double, long: Double): Flow<String> = flowOf("")
    }

    private class FakeLocalDataSource(
        private val locations: List<Location>,
    ) : SearchLocalDataSource {
        override fun searchLocation(name: String): Flow<List<Location>> = flowOf(locations)
    }
}
