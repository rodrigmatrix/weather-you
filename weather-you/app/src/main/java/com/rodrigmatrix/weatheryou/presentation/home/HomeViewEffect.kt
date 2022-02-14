package com.rodrigmatrix.weatheryou.presentation.home

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed class HomeViewEffect : ViewEffect {

    data class Error(val message: String): HomeViewEffect()
}