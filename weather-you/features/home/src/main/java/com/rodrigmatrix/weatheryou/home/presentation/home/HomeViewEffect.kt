package com.rodrigmatrix.weatheryou.home.presentation.home

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed class HomeViewEffect : com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect {

    data class Error(val message: String): HomeViewEffect()
}