package com.rodrigmatrix.weatheryou.wearos.presentation.settings

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import com.rodrigmatrix.weatheryou.domain.model.AppSettings

data class SettingsViewState(
    val appSettings: AppSettings = AppSettings.DEFAULT,
) : ViewState
