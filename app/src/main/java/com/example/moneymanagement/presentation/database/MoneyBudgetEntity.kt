package com.example.moneymanagement.presentation.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoneyBudgetEntity(

    @PrimaryKey(autoGenerate = true)
    val id : Int = 1,

    val moneyBudget : Int,

)