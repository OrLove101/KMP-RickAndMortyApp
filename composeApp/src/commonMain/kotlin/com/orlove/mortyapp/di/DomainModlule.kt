package com.orlove.mortyapp.di

import com.orlove.mortyapp.domain.usecase.GetCharacterByIdUseCase
import com.orlove.mortyapp.domain.usecase.GetCharactersUseCase
import com.orlove.mortyapp.domain.usecase.SearchCharactersUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetCharactersUseCase(get()) }
    single { SearchCharactersUseCase(get()) }
    single { GetCharacterByIdUseCase(get()) }
}