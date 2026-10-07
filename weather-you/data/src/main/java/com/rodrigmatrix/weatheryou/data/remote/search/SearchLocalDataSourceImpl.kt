package com.rodrigmatrix.weatheryou.data.remote.search

import android.content.Context
import android.util.JsonReader
import android.util.JsonToken
import com.rodrigmatrix.weatheryou.data.R
import com.rodrigmatrix.weatheryou.domain.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import java.io.InputStreamReader
import java.text.Normalizer

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
private const val MAX_LOCAL_SEARCH_RESULTS = 1
private const val LOCAL_SEARCH_TIMEOUT_MILLIS = 3_000L
private const val LOCAL_REGION_SEARCH_TIMEOUT_MILLIS = 1_000L
private const val CANCELLATION_CHECK_INTERVAL = 128

class SearchLocalDataSourceImpl(
    private val context: Context,
) : SearchLocalDataSource {

    override fun searchLocation(name: String): Flow<List<Location>> {
        return flow {
            val query = name.trim()
            emit(if (query.isBlank()) emptyList() else findLocations(query))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun findLocations(query: String): List<Location> {
        val normalizedQuery = query.unaccent()
        val cityMatches = withTimeoutOrNull(LOCAL_SEARCH_TIMEOUT_MILLIS) {
            scanLocations(
                query = query,
                normalizedQuery = normalizedQuery,
                matchRegions = false,
                maxResults = MAX_LOCAL_SEARCH_RESULTS,
            )
        }.orEmpty()
        if (cityMatches.isNotEmpty()) return cityMatches

        // Region and country matches are less relevant than a city-name match.
        // Search them only when the city pass found nothing, with a shorter cap.
        return withTimeoutOrNull(LOCAL_REGION_SEARCH_TIMEOUT_MILLIS) {
            scanLocations(
                query = query,
                normalizedQuery = normalizedQuery,
                matchRegions = true,
                maxResults = MAX_LOCAL_SEARCH_RESULTS,
            )
        } ?: emptyList()
    }

    private suspend fun scanLocations(
        query: String,
        normalizedQuery: String,
        matchRegions: Boolean,
        maxResults: Int,
    ): List<Location> {
        val coroutineContext = currentCoroutineContext()
        val locations = mutableListOf<Location>()
        var scanned = 0

        context.resources.openRawResource(R.raw.world_cities).use { input ->
            JsonReader(InputStreamReader(input, Charsets.UTF_8)).use { reader ->
                reader.beginArray()
                while (reader.hasNext() && locations.size < maxResults) {
                    if (scanned++ % CANCELLATION_CHECK_INTERVAL == 0) {
                        coroutineContext.ensureActive()
                    }
                    reader.beginObject()
                    var city = ""
                    var state = ""
                    var country = ""
                    var countryCode = ""
                    var latitude = 0.0
                    var longitude = 0.0
                    while (reader.hasNext()) {
                        when (reader.nextName()) {
                            "city" -> city = reader.nextNullableString()
                            "admin_name" -> state = reader.nextNullableString()
                            "country" -> country = reader.nextNullableString()
                            "iso2" -> countryCode = reader.nextNullableString()
                            "lat" -> latitude = reader.nextNullableString().toDoubleOrNull() ?: 0.0
                            "lng" -> longitude = reader.nextNullableString().toDoubleOrNull() ?: 0.0
                            else -> reader.skipValue()
                        }
                    }
                    reader.endObject()

                    if (city.matchesSearchQuery(query, normalizedQuery) ||
                        (matchRegions && (
                            country.matchesSearchQuery(query, normalizedQuery) ||
                                countryCode.matchesSearchQuery(query, normalizedQuery) ||
                                state.matchesSearchQuery(query, normalizedQuery)
                            ))
                    ) {
                        locations += Location(
                            city = city,
                            state = state,
                            lat = latitude,
                            long = longitude,
                            country = country,
                            countryCode = countryCode,
                        )
                    }
                }
            }
        }

        return locations
    }

    private fun JsonReader.nextNullableString(): String {
        return if (peek() == JsonToken.NULL) {
            nextNull()
            ""
        } else {
            nextString()
        }
    }

    private fun CharSequence.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(temp, "")
    }
}

internal fun String.matchesSearchQuery(
    query: String,
    normalizedQuery: String,
    accentInsensitive: Boolean = true,
): Boolean {
    if (contains(query, ignoreCase = true) ||
        (normalizedQuery != query && contains(normalizedQuery, ignoreCase = true))
    ) {
        return true
    }

    if (!accentInsensitive) return false

    // Most city data is ASCII. Normalize only the uncommon accented fields so a
    // search such as "sao" still finds "São Paulo" without normalizing all
    // 47k records for every offline search.
    return any { it.code > 127 } &&
        Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace(REGEX_UNACCENT, "")
            .contains(normalizedQuery, ignoreCase = true)
}

private fun String.unaccent(): String =
    REGEX_UNACCENT.replace(Normalizer.normalize(this, Normalizer.Form.NFD), "")
