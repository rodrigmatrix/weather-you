package com.rodrigmatrix.weatheryou.di

import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewModel
import com.rodrigmatrix.weatheryou.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object WeatherYouPresentationModules {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel {
            com.rodrigmatrix.weatheryou.presentation.home.HomeViewModel(
                fetchLocationsUseCase = get(),
                deleteLocationUseCase = get()
            )
        }
        viewModel { com.rodrigmatrix.weatheryou.presentation.details.WeatherDetailsViewModel() }
        viewModel {
            com.rodrigmatrix.weatheryou.addlocation.AddLocationViewModel(
                addLocationUseCase = get(),
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