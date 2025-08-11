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
        WHERE 
          name 
        LIKE 
          '%' || :name || '%' 
        ORDER BY 
          page, id
        """
    )
    abstract suspend fun getAllCharacters(name: String): List<CharacterEntity>

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
        DELETE FROM
          characters
        """
    )
    abstract override suspend fun clear()
}