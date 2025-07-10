package com.rodrigmatrix.weatheryou.tv.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.rodrigmatrix.weatheryou.domain.R
import kotlinx.serialization.Serializable

enum class TvScreenEntry(
    val route: TvRoutes,
    val icon: ImageVector,
    @StringRes val stringRes: Int
) {
    SEARCH(
        route = TvRoutes.Search,
        icon = Icons.Outlined.Search,
        stringRes = R.string.search_location,
    ),
    Locations(
        route = TvRoutes.Home,
        icon = Icons.Outlined.Home,
        stringRes = R.string.locations,
    ),
    Settings(
        route = TvRoutes.Settings,
        icon = Icons.Outlined.Settings,
        stringRes = R.string.settings
    ),
    About(
        route = TvRoutes.About,
        icon = Icons.Outlined.Info,
        stringRes = R.string.about,
    )
}

interface TvRoutes {

    @Serializable
    object Search : TvRoutes

    @Serializable
    object Home : TvRoutes

    @Serializable
    object Settings : TvRoutes

    @Serializable
    object About : TvRoutes
}

val Array<TvScreenEntry>.routes
    get() = this.map { it.route }