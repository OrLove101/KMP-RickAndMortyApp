package com.orlove.mortyapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.orlove.mortyapp.data.local.db.AppDatabase.Companion.DATABASE_VERSION
import com.orlove.mortyapp.data.local.db.converter.StringListConverter
import com.orlove.mortyapp.data.local.db.dao.CharacterDao
import com.orlove.mortyapp.data.local.db.entity.CharacterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

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

        fun getDatabase(builder: Builder<AppDatabase>): AppDatabase {
            return builder
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}