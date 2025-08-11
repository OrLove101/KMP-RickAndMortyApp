package com.orlove.mortyapp.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orlove.mortyapp.domain.usecase.GetCharactersUseCase
import com.orlove.mortyapp.ui.model.mapper.toUi
import com.orlove.mortyapp.utils.DefaultPaginator
import com.orlove.mortyapp.utils.UnidirectionalViewModel
import com.orlove.mortyapp.utils.mvi
import kotlinx.coroutines.*

class CharacterListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel(),
    UnidirectionalViewModel<CharacterListContract.State, CharacterListContract.Event, CharacterListContract.Effect> by mvi(
        CharacterListContract.State()
    ) {

    private val paginator = DefaultPaginator(
        initialKey = 1,
        onLoadUpdated = { isLoading ->
            if (state.value.page == 1) {
                updateUiState { copy(isLoading = isLoading) }
            } else {
                updateUiState {
                    copy(
                        isLoadingMore = isLoading,
                        isLoading = false
                    )
                }
            }
        },
        onRequest = { page ->
            getCharactersUseCase(page = page, name = state.value.searchQuery)
        },
        getNextKey = { items ->
            if (items.isEmpty()) state.value.page else state.value.page + 1
        },
        onError = { _ ->
            updateUiState {
                copy(
                    error = true,
                    isLoading = false,
                    isLoadingMore = false
                )
            }
        },
        onSuccess = { items, newKey ->
            updateUiState {
                copy(
                    characters = if (page == 1) {
                        items.map { it.toUi() }
                    } else {
                        characters + items.map { it.toUi() }
                    },
                    page = newKey,
                    endReached = items.isEmpty(),
                    error = false,
                )
            }
        }
    )

    init {
        loadCharacters()
    }

    override fun event(event: CharacterListContract.Event) {
        when (event) {
            CharacterListContract.Event.LoadCharacters -> loadCharacters()
            CharacterListContract.Event.LoadMore -> loadMore()
            is CharacterListContract.Event.SearchCharacters -> searchCharacters(event.query)
            CharacterListContract.Event.ClearSearch -> clearSearch()
            is CharacterListContract.Event.OnCharacterClick -> {
                viewModelScope
                    .emitSideEffect(
                        CharacterListContract.Effect.NavigateToDetail(event.character.id)
                    )
            }

            CharacterListContract.Event.RetryLoading -> retryLoading()
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            paginator.reset()
            updateUiState { copy(page = 1, characters = emptyList(), endReached = false) }
            paginator.loadNextItems()
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            if (
                !state.value.endReached
                && !state.value.isLoadingMore
            ) {
                paginator.loadNextItems()
            }
        }
    }

    private fun searchCharacters(query: String) {
        updateUiState { copy(searchQuery = query) }
        loadCharacters()
    }

    private fun clearSearch() {
        updateUiState { copy(searchQuery = "") }
        loadCharacters()
    }

    private fun retryLoading() {
        updateUiState { copy(error = false) }
        loadCharacters()
    }
}