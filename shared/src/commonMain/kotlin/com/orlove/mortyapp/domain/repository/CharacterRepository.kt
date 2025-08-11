package com.orlove.mortyapp.domain.repository

import com.orlove.mortyapp.domain.model.RickAndMortyCharacter

interface CharacterRepository {
    suspend fun getCharacters(page: Int, name: String): Result<List<RickAndMortyCharacter>>
    suspend fun getCharacterById(id: Int): Result<RickAndMortyCharacter?>
}