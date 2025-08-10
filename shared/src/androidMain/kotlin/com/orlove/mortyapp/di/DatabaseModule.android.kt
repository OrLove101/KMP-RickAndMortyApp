package com.orlove.mortyapp.di

import com.orlove.mortyapp.data.getDatabaseBuilder
import com.orlove.mortyapp.data.local.db.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModulePlatform(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        AppDatabase.getDatabase(builder)
    }
}