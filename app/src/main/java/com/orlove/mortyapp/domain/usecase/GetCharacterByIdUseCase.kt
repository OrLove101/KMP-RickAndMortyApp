package com.orlove.mortyapp.domain.usecase

import com.orlove.mortyapp.domain.model.RickAndMortyCharacter
import com.orlove.mortyapp.domain.repository.CharacterRepository

class GetCharacterByIdUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(id: Int): Result<RickAndMortyCharacter?> {
        return repository.getCharacterById(id)
    }
}