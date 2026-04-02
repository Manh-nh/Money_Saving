package com.example.moneymanagement.presentation.view.activity.budget

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import com.example.moneymanagement.presentation.database.roomdb.MoneyBudgetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel : ViewModel() {

    private lateinit var appDatabase : AppDatabase

    private val _setTotalMoney = MutableLiveData<MoneyBudgetEntity>()
    val setTotalMoney: LiveData<MoneyBudgetEntity> get() = _setTotalMoney

    private val _getAmountExpend = MutableLiveData<Int>()
    val getAmountExpend : LiveData<Int> get() = _getAmountExpend

    fun setAppDataBase(appDatabase: AppDatabase){
        this.appDatabase = appDatabase
    }

     fun loadTotalMoney(owner : LifecycleOwner) {
        appDatabase.setMoney().getMoney().observe(owner) {
            _setTotalMoney.postValue(it)
        }
    }

    fun updateMoney(money : Int){

        CoroutineScope(Dispatchers.IO).launch {
            val entity = MoneyBudgetEntity(id = 1, moneyBudget = money)
            appDatabase.setMoney().insertOrUpdate(entity)
        }
    }

    fun getTotalMoney(owner: LifecycleOwner){
        appDatabase.addNewDao().getAll().observe(owner){entities ->

            val typeExpend = entities.filter { it.type == "expend" }
            val totalMoney = typeExpend.sumOf { it.amount }
            _getAmountExpend.postValue(totalMoney)

        }
    }


}