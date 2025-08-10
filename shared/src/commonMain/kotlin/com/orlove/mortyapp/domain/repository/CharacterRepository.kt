package com.orlove.mortyapp.domain.repository

import com.orlove.mortyapp.domain.model.RickAndMortyCharacter

interface CharacterRepository {
    suspend fun getCharacters(page: Int): Result<List<RickAndMortyCharacter>>
    suspend fun searchCharacters(query: String): Result<List<RickAndMortyCharacter>>
    suspend fun getCharacterById(id: Int): Result<RickAndMortyCharacter?>
}