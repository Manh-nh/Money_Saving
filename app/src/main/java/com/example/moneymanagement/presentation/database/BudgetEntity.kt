package com.example.moneymanagement.presentation.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BudgetEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val nameBudget : String,
    val moneyBudget : Int = 0,
    val imgBudget: Int,
)