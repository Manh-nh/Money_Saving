package com.example.moneymanagement.presentation.database.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AddBudgetDetailDao {

    @Query("SELECT * FROM BudgetEntity")
    fun getBudgetDetail(): LiveData<List<BudgetEntity>>

//    @Query("SELECT * FROM BudgetEntity")
//    fun getImgBudgetById(): BudgetEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudgetDetail(budgetEntity: BudgetEntity)

    @Query("UPDATE BudgetEntity SET moneyBudget = :newMoneyBudget WHERE id = :id ")
    suspend fun updateMoney(id: Int, newMoneyBudget: Int)

    @Delete
    suspend fun deleteBudget(budgetEntity: BudgetEntity)

    @Query("SELECT COUNT(*) FROM BUDGETENTITY")
    suspend fun countJar(): Int

    @Query("SELECT * FROM BudgetEntity WHERE imgBudget = :imgBudgetId LIMIT 1")
    suspend fun getBudgetById(imgBudgetId: Int): BudgetEntity?

    @Query("SELECT * FROM BudgetEntity")
    suspend fun getBudgetDetailSync(): List<BudgetEntity>
}