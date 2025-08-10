package com.orlove.mortyapp.di

import com.orlove.mortyapp.ui.screens.details.CharacterDetailViewModel
import com.orlove.mortyapp.ui.screens.list.CharacterListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual fun presentationModulePlatform(): Module = module {
    viewModel { CharacterListViewModel(get(), get()) }
    viewModel { CharacterDetailViewModel(get(), get()) }
}