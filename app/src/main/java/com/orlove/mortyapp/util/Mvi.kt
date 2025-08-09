package com.orlove.mortyapp.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
inline fun <reified STATE, EVENT, EFFECT> use(
    viewModel: UnidirectionalViewModel<STATE, EVENT, EFFECT>,
): StateDispatchEffect<STATE, EVENT, EFFECT> {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val dispatch: (EVENT) -> Unit = remember {
        { event ->
            viewModel.event(event)
        }
    }

    return StateDispatchEffect(
        state = state,
        effectFlow = viewModel.effect,
        dispatch = dispatch,
    )
}

data class StateDispatchEffect<STATE, EVENT, EFFECT>(
    val state: STATE,
    val dispatch: (EVENT) -> Unit,
    val effectFlow: Flow<EFFECT>,
)

@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectInLaunchedEffect(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = Dispatchers.Main.immediate,
    function: suspend (value: T) -> Unit,
) {
    val effectFlow = this
    LaunchedEffect(key1 = effectFlow, key2 = function, key3 = lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                effectFlow.collect(function)
            } else {
                withContext(context) {
                    effectFlow.collect(function)
                }
            }
        }
    }
}

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