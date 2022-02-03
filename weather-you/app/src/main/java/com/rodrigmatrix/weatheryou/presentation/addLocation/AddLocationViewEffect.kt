package com.rodrigmatrix.weatheryou.presentation.addLocation

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed class AddLocationViewEffect : ViewEffect {

    data class ShowError(val string: String) : AddLocationViewEffect()

    object LocationAdded : AddLocationViewEffect()
}
