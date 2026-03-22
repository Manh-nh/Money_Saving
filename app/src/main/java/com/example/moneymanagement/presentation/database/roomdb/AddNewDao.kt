package com.example.moneymanagement.presentation.database.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AddNewDao {
    @Query("SELECT * FROM AddNewEntity ORDER BY date DESC")
    fun getAll(): LiveData<List<AddNewEntity>>

    @Insert
    suspend fun insertExpend(vararg expend: AddNewEntity)

    @Update
    fun updateExpend(updateExpend: AddNewEntity)

    @Delete
    fun deleteExpend(deleteExpend: AddNewEntity)

    @Query("DELETE FROM AddNewEntity Where id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM AddNewEntity WHERE id = :id")
    suspend fun getById(id: Int): AddNewEntity?

    @Query("SELECT * FROM AddNewEntity")
    suspend fun getAllSync(): List<AddNewEntity>
}