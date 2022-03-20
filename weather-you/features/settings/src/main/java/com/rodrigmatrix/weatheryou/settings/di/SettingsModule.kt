package com.rodrigmatrix.weatheryou.settings.di

import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object SettingsModule {

    fun loadModules() {
        loadKoinModules(presentationModule)
    }

    private val presentationModule = module {
        viewModel {
            SettingsViewModel()
        }
    }
}