package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.BottomSheetBudgetBinding
import com.example.moneymanagement.presentation.database.AppDatabase
import com.example.moneymanagement.presentation.database.DataManager
import com.example.moneymanagement.presentation.view.adapter.OnClickItemAddNew
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BudgetBottomSheet() : BottomSheetDialogFragment() {

    private var binding: BottomSheetBudgetBinding? = null

    private lateinit var listener: OnClickItemAddNew

    private var selectionBudget: String = "None"

    private var img: Int = R.drawable.ic_none

    fun setOnButtonClickListener(listener: OnClickItemAddNew) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnNone?.setOnClickListener {
            selectionBudget = "None"
             it.setBackgroundResource(R.drawable.bg_budget_selection)
            img = R.drawable.ic_none
        }
        binding?.btnNecessities?.setOnClickListener {
            it.setBackgroundResource(R.drawable.bg_budget_selection)
            selectionBudget = "Necessities"
            img = R.drawable.ic_necessities
        }

        binding?.btnEducation?.setOnClickListener {
            it.setBackgroundResource(R.drawable.bg_budget_selection)
            selectionBudget = "Education"
            img = R.drawable.ic_education_budget
        }

        binding?.btnSaving?.setOnClickListener {
            it.setBackgroundResource(R.drawable.bg_budget_selection)
            selectionBudget = "Saving"
            img = R.drawable.ic_saving_budget
        }
        binding?.btnPlay?.setOnClickListener {
            it.setBackgroundResource(R.drawable.bg_budget_selection)
            selectionBudget = "Play"
            img = R.drawable.ic_play_budget
        }
        binding?.btnInvestment?.setOnClickListener {
            it.setBackgroundResource(R.drawable.bg_budget_selection)
            selectionBudget = "Investment"
            img = R.drawable.ic_investment_budget
        }
        binding?.btnGive?.setOnClickListener {
            it.setBackgroundResource(R.drawable.bg_budget_selection)
            selectionBudget = "Give"
            img = R.drawable.ic_give_budget
        }
        binding?.btnCancel?.setOnClickListener { dismiss() }

        binding?.btnSave?.setOnClickListener {
            listener.onClickListenerBudget(selectionBudget, img)
            dismiss()
        }

        binding!!.btnCancel.setOnClickListener { dismiss() }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBudgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}