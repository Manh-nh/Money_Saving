package com.example.moneymanagement.presentation.view.activity.transactionsactivity

import android.graphics.Color
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.ActivityTransactionsBinding
import com.example.moneymanagement.presentation.Utils
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.database.model.TransactionChild
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.google.gson.Gson
import java.text.DecimalFormat
import kotlin.jvm.java

class TransactionsActivity :
    BaseActivity<ActivityTransactionsBinding>(ActivityTransactionsBinding::inflate) {

    private lateinit var data: TransactionChild
    private lateinit var viewModel: TransactionsViewModel
    private lateinit var value: String
    private var date: String? = null
    override fun initializeComponent() {
        super.initializeComponent()

        viewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]

        val expendValue = intent.getStringExtra(Utils.ITEM_HISTORY_EXPEND.name)
        val incomeValue = intent.getStringExtra(Utils.ITEM_HISTORY_INCOME.name)
        val loanValue = intent.getStringExtra(Utils.ITEM_HISTORY_LOAN.name)

        val expendDate = intent.getStringExtra("KEY_EXPEND")
        val incomeDate = intent.getStringExtra("KEY_INCOME")
        val loanDate = intent.getStringExtra("KEY_LOAN")

        if (expendValue != null) {
            value = expendValue
            date = expendDate
        } else if (incomeValue != null) {
            value = incomeValue
            date = incomeDate
        } else if (loanValue != null) {
            value = loanValue
            date = loanDate
        } else {
            value = ""
        }

        val gson = Gson()
        data = gson.fromJson(value, TransactionChild::class.java)

        val appDatabase = DataManager.getDataBase(this)
        viewModel.setAppDatabase(appDatabase)
    }

    override fun initializeEvents() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnDelete.setOnClickListener { deleteItem() }
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun bindView() {
        val moneyValue: Int = data.expendPrice
        var bindMoney = ""
        var color: Int = 0

        binding.imgCategoryMain.setImageResource(data.imgCategory)
        binding.txtNameTypeCategory.text = data.nameCategory

        if (data.type == "expend") {
            bindMoney = "- ${formatMoney(moneyValue)} đ"
            color = (Color.parseColor("#F44336"))
        } else if (data.type == "income") {
            bindMoney = "+ ${formatMoney(moneyValue)} đ"
            color = (Color.parseColor("#4CAF50"))
        } else {
            if (data.nameCategory == "Bills") {
                bindMoney =  "+ ${formatMoney(moneyValue)} đ"
                color = (Color.parseColor("#4CAF50"))
            } else {
                bindMoney = "- ${formatMoney(moneyValue)} đ"
                color = (Color.parseColor("#F44336"))
            }
        }

        binding.txtPrice.text = bindMoney
        binding.txtPrice.setTextColor(color)
        binding.txtdate.text = date
        binding.imgCategory.setImageResource(data.imgCategory)
        binding.txtNameCategory.text = data.nameCategory
        binding.txtContentCategory.text = data.note
        binding.txtNameBudget.text = data.nameBudget
        binding.txtContentBudget.text = data.note
        binding.edtNote.setText(data.note)
        binding.imgBudget.setImageResource(data.imgBudget)

        Log.d("nam", data.imgBudget.toString())
    }

    private fun deleteItem() {

        val dialog = AlertDialog.Builder(this).setTitle("Delete")
            .setMessage("Do you want to delete this item")
            .setPositiveButton("Delete") { dialog, it ->
                viewModel.delete(data.id)
                finish()
            }
            .setNegativeButton("Cancel") { dialog, it ->
                dialog.dismiss()
            }

            .show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.aleart_dialog_delete)
    }

    private fun formatMoney(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(",", ".")
    }

}