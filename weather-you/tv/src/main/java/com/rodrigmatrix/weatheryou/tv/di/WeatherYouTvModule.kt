package com.rodrigmatrix.weatheryou.tv.di

import com.rodrigmatrix.weatheryou.home.di.HomeModule
import com.rodrigmatrix.weatheryou.locationdetails.di.LocationDetailsModule
import com.rodrigmatrix.weatheryou.settings.di.SettingsModule
import com.rodrigmatrix.weatheryou.tv.presentation.locations.TVWeatherLocationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object WeatherYouTvModule {

    fun loadModules() {
        loadKoinModules(presentationModule)
        SettingsModule.loadModules()
        HomeModule.loadModules()
        LocationDetailsModule.loadModules()
    }

    private val presentationModule = module {
        viewModel {
            TVWeatherLocationsViewModel(
                updateLocationsUseCase = get(),
                deleteLocationUseCase = get(),
                getLocationsUseCase = get(),
            )
        }
    }
}