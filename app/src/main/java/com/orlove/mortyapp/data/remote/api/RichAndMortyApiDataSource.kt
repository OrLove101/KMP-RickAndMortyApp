package com.orlove.mortyapp.data.remote.api

import com.orlove.mortyapp.data.remote.dto.CharacterResponseDto

interface RichAndMortyApiDataSource {
    suspend fun getCharacters(page: Int): Result<CharacterResponseDto>
}
