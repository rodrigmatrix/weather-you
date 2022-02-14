package com.rodrigmatrix.weatheryou.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@NonRestartableComposable
@Composable
fun<S: ViewState, E: ViewEffect> LaunchViewEffect(
    viewModel: ViewModel<S, E>,
    onEffect: suspend (viewEffect: E) -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.viewEffect.onEach { viewEffect ->
            onEffect(viewEffect)
        }.collect()
    }
}