package com.rodrigmatrix.weatheryou.settings.di

import com.rodrigmatrix.weatheryou.domain.usecase.GetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SetAppSettingsUseCase
import com.rodrigmatrix.weatheryou.settings.presentation.settings.SettingsViewModel
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManager
import com.rodrigmatrix.weatheryou.settings.utils.AppThemeManagerImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object SettingsModule {

    fun loadModules() {
        loadKoinModules(presentationModule + otherModule)
    }

    private val presentationModule = module {
        viewModel {
            SettingsViewModel(
                getAppSettingsUseCase = get(),
                setAppSettingsUseCase = get(),
            )
        }
    }

    private val otherModule = module {
        factory<AppThemeManager> { AppThemeManagerImpl(getAppSettingsUseCase = get()) }
    }
}