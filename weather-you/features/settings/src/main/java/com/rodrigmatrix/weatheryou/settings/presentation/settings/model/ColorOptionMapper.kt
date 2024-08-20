package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import com.rodrigmatrix.weatheryou.components.theme.ColorMode
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference

fun ColorMode.toPreference() = when (this) {
    ColorMode.Dynamic -> AppColorPreference.DYNAMIC
    ColorMode.Default -> AppColorPreference.DEFAULT
    ColorMode.Mosque -> AppColorPreference.MOSQUE
    ColorMode.DarkFern -> AppColorPreference.DARK_FERN
    ColorMode.FreshEggplant -> AppColorPreference.FRESH_EGGPLANT
    ColorMode.Carmine -> AppColorPreference.CARMINE
    ColorMode.Cinnamon -> AppColorPreference.CINNAMON
    ColorMode.PeruTan -> AppColorPreference.PERU_TAN
    ColorMode.Gigas -> AppColorPreference.GIGAS
}
