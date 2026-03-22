package com.example.moneymanagement.presentation.view.activity.transactionsactivity

import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.roomdb.AddNewDao
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var dao : AddNewDao


    fun setAppDatabase(appDatabase: AppDatabase){
        this.appDatabase = appDatabase
        dao = appDatabase.addNewDao()
    }

    fun delete(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val transaction = dao.getById(id)
            if (transaction != null) {
                val budget = appDatabase.addBudget().getBudgetById(transaction.imgBudget)
                if (budget != null) {
                    val reversedMoney = when (transaction.type) {
                        "expend" -> budget.moneyBudget + transaction.amount
                        "income" -> budget.moneyBudget - transaction.amount
                        "loan" -> if (transaction.nameTypeCategory == "Loan") {
                            budget.moneyBudget - transaction.amount
                        } else {
                            budget.moneyBudget + transaction.amount
                        }
                        else -> budget.moneyBudget
                    }
                    appDatabase.addBudget().updateMoney(budget.id, reversedMoney)
                }
            }
            dao.deleteById(id)
        }
    }
}