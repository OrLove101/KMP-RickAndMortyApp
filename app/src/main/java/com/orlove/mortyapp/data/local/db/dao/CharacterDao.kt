package com.orlove.mortyapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.orlove.mortyapp.data.local.db.entity.CharacterEntity

@Dao
abstract class CharacterDao : BaseDao<CharacterEntity>() {

    @Query(
        """
        SELECT 
          * 
        FROM 
          characters 
        ORDER BY 
          page, id
        """
    )
    abstract suspend fun getAllCharacters(): List<CharacterEntity>

    @Query(
        """
        SELECT
          * 
        FROM 
          characters 
        WHERE 
          id = :id
        """
    )
    abstract suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query(
        """
        SELECT
          * 
        FROM 
          characters 
        WHERE 
          name 
        LIKE 
          '%' || :query || '%' 
        ORDER BY 
          page, id
        """
    )
    abstract suspend fun searchCharacters(query: String): List<CharacterEntity>

    @Query(
        """
        DELETE FROM
          characters
        """
    )
    abstract override suspend fun clear()
}