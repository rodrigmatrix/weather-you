package com.rodrigmatrix.weatheryou.locationdetails.di

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.ConditionsViewModel
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.WeatherDetailsViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object LocationDetailsModule {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel { (weatherLocation: WeatherLocation?) ->
            WeatherDetailsViewModel(
                weatherLocation = weatherLocation,
                getAppSettingsUseCase = get(),
            )
        }
        viewModel { ConditionsViewModel() }
    }
}