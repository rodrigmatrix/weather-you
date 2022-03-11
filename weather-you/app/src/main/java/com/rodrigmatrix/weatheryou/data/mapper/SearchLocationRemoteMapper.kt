package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.core.map.Map
import com.rodrigmatrix.weatheryou.data.model.search.SearchLocationResponse
import com.rodrigmatrix.weatheryou.domain.model.SearchLocation

class SearchLocationRemoteMapper: Map<SearchLocationResponse, SearchLocation>() {

    override fun map(source: SearchLocationResponse): SearchLocation {
        val location = source.results.orEmpty().first()
        val geometry = source.results.orEmpty().first().geometry?.location
        return SearchLocation(
            name = location.formattedAddress.orEmpty(),
            latitude = geometry?.lat ?: 0.0,
            longitude = geometry?.lng ?: 0.0
        )
    }
}
