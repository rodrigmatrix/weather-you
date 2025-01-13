package com.rodrigmatrix.weatheryou.wearos.di

import com.rodrigmatrix.weatheryou.wearos.domain.usecase.GetCurrentLocationUseCase
import com.rodrigmatrix.weatheryou.wearos.domain.usecase.GetLocationWeatherUseCase
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object WeatherYouWatchModules {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel {
            HomeViewModel(
                updateLocationsUseCase = get(),
                getLocationsUseCase = get(),
                addLocationUseCase = get(),
            )
        }
    }
}