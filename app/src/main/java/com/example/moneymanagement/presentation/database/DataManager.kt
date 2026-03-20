package com.example.moneymanagement.presentation.database

import android.content.Context
import androidx.room.Room

object DataManager {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDataBase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "data.db"
            ).build()
            INSTANCE = instance
            instance
        }
    }

}