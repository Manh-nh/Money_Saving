package com.example.moneymanagement.presentation.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moneymanagement.presentation.view.addnewexpendfragment.AddNewExpendFragment
import com.example.moneymanagement.presentation.view.addnewincomefragment.AddNewIncomeFragment
import com.example.moneymanagement.presentation.view.addnewloanfragment.AddNewLoanFragment

class AddNewAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AddNewExpendFragment()
            1 -> AddNewIncomeFragment()
            2 -> AddNewLoanFragment()
            else -> AddNewExpendFragment()
        }
    }

    override fun getItemCount(): Int = 3
}