package com.orlove.mortyapp.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class CharacterList(val component: CharacterListComponent) : Child()
        class CharacterDetail(val component: CharacterDetailComponent) : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.CharacterList,
            handleBackButton = true,
            childFactory = ::child,
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.CharacterList -> RootComponent.Child.CharacterList(
                DefaultCharacterListComponent(
                    componentContext = componentContext,
                    onNavigateToDetail = { characterId ->
                        navigation.push(Config.CharacterDetail(characterId = characterId))
                    }
                )
            )
            is Config.CharacterDetail -> RootComponent.Child.CharacterDetail(
                DefaultCharacterDetailComponent(
                    componentContext = componentContext,
                    characterId = config.characterId,
                    onNavigateBack = navigation::pop
                )
            )
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object CharacterList : Config

        @Serializable
        data class CharacterDetail(val characterId: Int) : Config
    }
}
