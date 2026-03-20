package com.example.moneymanagement.presentation.view.addnewexpendfragment

import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.CategoryModel

class AddNewExpendViewModel() : ViewModel() {

    private var data = mutableListOf<CategoryModel>()


    fun initData(): List<CategoryModel> {
        data.add(CategoryModel("Food", R.drawable.ic_hmburger))
        data.add(CategoryModel("Social", R.drawable.ic_socical))
        data.add(CategoryModel("Traffic", R.drawable.ic_traffic))
        data.add(CategoryModel("Shopping", R.drawable.ic_shooping))
        data.add(CategoryModel("Grocery", R.drawable.ic_grocery))
        data.add(CategoryModel("Education", R.drawable.ic_education))
        data.add(CategoryModel("Bills", R.drawable.ic_bill))
        data.add(CategoryModel("Rentals", R.drawable.ic_rent))
        data.add(CategoryModel("Medical", R.drawable.ic_medical))
        data.add(CategoryModel("Investment", R.drawable.ic_investment))
        data.add(CategoryModel("Gift", R.drawable.ic_gift))
        data.add(CategoryModel("Other", R.drawable.ic_other))
        return data
    }

}