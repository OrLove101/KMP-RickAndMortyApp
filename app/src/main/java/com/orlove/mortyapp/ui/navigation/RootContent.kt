package com.orlove.mortyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.orlove.mortyapp.ui.screens.details.CharacterDetailScreen
import com.orlove.mortyapp.ui.screens.list.CharacterListScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(slide())
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