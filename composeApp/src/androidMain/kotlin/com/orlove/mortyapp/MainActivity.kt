package com.orlove.mortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.orlove.mortyapp.ui.App
import com.orlove.mortyapp.ui.navigation.DefaultRootComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val rootComponent = DefaultRootComponent(defaultComponentContext())
        setContent {
            App(
                appDeclaration = {
                    androidContext(this@MainActivity)
                    androidLogger()
                },
                rootComponent = rootComponent,
            )
        }
    }
}