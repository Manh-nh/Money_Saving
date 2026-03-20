package com.example.moneymanagement.presentation.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moneymanagement.presentation.view.expendstaticfragment.ExpendStaticFragment
import com.example.moneymanagement.presentation.view.incomestaticfragment.IncomeStaticFragment
import com.example.moneymanagement.presentation.view.loanstaticfragment.LoanStaticFragment
import com.example.moneymanagement.presentation.view.staticactivity.StaticActivity

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