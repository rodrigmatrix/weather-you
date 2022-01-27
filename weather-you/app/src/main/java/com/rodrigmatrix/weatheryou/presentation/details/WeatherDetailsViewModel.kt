package com.rodrigmatrix.weatheryou.presentation.details

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

class WeatherDetailsViewModel(

) : ViewModel<WeatherDetailsViewState, WeatherDetailsViewEffect>(WeatherDetailsViewState()) {

    fun setWeatherLocation(weatherLocation: WeatherLocation) {
        setState {
            it.copy(
                weatherLocation = weatherLocation,
                todayWeatherHoursList = weatherLocation.hours,
                futureDaysList = weatherLocation
                    .days
                    .take(if (it.isFutureWeatherExpanded) 15 else 7)
            )
        }
    }

    fun onFutureWeatherButtonClick(isExpanded: Boolean) {
        setState {
            it.copy(
                futureDaysList = it.futureDaysList.take(if (isExpanded) 15 else 7),
                isFutureWeatherExpanded = isExpanded
            )
        }
    }
}