package com.example.moneymanagement.presentation.view.fragment.loanfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.roomdb.AddNewDao
import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import com.example.moneymanagement.presentation.database.model.TransactionChild
import com.example.moneymanagement.presentation.database.model.TransactionParent

class LoanViewModel : ViewModel() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var dao: AddNewDao
    val loanList: LiveData<List<AddNewEntity>>
        get() = dao.getAll()


    fun setAppDatabase(appDatabase: AppDatabase) {
        this.appDatabase = appDatabase
        dao = appDatabase.addNewDao()
    }

    fun initData(list: List<AddNewEntity>): List<TransactionParent> {

        val parent = list.groupBy { it.date }
        return parent.map { (date , item) ->
            val child = item.map {
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
            TransactionParent(date, child)

        }
    }
}