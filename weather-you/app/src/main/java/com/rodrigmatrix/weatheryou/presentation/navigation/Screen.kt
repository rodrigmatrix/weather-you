package com.rodrigmatrix.weatheryou.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.R

enum class HomeEntry(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val stringRes: Int
) {
    Home("home", R.drawable.ic_home, R.string.home),
    Settings("settings", R.drawable.ic_settings, R.string.settings),
    About("about", R.drawable.ic_info, R.string.about)
}

val Array<HomeEntry>.routes
    get() = this.map { it.route }