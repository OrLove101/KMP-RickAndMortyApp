package com.orlove.mortyapp.di

import com.orlove.mortyapp.data.local.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { AppDatabase.initDatabase(androidContext()) }
    single { get<AppDatabase>().characterDao() }
}