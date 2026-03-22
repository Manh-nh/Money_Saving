package com.example.moneymanagement.presentation.view.fragment.incomfragment

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.databinding.FragmentIncomeBinding
import com.example.moneymanagement.presentation.Utils
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.database.model.TransactionChild
import com.example.moneymanagement.presentation.view.activity.addnewactivity.AddNewActivity
import com.example.moneymanagement.presentation.view.adapter.IncomeParentAdapter
import com.example.moneymanagement.presentation.view.adapter.OnClickItemTransaction
import com.example.moneymanagement.presentation.view.base.BaseFragment
import com.example.moneymanagement.presentation.view.activity.homeactivity.HomeViewModel
import com.example.moneymanagement.presentation.view.activity.transactionsactivity.TransactionsActivity
import com.google.gson.Gson
import java.text.DecimalFormat

class IncomeFragment : BaseFragment<FragmentIncomeBinding>(FragmentIncomeBinding::inflate),
    OnClickItemTransaction {

    private lateinit var viewModel: IncomeViewModel
    private lateinit var shareDateViewModel: HomeViewModel
    private lateinit var adapter: IncomeParentAdapter

    override fun initializeComponent() {

        viewModel = ViewModelProvider(this)[IncomeViewModel::class.java]
        shareDateViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]


        adapter = IncomeParentAdapter(this, emptyList())
        binding.lstHistoryIncome.adapter = adapter

        val appDatabase = DataManager.getDataBase(requireContext())
        viewModel.setAppDataBase(appDatabase)

        viewModel.incomeList.observe(viewLifecycleOwner) { incomeEntities ->
            val filteredType = incomeEntities.filter { it.type == "income" }
            val parentData = viewModel.initData(filteredType)
            adapter.setData(parentData)

            binding.txtTransaction.visibility = if (filteredType.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        shareDateViewModel.selectedMonthYear.observe(viewLifecycleOwner) { (month, year, _) ->
            filterByMonthYear(month, year)
        }

    }

    override fun initializeEvents() {
        binding.btnAddInCome.setOnClickListener { addIncome() }

        binding.edtSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                searchHistory()
                true
            } else {
                false
            }
        }
    }

    override fun initializeData() {
    }

    override fun bindView() {
        totalMoneyIncome()
    }

    private fun addIncome() {
        val intent = Intent(requireContext(), AddNewActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClick(item: TransactionChild, date: String) {
        val gson = Gson()
        val value = gson.toJson(item)
        val intent = Intent(requireContext(), TransactionsActivity::class.java)
        intent.putExtra(Utils.ITEM_HISTORY_INCOME.name, value)
        intent.putExtra("KEY_INCOME", date)
        startActivity(intent)
    }

    private fun searchHistory() {
        val search = binding.edtSearch.text.toString()
        if (search.isEmpty()) {
            viewModel.incomeList.observe(viewLifecycleOwner) { incomeEntities ->
                val filteredType = incomeEntities.filter { it.type == "income" }
                val parentData = viewModel.initData(filteredType)
                adapter.setData(parentData)
            }
        } else {
            viewModel.incomeList.observe(viewLifecycleOwner) { incomeEntities ->
                val filteredType = incomeEntities.filter {
                    it.type == "income" && it.nameTypeCategory.contains(search)
                }
                val parentData = viewModel.initData(filteredType)
                adapter.setData(parentData)
            }
        }
    }

    private fun totalMoneyIncome(){
        val db = DataManager.getDataBase(requireContext())
        db.addNewDao().getAll().observe(this){list ->
            val typeIncome = list.filter { it.type == "income" }
            val totalMoney = typeIncome.sumOf { it.amount }

            val formatMoneyIncome = formatMoney(totalMoney)

            binding.txtMoney.text = "${formatMoneyIncome} đ"
        }
    }

    private fun formatMoney(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(",", ".")
    }


    private fun filterByMonthYear(selectedMonth: Int, selectedYear: Int) {

        if(selectedMonth == 0 || selectedYear == 0){
            Toast.makeText(requireContext(), "You are selection month", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.incomeList.observe(viewLifecycleOwner) { incomeEntities ->
            val filtered = incomeEntities.filter { item ->
                val (month, year) = extractMonthYear(item.date)
                item.type == "expend" && month == selectedMonth && year == selectedYear
            }

            val parentData = viewModel.initData(filtered)
            adapter.setData(parentData)

            binding.txtTransaction.visibility =
                if (filtered.isEmpty()) View.VISIBLE else View.GONE
        }
    }


    private fun extractMonthYear(dateString: String): Pair<Int, Int> {
        val parts = dateString.split("/")
        val month = parts[1].toInt()
        val year = parts[2].toInt()
        return Pair(month, year)
    }



}