package com.rodrigmatrix.weatheryou.presentation.navigation

sealed class Screen(
    val route: String
) {
    object Home: Screen("home")
    object Details: Screen("details")
}