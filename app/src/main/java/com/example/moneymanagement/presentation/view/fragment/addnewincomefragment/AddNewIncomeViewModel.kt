package com.example.moneymanagement.presentation.view.fragment.addnewincomefragment

import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.CategoryModel

class AddNewIncomeViewModel : ViewModel() {

    private var data = mutableListOf<CategoryModel>()

    fun initData(): List<CategoryModel> {
        data.add(CategoryModel("Bills", R.drawable.ic_other))
        data.add(CategoryModel("Rentals", R.drawable.ic_invest))
        data.add(CategoryModel("Medical", R.drawable.ic_business))
        data.add(CategoryModel("Investment", R.drawable.ic_interest))
        data.add(CategoryModel("Gift", R.drawable.ic_gift))
        data.add(CategoryModel("Other", R.drawable.ic_salary));

        return data
    }

}