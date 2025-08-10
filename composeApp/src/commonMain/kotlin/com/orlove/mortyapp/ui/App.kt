package com.orlove.mortyapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.orlove.mortyapp.di.appModule
import com.orlove.mortyapp.ui.navigation.RootComponent
import com.orlove.mortyapp.ui.navigation.RootContent
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Composable
fun App(
    appDeclaration: KoinAppDeclaration = {},
    rootComponent: RootComponent
) {
    KoinApplication(
        application = {
            appDeclaration()
            modules(
                appModule
            )
        }
    ) {
        MaterialTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                RootContent(
                    component = rootComponent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}