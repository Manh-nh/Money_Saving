package com.example.moneymanagement.presentation.database.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddNewEntity (

    @PrimaryKey(autoGenerate = true) val id: Int,
    val type: String,
    val amount: Int,
    val nameTypeCategory: String,
    val imgTypeCategory: Int,
    var imgBudget: Int,
    var nameBudget: String,
    val note: String?,
    val date: String,
    val time: String
)


