package com.rodrigmatrix.weatheryou.data.remote.search

import android.content.Context
import com.rodrigmatrix.weatheryou.data.R
import com.rodrigmatrix.weatheryou.data.local.model.LocalLocation
import com.rodrigmatrix.weatheryou.data.mapper.toLocationList
import com.rodrigmatrix.weatheryou.domain.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.text.Normalizer

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

class SearchLocalDataSourceImpl(
    private val context: Context,
) : SearchLocalDataSource {

    private var cachedLocations: List<Location> = emptyList()

    override fun searchLocation(name: String): Flow<List<Location>> {
        val typedLocation = name.unaccent()
        return getLocations().map { locationsList ->
            locationsList.filter { location ->
                val content = location.city.unaccent() + " " +
                        location.country.unaccent() + " " +
                        location.countryCode.unaccent() + " " +
                        location.state.unaccent() + " "
                content.contains(typedLocation, ignoreCase = true)
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getLocations(): Flow<List<Location>> = flow {
        emit(
            cachedLocations.ifEmpty {
                val locationsString = context.resources
                    .openRawResource(R.raw.world_cities)
                    .bufferedReader()
                    .use { it.readText() }
                val locationsList = Json.decodeFromString<List<LocalLocation>>(locationsString)
                    .toLocationList()
                cachedLocations = locationsList
                locationsList
            }
        )
    }

    private fun CharSequence.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(temp, "")
    }
}