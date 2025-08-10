package com.orlove.mortyapp.di

import com.orlove.mortyapp.data.local.db.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModuleCommon = module {
    single { get<AppDatabase>().characterDao() }
}

expect fun databaseModulePlatform(): Module