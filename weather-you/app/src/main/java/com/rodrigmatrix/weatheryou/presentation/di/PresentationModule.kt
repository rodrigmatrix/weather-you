package com.rodrigmatrix.weatheryou.presentation.di

import com.rodrigmatrix.weatheryou.presentation.addLocation.AddLocationViewModel
import com.rodrigmatrix.weatheryou.presentation.details.WeatherDetailsViewModel
import com.rodrigmatrix.weatheryou.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { HomeViewModel(weatherRepository = get()) }
    viewModel { WeatherDetailsViewModel() }
    viewModel { AddLocationViewModel(weatherRepository = get()) }
}