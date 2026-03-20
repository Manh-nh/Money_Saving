package com.example.moneymanagement.presentation.view.addnewloanfragment

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.databinding.FragmentAddNewLoanBinding
import com.example.moneymanagement.presentation.database.AddNewEntity
import com.example.moneymanagement.presentation.database.model.CategoryModel
import com.example.moneymanagement.presentation.view.dialog.BudgetBottomSheet
import com.example.moneymanagement.presentation.view.adapter.AddNewCategoryAdapter
import com.example.moneymanagement.presentation.view.adapter.OnClickItemAddNew
import com.example.moneymanagement.presentation.view.addnewactivity.AddNewViewModel
import com.example.moneymanagement.presentation.view.base.BaseFragment
import com.example.moneymanagement.presentation.view.dialog.SetDateBottomSheetDialog
import com.example.moneymanagement.presentation.view.dialog.SetTimeBottomSheetDialog
import java.util.Calendar

class AddNewLoanFragment :
    BaseFragment<FragmentAddNewLoanBinding>(FragmentAddNewLoanBinding::inflate),
    OnClickItemAddNew {

    private lateinit var adapter: AddNewCategoryAdapter
    private lateinit var data: List<CategoryModel>
    private lateinit var viewModel: AddNewLoanViewModel
    private var calendar = Calendar.getInstance()
    private var nameBudget: String = ""
    private var imgBudget: Int = 0
    private var amountMoney: Int = 0
    private var nameCategory: String = "None"
    private var imgCategory: Int = 0
    private lateinit var note: String
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var type: String

    private val addNew: AddNewViewModel by activityViewModels()


    override fun initializeComponent() {
        viewModel = ViewModelProvider(this)[AddNewLoanViewModel::class.java]

        addNew.typeAddNew.observe(viewLifecycleOwner) {
            type = it
        }

        data = viewModel.initData()
        adapter = AddNewCategoryAdapter(data, this)
        binding.lstCategory.adapter = adapter
    }

    override fun initializeEvents() {
        binding.btnBudget.setOnClickListener {
            showBudgetBottomSheet()
        }
    }

    override fun initializeData() {
        binding.btnBudget.setOnClickListener { showBudgetBottomSheet() }
        binding.btnTime.setOnClickListener { setTimeBottomSheet() }
        binding.btnCalender.setOnClickListener { setDateBottomSheet() }
    }

    override fun bindView() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        time = "$hour:$minute"
        date = "$day/$month/$year"

        binding.txtTime.text = "$hour:$minute"
        binding.txtDate.text = "$day/$month/$year"
    }

    private fun showBudgetBottomSheet() {
        val bottomSheet = BudgetBottomSheet()
        bottomSheet.setOnButtonClickListener(this)
        bottomSheet.show(requireActivity().supportFragmentManager, "budget Bottom Sheet Dialog")
    }

    private fun setTimeBottomSheet() {
        val bottomSheet = SetTimeBottomSheetDialog()
        bottomSheet.setOnButtonClickListener(this)
        bottomSheet.show(requireActivity().supportFragmentManager, "Set Time Bottom Sheet Dialog")
    }

    private fun setDateBottomSheet() {
        val bottomSheet = SetDateBottomSheetDialog()
        bottomSheet.setOnButtonClickListener(this)
        bottomSheet.show(requireActivity().supportFragmentManager, "Set Date Bottom Sheet Dialog")
    }

    override fun onClickListenerBudget(nameBudget: String, imgBudget : Int) {
        binding.txtBudgetSelection.text = nameBudget
        this.nameBudget = nameBudget
        this.imgBudget = imgBudget
    }

    override fun onCLickListenerDate(
        day: Int,
        month: Int,
        year: Int
    ) {
        val date = "$day/$month/$year"
        binding.txtDate.text = date
    }

    override fun onClickListerTime(minute: Int, hour: Int) {
        val time = "$hour:$minute"
        binding.txtTime.text = time
    }

    override fun onClickListenerCategory(
        item: CategoryModel,
        position: Int,
    ) {
        nameCategory = item.typeCategory
        imgCategory = item.imgTypeCategory
    }


    fun senDataLoan() {
        val getMoney = binding.edtSetMoney.text.toString()
        amountMoney = getMoney.toIntOrNull() ?: -1
        note = binding.edtNote.text.toString()

        if (amountMoney < 1000) {
            Toast.makeText(requireContext(), "Money must not be less than 1000", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (imgCategory == 0) {
            Toast.makeText(requireContext(), "please choose Category", Toast.LENGTH_SHORT).show()
            return
        }

        if (nameBudget.isEmpty()) {
            Toast.makeText(requireContext(), "Please choose Budget", Toast.LENGTH_SHORT).show()
            return
        }


        val entity = AddNewEntity(
            id = 0,
            type,
            amountMoney,
            nameCategory,
            imgCategory,
            imgBudget,
            nameBudget,
            note,
            date,
            time
        )
        addNew.setDataList(listOf(entity))
    }

}