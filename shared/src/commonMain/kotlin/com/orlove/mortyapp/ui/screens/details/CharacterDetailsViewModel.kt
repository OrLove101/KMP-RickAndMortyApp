package com.orlove.mortyapp.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orlove.mortyapp.domain.usecase.GetCharacterByIdUseCase
import com.orlove.mortyapp.ui.model.mapper.toUi
import com.orlove.mortyapp.utils.UnidirectionalViewModel
import com.orlove.mortyapp.utils.mvi
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    characterId: Int,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
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
            updateUiState { copy(isLoading = true, error = false) }

            try {
                val result = getCharacterByIdUseCase(id)
                result.fold(
                    onSuccess = { character ->
                        updateUiState {
                            copy(
                                character = character?.toUi(),
                                isLoading = false,
                                error = false
                            )
                        }
                    },
                    onFailure = { _ ->
                        updateUiState {
                            copy(
                                error = true,
                                isLoading = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                updateUiState {
                    copy(
                        error = true,
                        isLoading = false
                    )
                }
            }
        }
    }
}