package com.orlove.mortyapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.orlove.mortyapp.data.local.db.AppDatabase.Companion.DATABASE_VERSION
import com.orlove.mortyapp.data.local.db.converter.StringListConverter
import com.orlove.mortyapp.data.local.db.dao.CharacterDao
import com.orlove.mortyapp.data.local.db.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        const val DATABASE_VERSION = 1

        fun initDatabase(
            context: Context
        ): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_db"
            ).build()
        }
    }
}