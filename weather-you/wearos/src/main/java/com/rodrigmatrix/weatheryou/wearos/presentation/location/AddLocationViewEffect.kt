package com.rodrigmatrix.weatheryou.wearos.presentation.location

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

sealed interface AddLocationViewEffect : ViewEffect {

    data class ShowError(@StringRes val string: Int) : AddLocationViewEffect

    data class ShowErrorString(val string: String) : AddLocationViewEffect

    object LocationAdded : AddLocationViewEffect
}
