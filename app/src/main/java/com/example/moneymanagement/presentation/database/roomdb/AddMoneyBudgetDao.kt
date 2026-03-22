package com.example.moneymanagement.presentation.database.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AddMoneyBudgetDao {

    @Query("SELECT * FROM MoneyBudgetEntity WHERE id = 1")
    fun getMoney() : LiveData<MoneyBudgetEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(moneyBudgetEntity: MoneyBudgetEntity)

    @Query("SELECT * FROM MoneyBudgetEntity WHERE id = 1")
    suspend fun getMoneyNow() :  MoneyBudgetEntity


}