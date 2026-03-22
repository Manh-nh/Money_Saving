package com.example.moneymanagement.presentation.view.activity.homeactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _selectedMonthYear = MutableLiveData<Triple<Int, Int, String>>()  // (month, year)
    val selectedMonthYear: LiveData<Triple<Int, Int, String>> get() = _selectedMonthYear

    fun sendMonthYear(month: Int, year: Int, monthFormat: String ) {
        _selectedMonthYear.value = Triple(month, year, monthFormat)
    }

}