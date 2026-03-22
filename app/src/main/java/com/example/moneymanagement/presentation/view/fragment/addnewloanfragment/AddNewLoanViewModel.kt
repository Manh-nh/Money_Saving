package com.example.moneymanagement.presentation.view.fragment.addnewloanfragment

import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.CategoryModel

class AddNewLoanViewModel : ViewModel() {

    private var data = mutableListOf<CategoryModel>()

    fun initData(): List<CategoryModel> {
        data.add(CategoryModel("Loan", R.drawable.ic_loan));
        data.add(CategoryModel("Borrow", R.drawable.ic_borrow))

        return data
    }

}