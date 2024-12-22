package com.rodrigmatrix.weatheryou.locationdetails.di

import com.rodrigmatrix.weatheryou.locationdetails.presentaion.conditions.ConditionsViewModel
import com.rodrigmatrix.weatheryou.locationdetails.presentaion.details.WeatherDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object LocationDetailsModule {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel { WeatherDetailsViewModel(getAppSettingsUseCase = get()) }
        viewModel { ConditionsViewModel() }
    }
}