package com.orlove.mortyapp.ui.screens.list

import com.orlove.mortyapp.ui.model.RickAndMortyCharacterUi
import com.orlove.mortyapp.utils.constants.CharacterGender
import com.orlove.mortyapp.utils.constants.CharacterStatus

interface CharacterListContract {
    data class State(
        val characters: List<RickAndMortyCharacterUi> = emptyList(),
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val error: Boolean = false,
        val searchQuery: String = "",
        val page: Int = 1,
        val endReached: Boolean = false,
        val selectedStatus: CharacterStatus? = null,
        val selectedGender: CharacterGender? = null,
    )

    sealed interface Event {
        data object LoadCharacters : Event
        data object LoadMore : Event
        data class SearchCharacters(val query: String) : Event
        data class StatusChanged(val status: CharacterStatus) : Event
        data class GenderChanged(val gender: CharacterGender) : Event
        data object ClearSearch : Event
        data class OnCharacterClick(val character: RickAndMortyCharacterUi) : Event
        data object RetryLoading : Event
    }

    sealed interface Effect {
        data class NavigateToDetail(val characterId: Int) : Effect
        data class ShowError(val message: String) : Effect
    }
}