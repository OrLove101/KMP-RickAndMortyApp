package com.orlove.mortyapp.ui.screens.details

import com.orlove.mortyapp.ui.model.RickAndMortyCharacterUi

interface CharacterDetailContract {
    data class State(
        val character: RickAndMortyCharacterUi? = null,
        val isLoading: Boolean = false,
        val error: Boolean = false
    )

    sealed interface Event {
        data object NavigateBack : Event
        data object RetryLoading : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data class ShowError(val message: String) : Effect
    }
}