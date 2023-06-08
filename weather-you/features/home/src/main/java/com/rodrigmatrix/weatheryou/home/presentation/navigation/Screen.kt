package com.rodrigmatrix.weatheryou.home.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.home.R
import com.rodrigmatrix.weatheryou.components.R as Strings

enum class HomeEntry(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val stringRes: Int
) {
    Locations("locations", R.drawable.ic_light_mode, Strings.string.locations),
    Settings("settings", R.drawable.ic_settings, Strings.string.settings),
    About("about", R.drawable.ic_info, Strings.string.about)
}

val Array<HomeEntry>.routes
    get() = this.map { it.route }