package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.BottomSheetSetJarBudgetBinding
import com.example.moneymanagement.presentation.view.adapter.OnClickListenerUpdateMoney
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetMoneyJarBottomSheet(
    val money: Int,
    val jarName: String,
    val jarId: Int,

    ) : BottomSheetDialogFragment() {

    private var binding: BottomSheetSetJarBudgetBinding? = null
    private lateinit var onClickListenerUpdateMoney: OnClickListenerUpdateMoney

    fun setListener(onClickListenerUpdateMoney: OnClickListenerUpdateMoney) {
        this.onClickListenerUpdateMoney = onClickListenerUpdateMoney
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSetJarBudgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.edtSetMoney?.setText(money.toString())
        binding?.txtBudget?.text = jarName

        binding?.btnSave?.setOnClickListener {
            val getMoney = binding?.edtSetMoney?.text.toString().trim()

            if (getMoney.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter a money amount",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val parsedMoney = getMoney.toIntOrNull()
            if (parsedMoney == null || parsedMoney < 1000) {
                Toast.makeText(
                    requireContext(),
                    this.getString(R.string.the_value_must_be_1000_or_more),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            onClickListenerUpdateMoney.updateMoney(jarId, parsedMoney)
            dismiss()
        }


        binding?.btnCancel?.setOnClickListener { dismiss() }

    }


}