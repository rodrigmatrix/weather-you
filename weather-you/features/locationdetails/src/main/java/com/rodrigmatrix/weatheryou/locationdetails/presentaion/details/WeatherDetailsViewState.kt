package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

data class WeatherDetailsViewState(
    val isLoading: Boolean = false,
    val weatherLocation: WeatherLocation? = null,
    val todayWeatherHoursList: List<WeatherHour> = emptyList(),
    val futureDaysList: List<WeatherDay> = emptyList(),
    val isFutureWeatherExpanded: Boolean = false,
    val enableWeatherAnimations: Boolean = false,
    val enableThemeColorWithWeatherAnimations: Boolean = false,
    val isFullScreenMode: Boolean = false,
): ViewState