package com.rodrigmatrix.weatheryou.settings.presentation.settings.model

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.domain.model.AppColorPreference

enum class AppColorPreferenceOption(
    @StringRes val title: Int,
    val option: AppColorPreference,
) {
    DYNAMIC(
        title = R.string.dynamic,
        option = AppColorPreference.DYNAMIC,
    ),
    DEFAULT(
        title = R.string.default_app_color,
        option = AppColorPreference.DEFAULT,
    ),
    MOSQUE(
        title = R.string.mosque,
        option = AppColorPreference.MOSQUE,
    ),
    DARK_FERN(
        title = R.string.dark_fern,
        option = AppColorPreference.DARK_FERN,
    ),
    FRESH_EGGPLANT(
        title = R.string.fresh_eggplant,
        option = AppColorPreference.FRESH_EGGPLANT,
    ),
}