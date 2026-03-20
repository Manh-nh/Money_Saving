package com.example.moneymanagement.presentation.database.model

data class TransactionParent(
    val date: String,
    val child: List<TransactionChild>
)


data class TransactionChild(

    val id : Int,
    val type: String,
    val imgCategory: Int,
    val nameCategory: String?,
    val note: String,
    val time: String,
    val expendPrice: Int,
    val nameBudget: String,
    val imgBudget : Int,
)