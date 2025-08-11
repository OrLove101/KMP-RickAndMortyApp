package com.orlove.mortyapp.domain.usecase

import com.orlove.mortyapp.domain.model.RickAndMortyCharacter
import com.orlove.mortyapp.domain.repository.CharacterRepository
import com.orlove.mortyapp.utils.constants.CharacterGender
import com.orlove.mortyapp.utils.constants.CharacterStatus

class GetCharactersUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(
        page: Int,
        name: String,
        gender: CharacterGender?,
        status: CharacterStatus?
    ): Result<List<RickAndMortyCharacter>> {
        return repository.getCharacters(page = page, name = name, gender = gender, status = status)
    }
}