package com.example.moneymanagement.presentation.view.activity.jardetail

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity
import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JarDetailViewModel : ViewModel() {

    private lateinit var appDatabase: AppDatabase
    
    private val _transactions = MutableLiveData<List<AddNewEntity>>()
    val transactions: LiveData<List<AddNewEntity>> get() = _transactions

    fun setAppDataBase(appDatabase: AppDatabase) {
        this.appDatabase = appDatabase
    }

    fun deleteBudget(budgetEntity: BudgetEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.addBudget().deleteBudget(budgetEntity)
        }
    }

    fun getData(owner: LifecycleOwner, imgBudget: Int, nameBudget: String) {

        appDatabase.addNewDao().getAll().observe(owner) { entities ->
            val jarType = entities.filter { 
                it.nameBudget == nameBudget || it.imgBudget == imgBudget
            }

            _transactions.postValue(jarType)
        }

    }

}