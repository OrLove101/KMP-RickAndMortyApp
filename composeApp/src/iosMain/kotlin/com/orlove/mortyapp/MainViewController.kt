package com.orlove.mortyapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import com.orlove.mortyapp.ui.App
import com.orlove.mortyapp.ui.navigation.DefaultRootComponent

fun MainViewController() = ComposeUIViewController {
    val rootComponent = remember {
        DefaultRootComponent(
            DefaultComponentContext(
                ApplicationLifecycle()
            )
        )
    }
    App(
        rootComponent = rootComponent
    )
}