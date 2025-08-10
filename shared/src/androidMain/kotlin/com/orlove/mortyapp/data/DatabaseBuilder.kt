package com.orlove.mortyapp.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.orlove.mortyapp.data.local.db.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("mortyapp.db")

    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}