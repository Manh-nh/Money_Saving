package com.example.moneymanagement.presentation.view.fragment.addnewloan

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.CategoryModel

class AddNewLoanViewModel : ViewModel() {

    private var data = mutableListOf<CategoryModel>()

    fun initData(context: Context): List<CategoryModel> {
        data.clear()
        data.add(CategoryModel(context.getString(R.string.category_loan), R.drawable.ic_loan))
        data.add(CategoryModel(context.getString(R.string.category_borrow), R.drawable.ic_borrow))

        return data
    }

}