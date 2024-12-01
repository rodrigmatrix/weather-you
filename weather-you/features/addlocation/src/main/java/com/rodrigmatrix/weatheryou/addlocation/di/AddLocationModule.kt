package com.rodrigmatrix.weatheryou.addlocation.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object AddLocationModule {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel {
            AddLocationViewModel(
                addLocationUseCase = get(),
                getFamousLocationsUseCase = get(),
                searchLocationUseCase = get(),
                adsManager = get(),
                firebaseCrashlytics = FirebaseCrashlytics.getInstance(),
            )
        }
    }
}