package com.example.moneymanagement.presentation.view.fragment.expend

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.databinding.FragmentExpendBinding
import com.example.moneymanagement.presentation.Utils
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.database.model.TransactionChild
import com.example.moneymanagement.presentation.view.activity.addnew.AddNewActivity
import com.example.moneymanagement.presentation.view.adapter.ExpendParentAdapter
import com.example.moneymanagement.presentation.view.adapter.OnClickItemTransaction
import com.example.moneymanagement.presentation.view.base.BaseFragment
import com.example.moneymanagement.presentation.view.activity.home.HomeViewModel
import com.example.moneymanagement.presentation.view.activity.transactions.TransactionsActivity
import com.google.gson.Gson
import java.text.DecimalFormat

class ExpendFragment : BaseFragment<FragmentExpendBinding>(FragmentExpendBinding::inflate),
    OnClickItemTransaction {

    private lateinit var viewModel: ExpendViewModel
    private lateinit var shareDateViewModel: HomeViewModel
    private lateinit var parentAdapter: ExpendParentAdapter

    override fun initializeComponent() {

        viewModel = ViewModelProvider(this)[ExpendViewModel::class.java]
        shareDateViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        parentAdapter = ExpendParentAdapter(this, emptyList())
        binding.lstHistoryExpendParent.adapter = parentAdapter

        val appDatabase = DataManager.getDataBase(requireContext())
        viewModel.setAppDataBase(appDatabase)

        viewModel.expendList.observe(viewLifecycleOwner) { expendEntities ->
            val filteredType = expendEntities.filter { it.type == "expend" }
            val parentData = viewModel.initData(filteredType)
            parentAdapter.setData(parentData)

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
        binding.btnAddExpand.setOnClickListener {
            addExpend()
        }

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
        super.initializeData()
    }

    override fun bindView() {
        totalExpend()
    }

    private fun addExpend() {
        val intent = Intent(requireContext(), AddNewActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClick(item: TransactionChild, date: String) {
        val gson = Gson()
        val value = gson.toJson(item)
        val intent = Intent(requireContext(), TransactionsActivity::class.java)
        intent.putExtra(Utils.ITEM_HISTORY_EXPEND.name, value)
        intent.putExtra("KEY_EXPEND", date)
        startActivity(intent)
    }

    private fun searchHistory() {
        val search = binding.edtSearch.text.toString()
        if (search.isEmpty()) {
            viewModel.expendList.observe(viewLifecycleOwner) { expendEntities ->
                val filteredType = expendEntities.filter { it.type == "expend" }
                val parentData = viewModel.initData(filteredType)
                parentAdapter.setData(parentData)
            }
        } else {
            viewModel.expendList.observe(viewLifecycleOwner) { expendEntities ->
                val filteredType = expendEntities.filter {
                    it.type == "expend" && it.nameTypeCategory.contains(search)
                }
                val parentData = viewModel.initData(filteredType)
                parentAdapter.setData(parentData)
                binding.txtTransaction.visibility =
                    if (parentData.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun totalExpend() {
        val db = DataManager.getDataBase(requireContext())
        db.addNewDao().getAll().observe(this) { list ->
            val typeExpend = list.filter { it.type == "expend" }
            val totalMoneyExpends = typeExpend.sumOf { it.amount }

            val formatMoney = formatMoney(totalMoneyExpends)

            binding.txtMoney.text = "$formatMoney đ"

        }
    }

    private fun formatMoney(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(",", ".")
    }


    private fun filterByMonthYear(selectedMonth: Int, selectedYear: Int) {

        if (selectedMonth == 0 || selectedYear == 0) {
            Toast.makeText(requireContext(), "You are selection month", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.expendList.observe(viewLifecycleOwner) { expendEntities ->
            val filtered = expendEntities.filter { item ->
                val (month, year) = extractMonthYear(item.date)
                item.type == "expend" && month == selectedMonth && year == selectedYear
            }

            val parentData = viewModel.initData(filtered)
            parentAdapter.setData(parentData)

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


