package com.orlove.mortyapp.ui.navigation

import com.arkivanov.decompose.ComponentContext

interface CharacterListComponent {
    fun navigateToDetail(characterId: Int)
}

class DefaultCharacterListComponent(
    componentContext: ComponentContext,
    private val onNavigateToDetail: (Int) -> Unit,
) : CharacterListComponent, ComponentContext by componentContext {

    override fun navigateToDetail(characterId: Int) {
        onNavigateToDetail(characterId)
    }
}