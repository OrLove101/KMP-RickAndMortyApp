package com.orlove.mortyapp.ui.navigation

import com.arkivanov.decompose.ComponentContext

interface CharacterDetailComponent {
    val characterId: Int
    fun navigateBack()
}

class DefaultCharacterDetailComponent(
    componentContext: ComponentContext,
    override val characterId: Int,
    private val onNavigateBack: () -> Unit,
) : CharacterDetailComponent, ComponentContext by componentContext {

    override fun navigateBack() {
        onNavigateBack()
    }
}