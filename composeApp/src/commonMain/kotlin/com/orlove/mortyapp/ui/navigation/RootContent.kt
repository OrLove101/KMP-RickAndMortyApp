package com.orlove.mortyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.orlove.mortyapp.ui.screens.details.CharacterDetailScreen
import com.orlove.mortyapp.ui.screens.list.CharacterListScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.CharacterList -> CharacterListScreen(
                onNavigateToDetail = { characterId ->
                    child.component.navigateToDetail(characterId)
                }
            )

            is RootComponent.Child.CharacterDetail -> CharacterDetailScreen(
                onNavigateBack = child.component::navigateBack,
                viewModel = koinViewModel(
                    parameters = { parametersOf(child.component.characterId) },
                    key = "character_${child.component.characterId}",
                )
            )
        }
    }
}