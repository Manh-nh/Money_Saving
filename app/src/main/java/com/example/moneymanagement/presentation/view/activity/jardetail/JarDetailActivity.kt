package com.example.moneymanagement.presentation.view.activity.jardetail

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.ActivityJarDetailBinding
import com.example.moneymanagement.presentation.Utils
import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.view.adapter.JarTransactionAdapter
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.google.gson.Gson
import java.text.DecimalFormat

class JarDetailActivity :
    BaseActivity<ActivityJarDetailBinding>(ActivityJarDetailBinding::inflate) {

    private lateinit var budgetEntity: BudgetEntity
    private lateinit var viewModel: JarDetailViewModel
    private lateinit var transactionAdapter: JarTransactionAdapter


    override fun initializeComponent() {

        viewModel = ViewModelProvider(this)[JarDetailViewModel::class.java]
        val appDatabase = DataManager.getDataBase(this)
        viewModel.setAppDataBase(appDatabase)

        transactionAdapter = JarTransactionAdapter(emptyList())
        binding.lstTransactions.adapter = transactionAdapter

        val data = intent.getStringExtra(Utils.BUDGET_DETAIL.name)
        if (data != null) {
            val gson = Gson()
            budgetEntity = gson.fromJson(data, BudgetEntity::class.java)
            viewModel.getData(this, budgetEntity.imgBudget, budgetEntity.nameBudget)
        } else {
            finish()
        }

        viewModel.transactions.observe(this) {
            transactionAdapter.setData(it)
        }

    }

    override fun initializeEvents() {
        binding.btnExit.setOnClickListener { finish() }

        binding.btnDelete.setOnClickListener { deleteBudget() }

    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun bindView() {
        if (!::budgetEntity.isInitialized) return

        val formatMoneyJar = formatMoney(budgetEntity.moneyBudget)

        binding.txtTitleJar.text = budgetEntity.nameBudget
        binding.totalMoneyJar.text = "$formatMoneyJar đ"
        binding.imgJar.setImageResource(budgetEntity.imgBudget)
    }

    private fun deleteBudget() {
        if (!::budgetEntity.isInitialized) return
        val dialog = AlertDialog.Builder(this)
            .setTitle(this.getString(R.string.delete_budget))
            .setMessage(this.getString(R.string.do_you_want_to_delete_budget))
            .setPositiveButton(this.getString(R.string.delete)) { _, _ ->
                viewModel.deleteBudget(budgetEntity)

                finish()
            }
            .setNegativeButton(this.getString(R.string.cancel), null)
            .create()

        dialog.show()
    }


    private fun formatMoney(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(",", ".")
    }

}