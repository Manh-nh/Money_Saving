package com.example.moneymanagement.presentation.view.fragment.loanfragment

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.databinding.FragmentLoanBinding
import com.example.moneymanagement.presentation.Utils
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.database.model.TransactionChild
import com.example.moneymanagement.presentation.database.model.TransactionParent
import com.example.moneymanagement.presentation.view.activity.addnewactivity.AddNewActivity
import com.example.moneymanagement.presentation.view.adapter.LoanParentAdapter
import com.example.moneymanagement.presentation.view.adapter.OnClickItemTransaction
import com.example.moneymanagement.presentation.view.base.BaseFragment
import com.example.moneymanagement.presentation.view.activity.homeactivity.HomeViewModel
import com.example.moneymanagement.presentation.view.activity.transactionsactivity.TransactionsActivity
import com.google.gson.Gson
import java.text.DecimalFormat

class LoanFragment : BaseFragment<FragmentLoanBinding>(FragmentLoanBinding::inflate),
    OnClickItemTransaction {

    private lateinit var adapter: LoanParentAdapter
    private lateinit var data: List<TransactionParent>
    private lateinit var viewModel: LoanViewModel
    private lateinit var shareDateViewModel: HomeViewModel


    override fun initializeComponent() {

        viewModel = ViewModelProvider(this)[LoanViewModel::class.java]
        shareDateViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val appDatabase = DataManager.getDataBase(requireContext())
        viewModel.setAppDatabase(appDatabase)

        adapter = LoanParentAdapter(emptyList(), this)
        binding.lstHistoryLoan.adapter = adapter

        viewModel.loanList.observe(viewLifecycleOwner) { loanEntities ->
            val filterType = loanEntities.filter { it.type == "loan" }
            data = viewModel.initData(filterType)
            adapter.setData(data)

            binding.txtTransaction.visibility = if (filterType.isEmpty()) {
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
        binding.btnAddLoan.setOnClickListener {
            val intent = Intent(requireContext(), AddNewActivity::class.java)
            startActivity(intent)
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
        totalMoney()
    }

    override fun onItemClick(item: TransactionChild, date: String) {
        val gson = Gson()
        val value = gson.toJson(item)
        val intent = Intent(requireContext(), TransactionsActivity::class.java)
        intent.putExtra(Utils.ITEM_HISTORY_LOAN.name, value)
        intent.putExtra("KEY_LOAN", date)
        startActivity(intent)
    }

    private fun searchHistory() {
        val search = binding.edtSearch.text.toString()
        if (search.isEmpty()) {
            viewModel.loanList.observe(viewLifecycleOwner) { loanEntities ->
                val filteredType = loanEntities.filter { it.type == "loan" }
                val parentData = viewModel.initData(filteredType)
                adapter.setData(parentData)
            }
        } else {
            viewModel.loanList.observe(viewLifecycleOwner) { loanEntities ->
                val filteredType = loanEntities.filter {
                    it.type == "loan" && it.nameTypeCategory.contains(search)
                }
                val parentData = viewModel.initData(filteredType)
                adapter.setData(parentData)
            }
        }
    }

    private fun totalMoney() {
        val db = DataManager.getDataBase(requireContext())
        db.addNewDao().getAll().observe(this) { list ->
            val type = list.filter { it.type == "loan" }
            val totalMoneyLoan = type.filter { it.nameTypeCategory == "Loan" }.sumOf { it.amount }
            val totalMoneyBorrow =
                type.filter { it.nameTypeCategory == "Borrow" }.sumOf { it.amount }

            val formatMoneyLoan = formatMoney(totalMoneyLoan)
            binding.txtMoneyLoan.text = "$formatMoneyLoan đ"

            val formatMoneyBorrow = formatMoney(totalMoneyBorrow)
            binding.txtMoneyBorrow.text = "$formatMoneyBorrow đ"
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

        viewModel.loanList.observe(viewLifecycleOwner) { loanEntities ->
            val filtered = loanEntities.filter { item ->
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