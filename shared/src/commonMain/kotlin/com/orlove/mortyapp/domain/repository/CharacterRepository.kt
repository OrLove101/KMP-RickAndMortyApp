package com.orlove.mortyapp.domain.repository

import com.orlove.mortyapp.domain.model.RickAndMortyCharacter
import com.orlove.mortyapp.utils.constants.CharacterGender
import com.orlove.mortyapp.utils.constants.CharacterStatus

interface CharacterRepository {
    suspend fun getCharacters(
        page: Int,
        name: String,
        gender: CharacterGender?,
        status: CharacterStatus?
    ): Result<List<RickAndMortyCharacter>>

    suspend fun getCharacterById(id: Int): Result<RickAndMortyCharacter?>
}