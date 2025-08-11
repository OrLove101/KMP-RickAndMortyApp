package com.orlove.mortyapp.domain.usecase

import com.orlove.mortyapp.domain.model.RickAndMortyCharacter
import com.orlove.mortyapp.domain.repository.CharacterRepository

class GetCharactersUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(page: Int, name: String): Result<List<RickAndMortyCharacter>> {
        return repository.getCharacters(page = page, name = name)
    }
}