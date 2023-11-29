package com.rodrigmatrix.weatheryou.addlocation

import androidx.annotation.StringRes

sealed class AddLocationViewEffect : com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect {

    data class ShowError(@StringRes val string: Int) : AddLocationViewEffect()

    data class ShowErrorString(val string: String) : AddLocationViewEffect()

    object LocationAdded : AddLocationViewEffect()
}
