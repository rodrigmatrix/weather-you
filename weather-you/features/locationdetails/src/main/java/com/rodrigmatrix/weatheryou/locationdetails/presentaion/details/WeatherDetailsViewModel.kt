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
    weatherLocation: WeatherLocation?,
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
                            weatherLocation = weatherLocation,
                            todayWeatherHoursList = weatherLocation?.hours.orEmpty(),
                            futureDaysList = weatherLocation
                                ?.days
                                ?.getFutureDaysList(it.isFutureWeatherExpanded)
                                .orEmpty(),
                            enableThemeColorWithWeatherAnimations = settings.enableThemeColorWithWeatherAnimations,
                            enableWeatherAnimations = settings.enableWeatherAnimations,
                        )
                    }
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

    fun onFullScreenModeChange(isFullScreenMode: Boolean) {
        setState {
            it.copy(isFullScreenMode = isFullScreenMode)
        }
    }

    private fun List<WeatherDay>.getFutureDaysList(isExpanded: Boolean): List<WeatherDay> {
        return this.toMutableList().take(if (isExpanded) EXPANDED_LIST_SIZE else COLLAPSED_LIST_SIZE)
    }
}