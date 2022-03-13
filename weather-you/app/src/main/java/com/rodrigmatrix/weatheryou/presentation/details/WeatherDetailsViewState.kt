package com.rodrigmatrix.weatheryou.presentation.details

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class WeatherDetailsViewState(
    val isLoading: Boolean = false,
    val weatherLocation: com.rodrigmatrix.weatheryou.domain.model.WeatherLocation? = null,
    val todayWeatherHoursList: List<com.rodrigmatrix.weatheryou.domain.model.WeatherHour> = emptyList(),
    val futureDaysList: List<com.rodrigmatrix.weatheryou.domain.model.WeatherDay> = emptyList(),
    val isFutureWeatherExpanded: Boolean = false
): com.rodrigmatrix.weatheryou.core.viewmodel.ViewState