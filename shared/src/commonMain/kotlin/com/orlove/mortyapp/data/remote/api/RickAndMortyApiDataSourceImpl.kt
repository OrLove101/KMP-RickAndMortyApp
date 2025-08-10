package com.orlove.mortyapp.data.remote.api

import com.orlove.mortyapp.data.remote.dto.CharacterResponseDto
import com.orlove.mortyapp.utils.constants.api.CHARACTER_ENDPOINT
import com.orlove.mortyapp.utils.constants.api.CHARACTER_PAGE_PARAMETER
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.call.*

class RickAndMortyApiDataSourceImpl(private val client: HttpClient) : RichAndMortyApiDataSource {
    override suspend fun getCharacters(page: Int): Result<CharacterResponseDto> {
        return try {
            val response = client.get(CHARACTER_ENDPOINT) {
                parameter(CHARACTER_PAGE_PARAMETER, page)
            }.body<CharacterResponseDto>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}