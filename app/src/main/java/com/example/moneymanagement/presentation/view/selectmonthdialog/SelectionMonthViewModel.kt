package com.example.moneymanagement.presentation.view.selectmonthdialog

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.MonthModel

class SelectionMonthViewModel : ViewModel() {

    private var data = mutableListOf<MonthModel>()

   fun initData(context: Context) : List<MonthModel> {
      data.clear()
      data.add(MonthModel(context.getString(R.string.month_jan)))
      data.add(MonthModel(context.getString(R.string.month_feb)))
      data.add(MonthModel(context.getString(R.string.month_mar)))
      data.add(MonthModel(context.getString(R.string.month_apr)))
      data.add(MonthModel(context.getString(R.string.month_may_2)))
      data.add(MonthModel(context.getString(R.string.month_jun)))
      data.add(MonthModel(context.getString(R.string.month_jul)))
      data.add(MonthModel(context.getString(R.string.month_aug)))
      data.add(MonthModel(context.getString(R.string.month_sep)))
      data.add(MonthModel(context.getString(R.string.month_oct)))
      data.add(MonthModel(context.getString(R.string.month_nov)))
      data.add(MonthModel(context.getString(R.string.month_dec)))

      return data
   }



}