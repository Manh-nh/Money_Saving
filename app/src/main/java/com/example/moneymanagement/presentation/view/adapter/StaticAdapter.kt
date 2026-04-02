package com.example.moneymanagement.presentation.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moneymanagement.presentation.view.fragment.expendstatic.ExpendStaticFragment
import com.example.moneymanagement.presentation.view.fragment.income.IncomeStaticFragment
import com.example.moneymanagement.presentation.view.fragment.loanstatic.LoanStaticFragment
import com.example.moneymanagement.presentation.view.activity.static.StaticActivity

class StaticAdapter(activity: StaticActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpendStaticFragment()
            1 -> IncomeStaticFragment()
            2 -> LoanStaticFragment()
            else -> ExpendStaticFragment()
        }
    }

    override fun getItemCount(): Int = 3
}