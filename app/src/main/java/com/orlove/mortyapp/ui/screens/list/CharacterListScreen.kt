package com.orlove.mortyapp.ui.screens.list

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.orlove.mortyapp.R
import com.orlove.mortyapp.ui.components.EmptyState
import com.orlove.mortyapp.ui.components.ErrorState
import com.orlove.mortyapp.ui.components.SearchTextField
import com.orlove.mortyapp.ui.screens.details.components.GenderDropdownMenu
import com.orlove.mortyapp.ui.screens.details.components.StatusDropdownMenu
import com.orlove.mortyapp.ui.screens.list.components.CharacterItem
import com.orlove.mortyapp.ui.theme.Spacing
import com.orlove.mortyapp.util.collectInLaunchedEffect
import com.orlove.mortyapp.util.components.ProgressLoader
import com.orlove.mortyapp.util.use
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterListScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: CharacterListViewModel = koinViewModel()
) {
    val (state, dispatch, effectFlow) = use(viewModel)
    val context = LocalContext.current

    effectFlow.collectInLaunchedEffect { effect ->
        when (effect) {
            is CharacterListContract.Effect.NavigateToDetail -> {
                onNavigateToDetail(effect.characterId)
            }

            is CharacterListContract.Effect.ShowError -> {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    CharacterListContent(
        state = state,
        onEvent = dispatch
    )
}

@Composable
private fun CharacterListContent(
    state: CharacterListContract.State,
    onEvent: (CharacterListContract.Event) -> Unit
) {

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.large)
        ) {
            Text(
                text = stringResource(R.string.characters),
                style = MaterialTheme.typography.headlineSmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusDropdownMenu(
                    selectedStatus = state.selectedStatus,
                    onStatusSelected = { status ->
                        onEvent(CharacterListContract.Event.StatusChanged(status = status))
                    }
                )
                Spacer(
                    modifier = Modifier.width(width = 16.dp)
                )
                GenderDropdownMenu(
                    selectedGender = state.selectedGender,
                    onGenderSelected = { gender ->
                        onEvent(CharacterListContract.Event.GenderChanged(gender = gender))
                    }
                )
                Spacer(
                    modifier = Modifier.width(width = 16.dp)
                )
                Text(
                    modifier = Modifier.clickable {
                        onEvent(CharacterListContract.Event.ClearFilters)
                    },
                    text = stringResource(R.string.clear_filters),
                )
            }
        }
        SearchTextField(
            query = state.searchQuery,
            onQueryChange = { query ->
                onEvent(CharacterListContract.Event.SearchCharacters(query))
            },
            onClearClick = {
                onEvent(CharacterListContract.Event.ClearSearch)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = Spacing.large)
        )

        when {
            state.isLoading -> {
                ProgressLoader()
            }

            state.error -> {
                ErrorState(
                    error = stringResource(R.string.oops_something_went_wrong),
                    onRetry = { onEvent(CharacterListContract.Event.RetryLoading) }
                )
            }

            state.characters.isEmpty() -> {
                EmptyState()
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(all = Spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        items = state.characters,
                        key = { _, item -> item.id }
                    ) { index, character ->
                        if (
                            index >= state.characters.size - 1 && !state.endReached
                            && !state.isLoadingMore
                        ) {
                            onEvent(CharacterListContract.Event.LoadMore)
                        }
                        CharacterItem(
                            character = character,
                            onClick = {
                                onEvent(
                                    CharacterListContract.Event.OnCharacterClick(
                                        character
                                    )
                                )
                            },
                            modifier = Modifier.animateItem()
                        )
                    }

                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}