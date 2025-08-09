package com.orlove.mortyapp.ui.screens.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orlove.mortyapp.R
import com.orlove.mortyapp.domain.usecase.GetCharactersUseCase
import com.orlove.mortyapp.domain.usecase.SearchCharactersUseCase
import com.orlove.mortyapp.ui.model.mapper.toUi
import com.orlove.mortyapp.util.DefaultPaginator
import com.orlove.mortyapp.util.UnidirectionalViewModel
import com.orlove.mortyapp.util.mvi
import kotlinx.coroutines.*

class CharacterListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase,
    private val applicationContext: Context
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
            getCharactersUseCase(page)
        },
        getNextKey = { items ->
            if (items.isEmpty()) state.value.page else state.value.page + 1
        },
        onError = { throwable ->
            updateUiState {
                copy(
                    error = throwable?.message
                        ?: applicationContext.getString(R.string.unknown_error_occurred),
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
                    error = null,
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
            if (!state.value.endReached && !state.value.isLoadingMore) {
                paginator.loadNextItems()
            }
        }
    }

    private fun searchCharacters(query: String) {
        updateUiState { copy(searchQuery = query, isSearching = true) }

        if (query.isBlank()) {
            clearSearch()
            return
        }

        viewModelScope.launch {
            try {
                val result = searchCharactersUseCase(query)
                result.fold(
                    onSuccess = { characters ->
                        updateUiState {
                            copy(
                                characters = characters.map { it.toUi() },
                                isSearching = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { error ->
                        updateUiState {
                            copy(
                                error = error.message
                                    ?: applicationContext.getString(R.string.search_failed),
                                isSearching = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                updateUiState {
                    copy(
                        error = e.message
                            ?: applicationContext.getString(R.string.search_failed),
                        isSearching = false
                    )
                }
            }
        }
    }

    private fun clearSearch() {
        updateUiState { copy(searchQuery = "", isSearching = false) }
        loadCharacters()
    }

    private fun retryLoading() {
        updateUiState { copy(error = null) }
        loadCharacters()
    }
}