package com.example.moneymanagement.presentation.view.selectmonthdialog

import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.model.MonthModel

class SelectionMonthViewModel : ViewModel() {

    private var data = mutableListOf<MonthModel>()

     fun initData() : List<MonthModel> {
        data.add(MonthModel("Jan"))
        data.add(MonthModel("Feb"))
        data.add(MonthModel("Mar"))
        data.add(MonthModel("Apr"))
        data.add(MonthModel("May"))
        data.add(MonthModel("Jun"))
        data.add(MonthModel("Jul"))
        data.add(MonthModel("Aug"))
        data.add(MonthModel("Sep"))
        data.add(MonthModel("Oct"))
        data.add(MonthModel("Nov"))
        data.add(MonthModel("Dec"))

        return data
    }


}