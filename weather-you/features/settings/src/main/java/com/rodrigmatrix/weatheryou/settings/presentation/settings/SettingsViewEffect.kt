package com.rodrigmatrix.weatheryou.settings.presentation.settings

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed class SettingsViewEffect : ViewEffect {

    data object OnPermissionChanged : SettingsViewEffect()
}
