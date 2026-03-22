package com.example.moneymanagement.presentation.view.activity.addnewactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
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
            
            // Sync with Budget Jar
            val budget = appDatabase.addBudget().getBudgetById(imgBudget)
            if (budget != null) {
                val newMoney = when (type) {
                    "expend" -> budget.moneyBudget - amountExpend
                    "income" -> budget.moneyBudget + amountExpend
                    "loan" -> if (nameTypeCategory == "Loan") {
                        budget.moneyBudget + amountExpend
                    } else {
                        budget.moneyBudget - amountExpend
                    }
                    else -> budget.moneyBudget
                }
                appDatabase.addBudget().updateMoney(budget.id, newMoney)
            }
        }
    }
}
