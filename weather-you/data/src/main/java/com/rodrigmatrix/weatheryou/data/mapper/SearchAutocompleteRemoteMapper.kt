package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.core.map.Mapper
import com.rodrigmatrix.weatheryou.data.model.search.LocationFirebase
import com.rodrigmatrix.weatheryou.data.model.search.NinjasCityResponse
import com.rodrigmatrix.weatheryou.data.model.search.SearchAutoCompleteResponse
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation

class SearchAutocompleteRemoteMapper : Mapper<List<NinjasCityResponse>, List<SearchAutocompleteLocation>>() {

    override fun map(source: List<NinjasCityResponse>): List<SearchAutocompleteLocation> {
        return source.map {
            SearchAutocompleteLocation(
                name = it.name + " " + it.country,
                lat = it.latitude ?: 0.0,
                long = it.longitude ?: 0.0,
                countryCode = it.country.orEmpty(),
                timezone = "",
            )
        }
    }
}
