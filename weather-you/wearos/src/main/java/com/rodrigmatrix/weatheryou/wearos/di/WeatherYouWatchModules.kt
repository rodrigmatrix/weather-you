package com.rodrigmatrix.weatheryou.wearos.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewModel
import com.rodrigmatrix.weatheryou.wearos.presentation.location.AddLocationViewModel
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
        viewModel {
            AddLocationViewModel(
                addLocationUseCase = get(),
                getFamousLocationsUseCase = get(),
                searchLocationUseCase = get(),
                firebaseCrashlytics = FirebaseCrashlytics.getInstance(),
                firebaseAnalytics = get(),
            )
        }
    }
}