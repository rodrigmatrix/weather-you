package com.rodrigmatrix.weatheryou.tv.di

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.rodrigmatrix.weatheryou.settings.di.SettingsModule
import com.rodrigmatrix.weatheryou.tv.presentation.locations.WeatherLocationsViewModel
import com.rodrigmatrix.weatheryou.tv.presentation.search.SearchLocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object WeatherYouTvModule {

    fun loadModules() {
        loadKoinModules(presentationModule)
        SettingsModule.loadModules()
    }

    private val presentationModule = module {
        viewModel {
            WeatherLocationsViewModel(
                fetchLocationsUseCase = get(),
                deleteLocationUseCase = get(),
            )
        }
        viewModel {
            SearchLocationViewModel(
                searchLocationUseCase = get(),
                addLocationUseCase = get(),
                getFamousLocationsUseCase = get(),
                firebaseCrashlytics = Firebase.crashlytics,
            )
        }
    }
}