package com.rodrigmatrix.weatheryou.core.viewmodel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.lifecycle.ViewModel as AndroidxViewModel

abstract class ViewModel<State: ViewState, Effect: ViewEffect>(
    initialState: State
): AndroidxViewModel() {

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<State> = _viewState

    private val _viewEffect = Channel<Effect>()
    val viewEffect: Flow<Effect> = _viewEffect.receiveAsFlow()

    protected fun setState(newState: (State) -> State) {
        _viewState.value = newState(viewState.value)
    }

     protected fun setEffect(newEffect: () -> Effect) {
        _viewEffect.trySend(newEffect())
    }
}