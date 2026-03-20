package com.example.moneymanagement.presentation.view.addnewactivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.AppDatabase
import com.example.moneymanagement.presentation.database.AddNewEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewViewModel : ViewModel() {

    private var data = MutableLiveData<List<AddNewEntity>>()
    private var type = MutableLiveData<String>()
    val typeAddNew: LiveData<String> get() = type
    private lateinit var appDatabase: AppDatabase

    fun setDataList(list: List<AddNewEntity>) {
        data.value = list
    }

    fun getDataList(): List<AddNewEntity>? = data.value

    fun setAppDataBase(database: AppDatabase) {
        appDatabase = database
    }

    fun setType(value: String) {
        type.value = value
    }

    fun insertExpendEntity(
        amountExpend: Int,
        type: String,
        nameTypeCategory: String,
        imgTypeCategory: Int,
        nameBudget: String,
        note: String?,
        dateExpend: String,
        timeExpend: String,
        imgBudget: Int,

        ) {
        val entity = AddNewEntity(
            id = 0,
            type = type,
            amount = amountExpend,
            nameTypeCategory = nameTypeCategory,
            imgTypeCategory = imgTypeCategory,
            imgBudget = imgBudget,
            nameBudget = nameBudget,
            note = note,
            date = dateExpend,
            time = timeExpend
        )
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.addNewDao().insertExpend(entity)

        }
    }

    fun updateMoneyJar(transactions: List<AddNewEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            transactions.forEach { data ->
                val budget = appDatabase.addBudget().getBudgetById(data.imgBudget)
                if (budget != null) {
                    val newMoney = when (data.type) {
                        "expend" -> budget.moneyBudget - data.amount
                        "income" -> budget.moneyBudget + data.amount
                        "loan" -> if (data.nameTypeCategory == "Loan") {
                            budget.moneyBudget + data.amount
                        } else {
                            budget.moneyBudget - data.amount
                        }

                        else -> budget.moneyBudget
                    }
                    appDatabase.addBudget().updateMoney(budget.id, newMoney)
                }
            }
        }
    }

    // lỗi chưa update lại khi không còn bản ghi nào

}
