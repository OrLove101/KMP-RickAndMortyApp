package com.orlove.mortyapp.di

import com.orlove.mortyapp.ui.screens.details.CharacterDetailViewModel
import com.orlove.mortyapp.ui.screens.list.CharacterListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { CharacterListViewModel(get(), get(), androidContext()) }
    viewModel { CharacterDetailViewModel(get(), get(), androidContext()) }
}