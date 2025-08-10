package com.orlove.mortyapp.di

import com.orlove.mortyapp.ui.screens.details.CharacterDetailViewModel
import com.orlove.mortyapp.ui.screens.list.CharacterListViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun presentationModulePlatform(): Module = module {
    factory { CharacterListViewModel(get(), get()) }
    factory { CharacterDetailViewModel(get(), get()) }
}