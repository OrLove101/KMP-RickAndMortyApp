package com.orlove.mortyapp.domain.usecase

import com.orlove.mortyapp.domain.model.RickAndMortyCharacter
import com.orlove.mortyapp.domain.repository.CharacterRepository

class SearchCharactersUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(query: String): Result<List<RickAndMortyCharacter>> {
        return repository.searchCharacters(query)
    }
}