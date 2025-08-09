package com.orlove.mortyapp.data.repository

import com.orlove.mortyapp.data.local.db.dao.CharacterDao
import com.orlove.mortyapp.ui.model.mapper.toDomain
import com.orlove.mortyapp.ui.model.mapper.toEntity
import com.orlove.mortyapp.data.remote.api.RichAndMortyApiDataSource
import com.orlove.mortyapp.domain.model.RickAndMortyCharacter
import com.orlove.mortyapp.domain.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val apiService: RichAndMortyApiDataSource,
    private val characterDao: CharacterDao
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<RickAndMortyCharacter>> {
        return try {
            val response = apiService.getCharacters(page)
            response.fold(
                onSuccess = { dto ->
                    val characters = dto.results?.map { it.toDomain() }.orEmpty()
                    val entities = dto.results?.map { it.toEntity(page) }.orEmpty()
                    if (entities.isNotEmpty()) {
                        characterDao.insert(entities)
                    }
                    Result.success(characters)
                },
                onFailure = { error ->
                    val cachedEntities = if (page == 1) {
                        characterDao.getAllCharacters()
                    } else {
                        emptyList()
                    }
                    if (cachedEntities.isNotEmpty()) {
                        Result.success(cachedEntities.map { it.toDomain() })
                    } else {
                        Result.failure(error)
                    }
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchCharacters(query: String): Result<List<RickAndMortyCharacter>> {
        return try {
            val entities = characterDao.searchCharacters(query)
            Result.success(entities.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCharacterById(id: Int): Result<RickAndMortyCharacter?> {
        return try {
            val entity = characterDao.getCharacterById(id)
            Result.success(entity?.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}