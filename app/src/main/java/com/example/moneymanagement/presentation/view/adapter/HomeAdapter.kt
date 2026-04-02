package com.example.moneymanagement.presentation.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moneymanagement.presentation.view.fragment.expend.ExpendFragment
import com.example.moneymanagement.presentation.view.fragment.incom.IncomeFragment
import com.example.moneymanagement.presentation.view.fragment.loan.LoanFragment

class HomeAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpendFragment()
            1 -> IncomeFragment()
            2 -> LoanFragment()
            else -> ExpendFragment()
        }
    }

    override fun getItemCount() = 3


}