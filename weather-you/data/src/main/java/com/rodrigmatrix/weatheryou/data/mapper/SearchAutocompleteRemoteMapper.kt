package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.core.map.Mapper
import com.rodrigmatrix.weatheryou.data.model.search.SearchAutoCompleteResponse
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation

class SearchAutocompleteRemoteMapper : Mapper<SearchAutoCompleteResponse, List<SearchAutocompleteLocation>>() {

    override fun map(source: SearchAutoCompleteResponse): List<SearchAutocompleteLocation> {
        return source.predictions.orEmpty().map {
            SearchAutocompleteLocation(
                locationName = it.description.orEmpty(),
                placeId = it.placeId.orEmpty()
            )
        }
    }
}
