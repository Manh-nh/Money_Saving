package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneymanagement.databinding.BottomSheetSetTimeBinding
import com.example.moneymanagement.presentation.view.adapter.OnClickItemAddNew
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class SetTimeBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var listener: OnClickItemAddNew
    private var binding: BottomSheetSetTimeBinding? = null
    private var calendar = Calendar.getInstance()
    private var minute: Int = calendar.get(Calendar.MINUTE)
    private var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)

    fun setOnButtonClickListener(listener: OnClickItemAddNew) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTimeNow()
        binding?.numberPickerMinute?.setOnValueChangedListener { picker, oldVal, newVal ->
            minute = newVal
        }

        binding?.numberPickerHour?.setOnValueChangedListener { picker, oldVal, newVal ->
            hour = newVal
        }

        binding?.btnSave?.setOnClickListener {
            listener.onClickListerTime(minute, hour)
            dismiss()
        }

        binding?.btnCancel?.setOnClickListener { dismiss() }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSetTimeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun getTimeNow() {
        binding?.numberPickerMinute?.value = minute
        binding?.numberPickerHour?.value = hour
    }

}