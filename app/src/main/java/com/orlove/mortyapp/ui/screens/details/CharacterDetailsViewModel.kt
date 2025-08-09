package com.orlove.mortyapp.ui.screens.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orlove.mortyapp.R
import com.orlove.mortyapp.domain.usecase.GetCharacterByIdUseCase
import com.orlove.mortyapp.ui.model.mapper.toUi
import com.orlove.mortyapp.util.UnidirectionalViewModel
import com.orlove.mortyapp.util.mvi
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    characterId: Int,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val applicationContext: Context
) : ViewModel(),
    UnidirectionalViewModel<CharacterDetailContract.State, CharacterDetailContract.Event, CharacterDetailContract.Effect> by mvi(
        CharacterDetailContract.State()
    ) {

    init {
        loadCharacter(characterId)
    }

    override fun event(event: CharacterDetailContract.Event) {
        when (event) {
            CharacterDetailContract.Event.NavigateBack -> {
                viewModelScope.emitSideEffect(CharacterDetailContract.Effect.NavigateBack)
            }

            CharacterDetailContract.Event.RetryLoading -> {
                state.value.character?.let {
                    loadCharacter(it.id)
                }
            }
        }
    }

    private fun loadCharacter(id: Int) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true, error = null) }

            try {
                val result = getCharacterByIdUseCase(id)
                result.fold(
                    onSuccess = { character ->
                        updateUiState {
                            copy(
                                character = character?.toUi(),
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { error ->
                        updateUiState {
                            copy(
                                error = error.message
                                    ?: applicationContext
                                        .getString(R.string.failed_to_load_character),
                                isLoading = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                updateUiState {
                    copy(
                        error = e.message ?: applicationContext
                            .getString(R.string.failed_to_load_character),
                        isLoading = false
                    )
                }
            }
        }
    }
}