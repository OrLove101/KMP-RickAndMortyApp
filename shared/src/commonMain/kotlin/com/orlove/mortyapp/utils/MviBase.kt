package com.orlove.mortyapp.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface UnidirectionalViewModel<STATE, EVENT, EFFECT> {
    val state: StateFlow<STATE>
    val effect: Flow<EFFECT>

    fun updateUiState(newUiState: STATE)
    fun updateUiState(block: STATE.() -> STATE)
    fun event(event: EVENT)
    fun CoroutineScope.emitSideEffect(effect: EFFECT)
}

class MviContractDelegate<STATE, EVENT, EFFECT> internal constructor(
    initialUiState: STATE,
) : UnidirectionalViewModel<STATE, EVENT, EFFECT> {

    private val _state = MutableStateFlow(initialUiState)
    override val state: StateFlow<STATE>
        get() = _state.asStateFlow()

    private val _effect by lazy { Channel<EFFECT>() }
    override val effect: Flow<EFFECT> by lazy { _effect.receiveAsFlow() }

    override fun event(event: EVENT) {}

    override fun updateUiState(newUiState: STATE) {
        _state.update { newUiState }
    }

    override fun updateUiState(block: STATE.() -> STATE) {
        _state.update(block)
    }

    override fun CoroutineScope.emitSideEffect(effect: EFFECT) {
        this.launch { _effect.send(effect) }
    }
}

fun <STATE, EVENT, EFFECT> mvi(
    initialUiState: STATE,
): UnidirectionalViewModel<STATE, EVENT, EFFECT> = MviContractDelegate(initialUiState)