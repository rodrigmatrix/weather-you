package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetLocationUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val EXPANDED_LIST_SIZE = 15
private const val COLLAPSED_LIST_SIZE = 7

class WeatherDetailsViewModel(
    private val weatherLocation: WeatherLocation?,
    private val getLocationUseCase: GetLocationUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
) : ViewModel<WeatherDetailsViewState, WeatherDetailsViewEffect>(
    WeatherDetailsViewState()
) {

    init {
        viewModelScope.launch {
            getAppSettingsUseCase()
                .collect { settings ->
                    setState {
                        it.copy(
                            enableThemeColorWithWeatherAnimations = settings.enableThemeColorWithWeatherAnimations,
                            enableWeatherAnimations = settings.enableWeatherAnimations,
                        )
                    }
                }
        }
        getLocation()
    }

    private fun getLocation() {
        viewModelScope.launch {
            getLocationUseCase(
                id = weatherLocation?.id ?: return@launch,
                isCurrentLocation = weatherLocation.isCurrentLocation,
            )
                .onStart {
                    setState {
                        it.copy(
                            weatherLocation = weatherLocation,
                            todayWeatherHoursList = weatherLocation.hours,
                            isFutureWeatherExpanded = true,
                            futureDaysList = weatherLocation.days,
                        )
                    }
                }
                .catch {
                    setState {
                        it.copy(
                            weatherLocation = weatherLocation,
                            todayWeatherHoursList = weatherLocation.hours,
                            isFutureWeatherExpanded = true,
                            futureDaysList = weatherLocation.days,
                        )
                    }
                }
                .collect { location ->
                    setState {
                        it.copy(
                            weatherLocation = location,
                            todayWeatherHoursList = location.hours,
                            isFutureWeatherExpanded = true,
                            futureDaysList = location.days,
                        )
                    }
                }
        }
    }

    fun onFutureWeatherButtonClick(isExpanded: Boolean) {
        setState {
            it.copy(
                futureDaysList = it.weatherLocation?.days.orEmpty(),
                isFutureWeatherExpanded = isExpanded
            )
        }
    }

    fun onFullScreenModeChange(isFullScreenMode: Boolean) {
        setState {
            it.copy(isFullScreenMode = isFullScreenMode)
        }
    }

    private fun List<WeatherDay>.getFutureDaysList(isExpanded: Boolean): List<WeatherDay> {
        return this.toMutableList().take(if (isExpanded) EXPANDED_LIST_SIZE else COLLAPSED_LIST_SIZE)
    }
}