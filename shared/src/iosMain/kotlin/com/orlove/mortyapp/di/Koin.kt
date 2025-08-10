package com.orlove.mortyapp.di

import org.koin.core.context.startKoin

fun initKoinIos() {
    startKoin {
        modules(
            appModule
        )
    }
}