package com.example.moneymanagement.presentation.view.activity.static

import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.ActivityStaticBinding
import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.view.adapter.StaticAdapter
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat

class StaticActivity : BaseActivity<ActivityStaticBinding>((ActivityStaticBinding::inflate)) {

    private lateinit var adapter: StaticAdapter

    override fun initializeComponent() {
        super.initializeComponent()

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE


        adapter = StaticAdapter(this)
        binding.viewPageStatic.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPageStatic) { tab, position ->
            tab.text = when (position) {
                0 -> this.getText(R.string.expend)
                1 -> this.getText(R.string.income)
                2 -> this.getText(R.string.loan)
                else ->  this.getText(R.string.expend)
            }
        }.attach()

        // Calculate and observe total money dynamically
        val db = DataManager.getDataBase(this)
        var initialBudget = 0
        var transactionsList: List<AddNewEntity> = emptyList()

        fun updateUI() {
            val expendMoney = transactionsList.filter { it.type == "expend" }
            val totalMoneyExpend = expendMoney.sumOf { it.amount }

            val incomeMoney = transactionsList.filter { it.type == "income" }
            val totalMoneyIncome = incomeMoney.sumOf { it.amount }

            val loanMoney = transactionsList.filter { it.type == "loan" && it.nameTypeCategory == "Loan" }
            val totalMoneyLoan = loanMoney.sumOf { it.amount }

            val borrowMoney = transactionsList.filter { it.type == "loan" && it.nameTypeCategory == "Borrow" }
            val totalMoneyBorrow = borrowMoney.sumOf { it.amount }

            val totalMoney = initialBudget - totalMoneyExpend + totalMoneyIncome - totalMoneyBorrow + totalMoneyLoan
            binding.txtTotalMoney.text = "${formatMoney(totalMoney)} vnđ"
        }

        db.setMoney().getMoney().observe(this) { budgetEntity ->
            initialBudget = budgetEntity?.moneyBudget ?: 0
            updateUI()
        }

        db.addNewDao().getAll().observe(this) { list ->
            transactionsList = list ?: emptyList()
            updateUI()
        }
    }

    override fun initializeEvents() {
        binding.btnBack.setOnClickListener { finish() }
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun bindView() {
        super.bindView()
    }

    private fun formatMoney(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(",", ".")
    }

}