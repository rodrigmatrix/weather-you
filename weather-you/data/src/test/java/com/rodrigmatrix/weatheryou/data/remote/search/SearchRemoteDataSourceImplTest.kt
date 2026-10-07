package com.rodrigmatrix.weatheryou.data.remote.search

import com.rodrigmatrix.weatheryou.data.model.locationiq.Address
import com.rodrigmatrix.weatheryou.data.model.locationiq.LocationIqSearchResponseItem
import com.rodrigmatrix.weatheryou.data.model.locationiq.TimezoneResponse
import com.rodrigmatrix.weatheryou.data.service.LocationIqService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class SearchRemoteDataSourceImplTest {

    @Test
    fun `search discards malformed or out of range coordinates`() = runBlocking {
        val service = FakeLocationIqService(
            searchResults = listOf(
                LocationIqSearchResponseItem(
                    displayName = "London, UK",
                    lat = "51.5072",
                    lon = "-0.1276",
                    address = Address(countryCode = "gb"),
                ),
                LocationIqSearchResponseItem(displayName = "Bad latitude", lat = "north", lon = "1"),
                LocationIqSearchResponseItem(displayName = "Out of range", lat = "91", lon = "1"),
                LocationIqSearchResponseItem(displayName = "Bad longitude", lat = "1", lon = "181"),
            ),
        )

        val locations = SearchRemoteDataSourceImpl(service, callTimeoutMillis = 100)
            .searchLocation("London")
            .first()

        assertEquals(1, locations.size)
        assertEquals(51.5072, locations.single().lat, 0.0001)
        assertEquals(-0.1276, locations.single().long, 0.0001)
        assertEquals("gb", locations.single().countryCode)
    }

    @Test
    fun `search timeout returns an IOException for repository fallback`() {
        val service = FakeLocationIqService(searchDelayMillis = 1_000)

        val exception = runCatching {
            runBlocking {
                SearchRemoteDataSourceImpl(service, callTimeoutMillis = 20)
                    .searchLocation("London")
                    .first()
            }
        }.exceptionOrNull()

        assertTrue(exception is IOException)
    }

    @Test
    fun `timezone timeout returns an empty value`() = runBlocking {
        val service = FakeLocationIqService(timezoneDelayMillis = 1_000)

        val timezone = SearchRemoteDataSourceImpl(service, callTimeoutMillis = 20)
            .getTimezone(51.5072, -0.1276)
            .first()

        assertEquals("", timezone)
    }

    private class FakeLocationIqService(
        private val searchResults: List<LocationIqSearchResponseItem> = emptyList(),
        private val searchDelayMillis: Long = 0,
        private val timezoneDelayMillis: Long = 0,
        private val timezoneResponse: TimezoneResponse = TimezoneResponse(),
    ) : LocationIqService {
        override suspend fun searchLocation(
            query: String,
            tag: String,
            acceptLanguage: String,
            limit: Int,
        ): List<LocationIqSearchResponseItem> {
            delay(searchDelayMillis)
            return searchResults
        }

        override suspend fun getTimezone(lat: Double, lon: Double): TimezoneResponse {
            delay(timezoneDelayMillis)
            return timezoneResponse
        }
    }
}
