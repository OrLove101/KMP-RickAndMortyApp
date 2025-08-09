package com.orlove.mortyapp.di

import com.orlove.mortyapp.data.remote.api.RichAndMortyApiDataSource
import com.orlove.mortyapp.data.remote.api.RickAndMortyApiDataSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
    single<RichAndMortyApiDataSource> { RickAndMortyApiDataSourceImpl(get()) }
}