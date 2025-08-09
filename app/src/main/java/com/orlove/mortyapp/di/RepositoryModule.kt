package com.orlove.mortyapp.di

import com.orlove.mortyapp.data.repository.CharacterRepositoryImpl
import com.orlove.mortyapp.domain.repository.CharacterRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
}