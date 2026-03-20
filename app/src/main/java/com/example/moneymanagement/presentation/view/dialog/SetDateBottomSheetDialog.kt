package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneymanagement.databinding.BottomSheetSetDateBinding
import com.example.moneymanagement.presentation.view.adapter.OnClickItemAddNew
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class SetDateBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var listener: OnClickItemAddNew
    private var binding: BottomSheetSetDateBinding? = null
    private var calendar = Calendar.getInstance()
    private var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    private var month: Int = calendar.get(Calendar.MONTH) + 1
    private var year: Int = calendar.get(Calendar.YEAR)


    fun setOnButtonClickListener(listener: OnClickItemAddNew) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDateNow()
        binding!!.numberPickerYear.setFormatter { number ->
            String.format("%02d", number)
        }

        binding?.numberPickerDay?.setOnValueChangedListener { picker, oldVal, newVal ->
            day = newVal
        }

        binding?.numberPickerMonth?.setOnValueChangedListener { picker, oldVal, newVal ->
            month = newVal
        }

        binding?.numberPickerYear?.setOnValueChangedListener { picker, oldVal, newVal ->
            year = newVal
        }

        binding?.btnSave?.setOnClickListener {
            listener.onCLickListenerDate(day, month, year)
            dismiss()
        }

        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSetDateBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun getDateNow() {

        binding?.numberPickerDay?.value = day
        binding?.numberPickerMonth?.value = month
        binding?.numberPickerYear?.value = year
    }

}