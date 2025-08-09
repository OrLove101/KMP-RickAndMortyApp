package com.orlove.mortyapp.di

val appModule = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    domainModule,
    presentationModule
)