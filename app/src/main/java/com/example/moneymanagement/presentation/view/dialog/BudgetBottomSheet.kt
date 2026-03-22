package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.BottomSheetBudgetBinding
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.view.adapter.BudgetSelectionAdapter
import com.example.moneymanagement.presentation.view.adapter.OnClickItemAddNew
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BudgetBottomSheet : BottomSheetDialogFragment() {

    private var binding: BottomSheetBudgetBinding? = null
    private lateinit var listener: OnClickItemAddNew
    private var selectionBudget: String = "None"
    private var img: Int = R.drawable.ic_none
    private lateinit var adapter: BudgetSelectionAdapter

    fun setOnButtonClickListener(listener: OnClickItemAddNew) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBudgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appDatabase = DataManager.getDataBase(requireContext())
        
        adapter = BudgetSelectionAdapter(emptyList()) { name, icon ->
            selectionBudget = name
            img = icon
            binding?.btnNone?.setBackgroundResource(0)
        }
        binding?.lstBudgetSelection?.adapter = adapter

        appDatabase.addBudget().getBudgetDetail().observe(viewLifecycleOwner) { jars ->
            adapter.setData(jars)
        }

        binding?.btnNone?.setOnClickListener {
            selectionBudget = "None"
            img = R.drawable.ic_none
            adapter.clearSelection()
            it.setBackgroundResource(R.drawable.bg_budget_selection)
        }

        binding?.btnSave?.setOnClickListener {
            if (selectionBudget != "None") {
                listener.onClickListenerBudget(selectionBudget, img)
                dismiss()
            } else {
                android.widget.Toast.makeText(requireContext(), "Please select a jar", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        binding?.btnCancel?.setOnClickListener { dismiss() }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}