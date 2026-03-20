package com.example.moneymanagement.presentation.view.adapter

import com.example.moneymanagement.presentation.database.model.CategoryModel

interface OnClickItemAddNew {

    fun onClickListenerCategory(item: CategoryModel, position: Int)

    fun onClickListenerBudget(nameBudget : String, imgBudget: Int)

    fun onCLickListenerDate(day: Int, month: Int, year: Int)

    fun onClickListerTime(minute: Int, hour: Int)

}