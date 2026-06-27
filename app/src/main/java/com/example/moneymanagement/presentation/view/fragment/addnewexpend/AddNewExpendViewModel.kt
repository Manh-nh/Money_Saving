package com.example.moneymanagement.presentation.view.fragment.addnewexpend

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.CategoryModel

class AddNewExpendViewModel() : ViewModel() {

    private var data = mutableListOf<CategoryModel>()

    fun initData(context: Context): List<CategoryModel> {
        data.clear()
        data.add(CategoryModel(context.getString(R.string.category_food), R.drawable.ic_hmburger))
        data.add(CategoryModel(context.getString(R.string.category_social), R.drawable.ic_socical))
        data.add(CategoryModel(context.getString(R.string.category_traffic), R.drawable.ic_traffic))
        data.add(CategoryModel(context.getString(R.string.category_shopping), R.drawable.ic_shooping))
        data.add(CategoryModel(context.getString(R.string.category_grocery), R.drawable.ic_grocery))
        data.add(CategoryModel(context.getString(R.string.category_education), R.drawable.ic_education))
        data.add(CategoryModel(context.getString(R.string.category_bills), R.drawable.ic_bill))
        data.add(CategoryModel(context.getString(R.string.category_rentals), R.drawable.ic_rent))
        data.add(CategoryModel(context.getString(R.string.category_medical), R.drawable.ic_medical))
        data.add(CategoryModel(context.getString(R.string.category_investment), R.drawable.ic_investment))
        data.add(CategoryModel(context.getString(R.string.category_gift), R.drawable.ic_gift))
        data.add(CategoryModel(context.getString(R.string.category_other), R.drawable.ic_other))
        return data
    }

}