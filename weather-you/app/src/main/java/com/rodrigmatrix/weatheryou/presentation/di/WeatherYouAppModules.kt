package com.rodrigmatrix.weatheryou.presentation.di

import com.rodrigmatrix.weatheryou.presentation.widget.CurrentWeatherWidgetConfigurationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object WeatherYouAppModules {

    fun loadModules() {
        loadKoinModules(
            listOf(
                viewModelModule,
            )
        )
    }

    private val viewModelModule = module {
        viewModel {
            CurrentWeatherWidgetConfigurationViewModel(
                setWidgetLocationUseCase = get(),
                getWidgetTemperatureUseCase = get(),
                deleteWidgetLocationUseCase = get(),
                fetchLocationsUseCase = get(),
            )
        }
    }
}