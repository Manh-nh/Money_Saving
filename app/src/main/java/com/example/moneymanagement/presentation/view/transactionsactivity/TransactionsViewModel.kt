package com.example.moneymanagement.presentation.view.transactionsactivity

import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.AddNewDao
import com.example.moneymanagement.presentation.database.AddNewEntity
import com.example.moneymanagement.presentation.database.AppDatabase
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

    fun delete(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(id)
        }

    }


}