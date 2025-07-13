package com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed interface HomeViewEffect : ViewEffect {

    data class ScrollPager(val page: Int) : HomeViewEffect
}