package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.domain.R
import com.rodrigmatrix.weatheryou.components.theme.ColorMode

enum class AppColorPreferenceOption(
    @StringRes val title: Int,
    val option: ColorMode,
) {
    DYNAMIC(
        title = R.string.dynamic,
        option = ColorMode.Dynamic,
    ),
    DEFAULT(
        title = R.string.default_app_color,
        option = ColorMode.Default,
    ),
    MOSQUE(
        title = R.string.mosque,
        option = ColorMode.Mosque,
    ),
    DARK_FERN(
        title = R.string.dark_fern,
        option = ColorMode.DarkFern,
    ),
    FRESH_EGGPLANT(
        title = R.string.fresh_eggplant,
        option = ColorMode.FreshEggplant,
    ),
    CARMINE(
        title = R.string.carmine,
        option = ColorMode.Carmine,
    ),
    CINNAMON(
        title = R.string.cinnamon,
        option = ColorMode.Cinnamon,
    ),
    PERU_TAN(
        title = R.string.peru_tan,
        option = ColorMode.PeruTan,
    ),
    GIGAS(
        title = R.string.gigas,
        option = ColorMode.Gigas,
    ),
}