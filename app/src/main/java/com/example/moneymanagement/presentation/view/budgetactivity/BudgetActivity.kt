package com.example.moneymanagement.presentation.view.budgetactivity

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.databinding.ActivityBudgetBinding
import com.example.moneymanagement.presentation.database.DataManager
import com.example.moneymanagement.presentation.view.adapter.OnBudgetUpdatedListener
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.example.moneymanagement.presentation.view.budetdetailactivity.BudgetDetailActivity
import com.example.moneymanagement.presentation.view.dialog.SetMoneyBudgetBottomSheet
import java.text.DecimalFormat

class BudgetActivity : BaseActivity<ActivityBudgetBinding>(ActivityBudgetBinding::inflate),
    OnBudgetUpdatedListener {

    private lateinit var viewModel: BudgetViewModel
    private var totalMoneyExpend: Int = 0
    private var moneyBudget: Int = 0
    private var budgetPercent : Float = 0f

    override fun initializeComponent() {
        super.initializeComponent()

        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        val appDatabase = DataManager.getDataBase(this)

        viewModel.setAppDataBase(appDatabase)
        viewModel.loadTotalMoney(this)
        viewModel.getTotalMoney(this)
    }

    override fun initializeEvents() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnEditMoneyBudget.setOnClickListener {
            val sheet = SetMoneyBudgetBottomSheet()
            sheet.setListener(this)
            sheet.show(supportFragmentManager, "money")
        }

        binding.btnBudgetDetail.setOnClickListener {
            val intent = Intent(this, BudgetDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun bindView() {

        viewModel.setTotalMoney.observe(this) {
            if (it == null) {
                binding.txtMoneyBudget.text = "0 đ"
                binding.txtBudgetMoney.text = "Budget: 0 đ"
                moneyBudget = 0
            } else {
                val formattedBudget = formatMoney(it.moneyBudget)
                binding.txtMoneyBudget.text = "$formattedBudget đ"
                binding.txtBudgetMoney.text = "Budget: $formattedBudget đ"
                moneyBudget = it.moneyBudget
                calculateRemainingBudget()
                calculateBudgetPercent()
                progressBar()
            }
        }

        viewModel.getAmountExpend.observe(this) { totalExpend ->
            totalMoneyExpend = totalExpend
            val formattedExpend = formatMoney(totalExpend)
            binding.txtExpends.text = "Expends: $formattedExpend đ"
            calculateRemainingBudget()
            calculateBudgetPercent()
            progressBar()
        }

    }

    private fun calculateBudgetPercent() {
        if (totalMoneyExpend <= 0 || moneyBudget <=0) {
            binding.txtProgress.text = "0%"
            budgetPercent = 0f
        } else {
            budgetPercent = ((totalMoneyExpend.toFloat() / moneyBudget) * 100)
            val percentDisplay = String.format("%.1f", budgetPercent)
            binding.txtProgress.text = "$percentDisplay %"
        }
    }

    override fun onMoneySet(amount: Int) {
        viewModel.updateMoney(amount)
    }

    private fun calculateRemainingBudget() {
        val remainingAmount = moneyBudget - totalMoneyExpend
        val formattedRemain = formatMoney(remainingAmount)
        binding.txtRemain.text = "Remain: $formattedRemain đ"
        binding.txtMoneyBudget.text = "$formattedRemain đ"
    }

    private fun formatMoney(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(",", ".")
    }

    private fun progressBar(){
        if (totalMoneyExpend <= 0) {
            binding.progressBar.progress = 0
        } else {
            budgetPercent = ((totalMoneyExpend.toFloat() / moneyBudget) * 100)
            binding.progressBar.progress = budgetPercent.toInt()
        }
    }

}
