package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.moneymanagement.databinding.BottomSheetSetMoneyBudgetBinding
import com.example.moneymanagement.presentation.view.adapter.OnBudgetUpdatedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetMoneyBudgetBottomSheet : BottomSheetDialogFragment() {

    private var binding: BottomSheetSetMoneyBudgetBinding? = null
    private lateinit var listener: OnBudgetUpdatedListener

    fun setListener(listener: OnBudgetUpdatedListener) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.btnSave?.setOnClickListener {
            val setMoney = binding?.edtSetMoney?.text.toString()

            if (setMoney.isEmpty()) {
                Toast.makeText(requireContext(), "Money not is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = setMoney.toInt()

            if (amount < 0) {
                Toast.makeText(requireContext(), "Money not is less than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            listener.onMoneySet(amount)
            dismiss()
        }



        binding?.btnCancel?.setOnClickListener { dismiss() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSetMoneyBudgetBinding.inflate(inflater, container, false)
        return binding?.root

    }

}