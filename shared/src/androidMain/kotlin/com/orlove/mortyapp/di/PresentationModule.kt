package com.orlove.mortyapp.di

import com.orlove.mortyapp.ui.screens.details.CharacterDetailViewModel
import com.orlove.mortyapp.ui.screens.list.CharacterListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun presentationModulePlatform(): Module = module {
    viewModel { CharacterListViewModel(get()) }
    viewModel { CharacterDetailViewModel(get(), get()) }
}