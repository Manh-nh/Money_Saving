package com.example.moneymanagement.presentation.database.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AddNewEntity :: class, MoneyBudgetEntity::class, BudgetEntity::class, ChatMessageEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun addNewDao() : AddNewDao

    abstract fun setMoney() : AddMoneyBudgetDao

    abstract fun addBudget() : AddBudgetDetailDao
    
    abstract fun chatMessageDao() : ChatMessageDao
}