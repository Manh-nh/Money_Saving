package com.example.moneymanagement.presentation.view.activity.budetdetail

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.databinding.ActivityBudgetDetailBinding
import com.example.moneymanagement.presentation.Utils
import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.view.adapter.BudgetDetailAdapter
import com.example.moneymanagement.presentation.view.adapter.OnAddBudgerListener
import com.example.moneymanagement.presentation.view.adapter.OnClickListenerUpdateMoney
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.example.moneymanagement.presentation.view.dialog.SetBudgetNameBottomSheetDialog
import com.example.moneymanagement.presentation.view.dialog.SetMoneyJarBottomSheet
import com.example.moneymanagement.presentation.view.activity.jardetail.JarDetailActivity
import com.google.gson.Gson

class BudgetDetailActivity :
    BaseActivity<ActivityBudgetDetailBinding>(ActivityBudgetDetailBinding::inflate),
    OnAddBudgerListener,
    OnClickListenerUpdateMoney {

    private lateinit var adapter: BudgetDetailAdapter
    private lateinit var viewModel: BudgetDetailViewModel

    private val _data = MutableLiveData<List<BudgetEntity>>()
    val data: LiveData<List<BudgetEntity>> get() = _data

    override fun initializeComponent() {
        super.initializeComponent()

        viewModel = ViewModelProvider(this)[BudgetDetailViewModel::class.java]

        val appDatabase = DataManager.getDataBase(this)
        viewModel.setAppDataBase(appDatabase)

        adapter = BudgetDetailAdapter(emptyList(), this)
        binding.lstBudgetName.adapter = adapter

        viewModel.listBudget.observe(this) {
            adapter.setData(it)
        }

        viewModel.syncSuccess.observe(this) { success ->
            if (success) {
                android.widget.Toast.makeText(this, "Synchronization complete", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.initData()
        viewModel.recalculateBalances()


    }

    override fun initializeEvents() {

        binding.btnBack.setOnClickListener { finish() }

        binding.btnAddBudget.setOnClickListener {
            val sheet = SetBudgetNameBottomSheetDialog()
            sheet.setListener(this)
            sheet.show(supportFragmentManager, "add budget")
        }

        binding.btnSync.setOnClickListener {
            viewModel.recalculateBalances(true)
        }

    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun bindView() {

    }

    override fun onAddBudgetListener(setMoney: Int, setNameBudget: String) {
        viewModel.insertBudget(setNameBudget, setMoney)
    }

    override fun getJar(id: Int, money: Int, jarName: String) {
        val sheet = SetMoneyJarBottomSheet(money, jarName, id)
        sheet.setListener(this)
        sheet.show(supportFragmentManager, "update money")
    }

    override fun updateMoney(id: Int, moneyJar: Int) {
        viewModel.updateMoney(id, moneyJar, this)
    }

    override fun onItemClick(budget: BudgetEntity) {
        val intent = Intent(this, JarDetailActivity::class.java)
        val gson = Gson()
        val data = gson.toJson(budget)
        intent.putExtra(Utils.BUDGET_DETAIL.name, data)
        startActivity(intent)
    }


}