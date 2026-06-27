package com.example.moneymanagement.presentation.view.activity.budetdetail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetDetailViewModel : ViewModel() {

    private lateinit var appDatabase: AppDatabase

    lateinit var listBudget: LiveData<List<BudgetEntity>>
    lateinit var overallBudget: LiveData<com.example.moneymanagement.presentation.database.roomdb.MoneyBudgetEntity>

    private var _money = MutableLiveData<Int>()
    val money: LiveData<Int> get() = _money

    fun setAppDataBase(appDatabase: AppDatabase) {
        this.appDatabase = appDatabase
        listBudget = appDatabase.addBudget().getBudgetDetail()
        overallBudget = appDatabase.setMoney().getMoney()
    }

    fun initData(context : Context) {
        CoroutineScope(Dispatchers.IO).launch {

            val count = appDatabase.addBudget().countJar()

            if (count == 0) {
                val budgetName = listOf(
                    BudgetEntity(1, context.getString(R.string.necessities), 0, R.drawable.ic_necessities),
                    BudgetEntity(2, context.getString(R.string.education), 0, R.drawable.ic_education_budget),
                    BudgetEntity(3, context.getString(R.string.saving), 0, R.drawable.ic_saving_budget),
                    BudgetEntity(4, context.getString(R.string.play), 0, R.drawable.ic_play_budget),
                    BudgetEntity(5, context.getString(R.string.investment), 0, R.drawable.ic_investment_budget),
                    BudgetEntity(6, context.getString(R.string.give), 0, R.drawable.ic_give_budget),

                    )

                budgetName.forEach { jar ->
                    appDatabase.addBudget().insertBudgetDetail(jar)
                }
            }
        }
    }

    fun updateMoney(id: Int, newMoney: Int, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {

            val budgetEntity = appDatabase.setMoney().getMoneyNow()
            val maxMoney = budgetEntity?.moneyBudget ?: 0

            if (budgetEntity == null || maxMoney == 0) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.please_set_the_overall_budget_first),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            if (newMoney <= maxMoney) {
                appDatabase.addBudget().updateInitialAndCurrentMoney(id, newMoney, newMoney)
                recalculateBalances()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, context.getString(R.string.update_success), Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.the_jar_balance_must_not_exceed_the_overall_budget),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun insertBudget(name: String, money: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val newJar = BudgetEntity(
                nameBudget = name,
                moneyBudget = money,
                initialBudget = money,
                imgBudget = R.drawable.ic_saving_budget // Default icon for new jars
            )
            appDatabase.addBudget().insertBudgetDetail(newJar)
        }
    }

    fun recalculateBalances(showToast: Boolean = false) {
        CoroutineScope(Dispatchers.IO).launch {
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
            if (showToast) {
                _syncSuccess.postValue(true)
            }
        }
    }

    private val _syncSuccess = MutableLiveData<Boolean>()
    val syncSuccess: LiveData<Boolean> get() = _syncSuccess

    fun takeFromJar(owner: LifecycleOwner) {
    }

}
