package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            val getMoney = binding?.edtSetMoney?.text.toString()

            if (getMoney.toInt() < 1000) {
                Toast.makeText(
                    requireContext(),
                    "The value must be 1000 or more",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }

            onClickListenerUpdateMoney.updateMoney(jarId, getMoney.toInt())
            dismiss()
        }


        binding?.btnCancel?.setOnClickListener { dismiss() }

    }


}