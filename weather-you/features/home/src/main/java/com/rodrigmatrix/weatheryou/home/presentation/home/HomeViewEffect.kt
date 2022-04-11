package com.rodrigmatrix.weatheryou.home.presentation.home

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed class HomeViewEffect : ViewEffect {

    data class Error(@StringRes val stringRes: Int): HomeViewEffect()
}