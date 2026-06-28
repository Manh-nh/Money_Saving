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

    private val _resetEvent = MutableLiveData<Boolean>()
    val resetEvent: LiveData<Boolean> get() = _resetEvent

    fun updateMoney(money : Int){

        CoroutineScope(Dispatchers.IO).launch {
            val jars = appDatabase.addBudget().getBudgetDetailSync()
            val totalAllocated = jars.sumOf { it.initialBudget }

            var didReset = false
            if (money < totalAllocated) {
                appDatabase.addBudget().resetAllJarsBudgets()
                recalculateBalances()
                didReset = true
            }

            val entity = MoneyBudgetEntity(id = 1, moneyBudget = money)
            appDatabase.setMoney().insertOrUpdate(entity)
            _resetEvent.postValue(didReset)
        }
    }

    private suspend fun recalculateBalances() {
        val transactions = appDatabase.addNewDao().getAllSync()
        val jars = appDatabase.addBudget().getBudgetDetailSync()

        jars.forEach { jar ->
            val jarTransactions = transactions.filter { it.nameBudget == jar.nameBudget }
            var balance = jar.initialBudget

            jarTransactions.forEach { trans ->
                when (trans.type) {
                    "expend" -> balance -= trans.amount
                    "income" -> balance += trans.amount
                    "loan" -> {
                        if (trans.nameTypeCategory == "Loan") balance += trans.amount
                        else if (trans.nameTypeCategory == "Borrow") balance -= trans.amount
                    }
                }
            }
            appDatabase.addBudget().updateMoney(jar.id, balance)
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