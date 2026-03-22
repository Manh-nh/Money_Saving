package com.example.moneymanagement.presentation.view.adapter

import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity


interface OnClickListenerUpdateMoney {

    fun getJar (id: Int, money : Int, jarName : String)

    fun updateMoney (id : Int, moneyJar : Int)

    fun onItemClick (budget : BudgetEntity)
    
}