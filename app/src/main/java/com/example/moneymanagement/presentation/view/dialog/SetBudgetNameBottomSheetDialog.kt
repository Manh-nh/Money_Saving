package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.moneymanagement.databinding.BottomSheetAddBudgetBinding
import com.example.moneymanagement.presentation.view.adapter.OnAddBudgerListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetBudgetNameBottomSheetDialog : BottomSheetDialogFragment() {

    private var binding: BottomSheetAddBudgetBinding? = null
    private lateinit var listener: OnAddBudgerListener

    fun setListener(listener: OnAddBudgerListener) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnCancel?.setOnClickListener { dismiss() }

        binding?.btnSave?.setOnClickListener {
            addBudget()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetAddBudgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    fun addBudget() {
        val setMoney = binding?.edtSetMoney?.text.toString()
        val setNameBudget = binding?.edtNameBudget?.text.toString()
        var money = 0

        if (setNameBudget.isEmpty()) {
            Toast.makeText(requireContext(), "name budget is empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (setMoney.toInt() < 1000) {
            Toast.makeText(requireContext(), "money is less than 1000", Toast.LENGTH_SHORT).show()
            return
        }

        money = setMoney.toInt()
        listener.onAddBudgetListener(money, setNameBudget)

        dismiss()
    }

}