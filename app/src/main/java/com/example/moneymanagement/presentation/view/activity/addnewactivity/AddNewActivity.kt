package com.example.moneymanagement.presentation.view.activity.addnewactivity

import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.moneymanagement.databinding.ActivityAddNewBinding
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.view.adapter.AddNewAdapter
import com.example.moneymanagement.presentation.view.fragment.addnewexpendfragment.AddNewExpendFragment
import com.example.moneymanagement.presentation.view.fragment.addnewincomefragment.AddNewIncomeFragment
import com.example.moneymanagement.presentation.view.fragment.addnewloanfragment.AddNewLoanFragment
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator

class AddNewActivity : BaseActivity<ActivityAddNewBinding>(ActivityAddNewBinding::inflate) {

    private lateinit var adapter: com.example.moneymanagement.presentation.view.adapter.AddNewAdapter
    private val addNewViewModel: com.example.moneymanagement.presentation.view.activity.addnewactivity.AddNewViewModel by viewModels()
    private lateinit var typeAddNew: String
    private lateinit var appDatabase: AppDatabase

    override fun initializeComponent() {
        super.initializeComponent()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        adapter = AddNewAdapter(this)
        binding.viewPagerAddNew.adapter = adapter

        appDatabase = DataManager.getDataBase(this)
        addNewViewModel.setAppDataBase(appDatabase)

        TabLayoutMediator(binding.tabLayoutAdd, binding.viewPagerAddNew) { tab, position ->
            tab.text = when (position) {
                0 -> "Expend"
                1 -> "Income"
                2 -> "Loan"
                else -> "Expend"
            }
        }.attach()

        binding.viewPagerAddNew.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                typeAddNew = when (position) {
                    0 -> "expend"
                    1 -> "income"
                    2 -> "loan"
                    else -> "expend"
                }
                addNewViewModel.setType(typeAddNew)
            }
            })

//        updateJar()

    }

    override fun initializeEvents() {
        
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { saveData() }
    }

    private fun saveData() {

        val getCurrent = binding.viewPagerAddNew.currentItem

        when(getCurrent){
            0 -> {
                val fragmentExpend = supportFragmentManager.findFragmentByTag("f0") as? AddNewExpendFragment
                fragmentExpend?.sendDataExpend()
            }

            1 -> {
                val fragmentIncome = supportFragmentManager.findFragmentByTag("f1") as? AddNewIncomeFragment
                fragmentIncome?.sendDataIncome()
            }

            2 -> {
                val fragmentLoan = supportFragmentManager.findFragmentByTag("f2") as? AddNewLoanFragment
                fragmentLoan?.senDataLoan()
            }
        }

        val data = addNewViewModel.getDataList()
        if (!data.isNullOrEmpty()) {
            val expend = data[0]
            addNewViewModel.insertExpendEntity(
                expend.amount,
                expend.type,
                expend.nameTypeCategory,
                expend.imgTypeCategory,
                expend.nameBudget,
                expend.note,
                expend.date,
                expend.time,
                expend.imgBudget

            )
            Toast.makeText(this, "Save success", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}