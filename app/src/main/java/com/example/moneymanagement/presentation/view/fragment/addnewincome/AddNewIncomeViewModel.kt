package com.example.moneymanagement.presentation.view.fragment.addnewincome

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.CategoryModel

class AddNewIncomeViewModel : ViewModel() {

    private var data = mutableListOf<CategoryModel>()

    fun initData(context: Context): List<CategoryModel> {
        data.clear()
        data.add(CategoryModel(context.getString(R.string.category_bills), R.drawable.ic_other))
        data.add(CategoryModel(context.getString(R.string.category_rentals), R.drawable.ic_invest))
        data.add(CategoryModel(context.getString(R.string.category_medical), R.drawable.ic_business))
        data.add(CategoryModel(context.getString(R.string.category_investment), R.drawable.ic_interest))
        data.add(CategoryModel(context.getString(R.string.category_gift), R.drawable.ic_gift))
        data.add(CategoryModel(context.getString(R.string.category_other), R.drawable.ic_salary))

        return data
    }

}