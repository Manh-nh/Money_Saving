package com.example.moneymanagement.presentation.view.introactivity

import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.IntroModel

class IntroViewModel : ViewModel() {

    private var data: ArrayList<IntroModel> = ArrayList()

    fun initData(): ArrayList<IntroModel> {

        data.add(
            IntroModel(
                R.drawable.intro1,
                "Financial management",
                "Smart expense tracking, easy to manage"
            )
        )
        data.add(
            IntroModel(
                R.drawable.intro2,
                "Expense analysis chart",
                "Smart chart for monthly financial management reporting"
            )
        )
        data.add(
            IntroModel(
                R.drawable.intro3,
                "Expense classification",
                "Clear income classification with six recommended spending jars."
            )
        )
        data.add(
            IntroModel(
                R.drawable.intro4,
                "Debt management",
                "Loans are clearly and tightly categorized."
            )
        )
        return data
    }


}