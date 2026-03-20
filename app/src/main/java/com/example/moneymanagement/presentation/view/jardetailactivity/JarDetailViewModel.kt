package com.example.moneymanagement.presentation.view.jardetailactivity

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.AppDatabase
import com.example.moneymanagement.presentation.database.BudgetEntity
import com.example.moneymanagement.presentation.database.model.JarCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JarDetailViewModel : ViewModel() {

    private lateinit var appDatabase: AppDatabase
    private val _data = MutableLiveData<List<JarCategory>>()
    val dataBudget: LiveData<List<JarCategory>> get() = _data

    fun setAppDataBase(appDatabase: AppDatabase) {
        this.appDatabase = appDatabase
    }

    fun deleteBudget(budgetEntity: BudgetEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.addBudget().deleteBudget(budgetEntity)
        }
    }

    fun getData(owner: LifecycleOwner, imgBudget: Int) {

        appDatabase.addNewDao().getAll().observe(owner) { entities ->
            val jarType = entities.filter { it.imgBudget == imgBudget }

            val jarCategory = jarType
                .groupBy { it.nameTypeCategory to it.imgTypeCategory }
                .map { (key, group) ->
                    JarCategory(
                        imgCategory = key.second,
                        nameCategory = key.first,
                        totalMoney = group.sumOf { it.amount },
                        process = 50
                    )
                }

            _data.postValue(jarCategory)
        }

    }

}