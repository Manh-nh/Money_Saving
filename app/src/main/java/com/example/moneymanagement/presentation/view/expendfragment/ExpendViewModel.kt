package com.example.moneymanagement.presentation.view.expendfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.AppDatabase
import com.example.moneymanagement.presentation.database.AddNewDao
import com.example.moneymanagement.presentation.database.AddNewEntity
import com.example.moneymanagement.presentation.database.model.TransactionChild
import com.example.moneymanagement.presentation.database.model.TransactionParent
import kotlin.collections.component1
import kotlin.collections.component2

class ExpendViewModel : ViewModel() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var dao: AddNewDao
    val expendList: LiveData<List<AddNewEntity>>
        get() = dao.getAll()

    fun setAppDataBase(database: AppDatabase) {
        appDatabase = database
        dao = appDatabase.addNewDao()
    }

    fun initData(list: List<AddNewEntity>): List<TransactionParent> {
        val parent = list.groupBy { it.date }
        return parent.map { (date, items) ->
            val children = items.map {
                TransactionChild(
                    id = it.id,
                    type = it.type,
                    imgCategory = it.imgTypeCategory,
                    nameCategory = it.nameTypeCategory,
                    note = it.note ?: "",
                    time = it.time,
                    expendPrice = it.amount,
                    nameBudget = it.nameBudget,
                    imgBudget = it.imgBudget
                )
            }
            TransactionParent(date, children)
        }
    }
}
