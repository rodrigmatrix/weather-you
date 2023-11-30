package com.rodrigmatrix.weatheryou.tv.presentation.search

import androidx.annotation.StringRes
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect

internal sealed class SearchLocationUiAction : ViewEffect {

    data object LocationAdded : SearchLocationUiAction()

    data class ShowError(@StringRes val string: Int) : SearchLocationUiAction()

    data class ShowErrorString(val string: String) : SearchLocationUiAction()
}