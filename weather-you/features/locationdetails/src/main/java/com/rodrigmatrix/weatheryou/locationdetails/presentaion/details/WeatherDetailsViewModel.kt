package com.rodrigmatrix.weatheryou.locationdetails.presentaion.details

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import kotlinx.coroutines.launch

private const val EXPANDED_LIST_SIZE = 15
private const val COLLAPSED_LIST_SIZE = 7

class WeatherDetailsViewModel(
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
    }

    fun setWeatherLocation(weatherLocation: WeatherLocation?) {
        if (weatherLocation != null) {
            setState {
                it.copy(
                    weatherLocation = weatherLocation,
                    todayWeatherHoursList = weatherLocation.hours,
                    futureDaysList = weatherLocation
                        .days
                        .getFutureDaysList(it.isFutureWeatherExpanded)
                )
            }
        }
    }

    fun onFutureWeatherButtonClick(isExpanded: Boolean) {
        setState {
            it.copy(
                futureDaysList = it.weatherLocation?.days?.getFutureDaysList(isExpanded).orEmpty(),
                isFutureWeatherExpanded = isExpanded
            )
        }
    }

    private fun List<WeatherDay>.getFutureDaysList(isExpanded: Boolean): List<WeatherDay> {
        return this.toMutableList().take(if (isExpanded) EXPANDED_LIST_SIZE else COLLAPSED_LIST_SIZE)
    }
}