package com.rodrigmatrix.weatheryou.addlocation

sealed class AddLocationViewEffect : com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect {

    data class ShowError(val string: String) : AddLocationViewEffect()

    object LocationAdded : AddLocationViewEffect()
}
