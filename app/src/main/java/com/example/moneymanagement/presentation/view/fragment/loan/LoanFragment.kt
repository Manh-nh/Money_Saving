package com.example.moneymanagement.presentation.view.fragment.loan

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.FragmentLoanBinding
import com.example.moneymanagement.presentation.Utils
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.database.model.TransactionChild
import com.example.moneymanagement.presentation.database.model.TransactionParent
import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
import com.example.moneymanagement.presentation.view.activity.addnew.AddNewActivity
import com.example.moneymanagement.presentation.view.adapter.LoanParentAdapter
import com.example.moneymanagement.presentation.view.adapter.OnClickItemTransaction
import com.example.moneymanagement.presentation.view.base.BaseFragment
import com.example.moneymanagement.presentation.view.activity.home.HomeViewModel
import com.example.moneymanagement.presentation.view.activity.transactions.TransactionsActivity
import com.google.gson.Gson
import java.text.DecimalFormat

class LoanFragment : BaseFragment<FragmentLoanBinding>(FragmentLoanBinding::inflate),
    OnClickItemTransaction {

    private lateinit var adapter: LoanParentAdapter
    private lateinit var data: List<TransactionParent>
    private lateinit var viewModel: LoanViewModel
    private lateinit var shareDateViewModel: HomeViewModel

    private var allTransactions: List<AddNewEntity> = emptyList()
    private var selectedMonth: Int = 0
    private var selectedYear: Int = 0

    override fun initializeComponent() {
        super.initializeComponent()

        viewModel = ViewModelProvider(this)[LoanViewModel::class.java]
        shareDateViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val appDatabase = DataManager.getDataBase(requireContext())
        viewModel.setAppDatabase(appDatabase)

        adapter = LoanParentAdapter(emptyList(), this)
        binding.lstHistoryLoan.adapter = adapter

        viewModel.loanList.observe(viewLifecycleOwner) { loanEntities ->
            allTransactions = loanEntities ?: emptyList()
            applyFilters()
        }

        shareDateViewModel.selectedMonthYear.observe(viewLifecycleOwner) { (month, year, _) ->
            selectedMonth = month
            selectedYear = year
            applyFilters()
        }

    }

    override fun initializeEvents() {
        binding.btnAddLoan.setOnClickListener {
            val intent = Intent(requireContext(), AddNewActivity::class.java)
            intent.putExtra("TAB_INDEX", 2)
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
        applyFilters()
    }

    private fun applyFilters() {
        val search = binding.edtSearch.text.toString().trim()
        val filtered = allTransactions.filter { item ->
            if (item.type != "loan") return@filter false

            if (selectedMonth > 0 && selectedYear > 0) {
                val (month, year) = extractMonthYear(item.date)
                if (month != selectedMonth || year != selectedYear) return@filter false
            }

            if (search.isNotEmpty()) {
                item.nameTypeCategory.contains(search, ignoreCase = true) ||
                (item.note?.contains(search, ignoreCase = true) ?: false)
            } else {
                true
            }
        }

        data = viewModel.initData(filtered)
        adapter.setData(data)

        binding.txtTransaction.visibility =
            if (filtered.isEmpty()) View.VISIBLE else View.GONE
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

    private fun extractMonthYear(dateString: String): Pair<Int, Int> {
        return try {
            val parts = dateString.split("/")
            if (parts.size >= 3) {
                Pair(parts[1].toInt(), parts[2].toInt())
            } else {
                Pair(0, 0)
            }
        } catch (e: Exception) {
            Pair(0, 0)
        }
    }

}