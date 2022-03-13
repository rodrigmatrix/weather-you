package com.rodrigmatrix.weatheryou.di

import com.rodrigmatrix.weatheryou.presentation.addLocation.AddLocationViewModel
import com.rodrigmatrix.weatheryou.presentation.details.WeatherDetailsViewModel
import com.rodrigmatrix.weatheryou.presentation.home.HomeViewModel
import com.rodrigmatrix.weatheryou.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object WeatherYouPresentationModules {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel { HomeViewModel(weatherRepository = get()) }
        viewModel { WeatherDetailsViewModel() }
        viewModel {
            AddLocationViewModel(
                weatherRepository = get(),
                getFamousLocationsUseCase = get(),
                searchLocationUseCase = get(),
                getLocationUseCase = get()
            )
        }
        viewModel {
            SettingsViewModel()
        }
    }
}