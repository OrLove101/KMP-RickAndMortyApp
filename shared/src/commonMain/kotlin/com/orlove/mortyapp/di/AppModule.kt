package com.orlove.mortyapp.di

val appModule = listOf(
    networkModule,
    databaseModuleCommon,
    databaseModulePlatform(),
    repositoryModule,
    domainModule,
    presentationModulePlatform()
)