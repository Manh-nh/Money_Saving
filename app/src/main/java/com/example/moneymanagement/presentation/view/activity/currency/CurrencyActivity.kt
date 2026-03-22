package com.example.moneymanagement.presentation.view.activity.currency

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.moneymanagement.databinding.ActivityCurrencyBinding
import com.example.moneymanagement.presentation.database.api.RetrofitClient
import com.example.moneymanagement.presentation.database.model.CurrencyItem
import com.example.moneymanagement.presentation.database.model.CurrencyResponse
import com.example.moneymanagement.presentation.view.base.BaseActivity
import com.example.moneymanagement.presentation.view.dialog.CurrencyBottomSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CurrencyActivity : BaseActivity<ActivityCurrencyBinding>(ActivityCurrencyBinding::inflate) {

    private var fromCurrencyCode = "USD"
    private var toCurrencyCode = "VND"

    override fun initializeEvents() {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnFromCurrency.setOnClickListener {
            showCurrencySelector(fromCurrencyCode) { currency ->
                fromCurrencyCode = currency.code
                binding.tvFromCurrency.text = currency.code
            }
        }

        binding.btnToCurrency.setOnClickListener {
            showCurrencySelector(toCurrencyCode) { currency ->
                toCurrencyCode = currency.code
                binding.tvToCurrency.text = currency.code
            }
        }

        binding.ivSwap.setOnClickListener {
            val tempCode = fromCurrencyCode
            fromCurrencyCode = toCurrencyCode
            toCurrencyCode = tempCode

            binding.tvFromCurrency.text = fromCurrencyCode
            binding.tvToCurrency.text = toCurrencyCode

            // Rotation animation
            binding.ivSwap.animate().rotationBy(180f).setDuration(300).start()
        }

        binding.btnConvert.setOnClickListener {
            performConversion()
        }
    }

    override fun initializeData() {
        binding.tvFromCurrency.text = fromCurrencyCode
        binding.tvToCurrency.text = toCurrencyCode
    }

    private fun showCurrencySelector(selectedCode: String, onSelected: (CurrencyItem) -> Unit) {
        val bottomSheet = CurrencyBottomSheet(selectedCode, onSelected)
        bottomSheet.show(supportFragmentManager, "CurrencyBottomSheet")
    }

    private fun performConversion() {
        val amountStr = binding.edtAmount.text.toString()
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull() ?: 0.0

        binding.btnConvert.isEnabled = false
        binding.btnConvert.text = "Converting..."
        
        RetrofitClient.retrofitCurrency.getLatestRates(fromCurrencyCode).enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                binding.btnConvert.isEnabled = true
                binding.btnConvert.text = "Convert Now"
                if (response.isSuccessful) {
                    val rates = response.body()?.conversionRates
                    val rate = rates?.get(toCurrencyCode)
                    if (rate != null) {
                        val result = amount * rate
                        showResult(amount, fromCurrencyCode, result, toCurrencyCode, rate)
                    } else {
                        Toast.makeText(this@CurrencyActivity, "Rate not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CurrencyActivity, "Failed to fetch rates", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                binding.btnConvert.isEnabled = true
                binding.btnConvert.text = "Convert Now"
                Toast.makeText(this@CurrencyActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showResult(amount: Double, from: String, result: Double, to: String, rate: Double) {
        binding.layoutResult.visibility = View.VISIBLE
        binding.layoutResult.alpha = 0f
        binding.layoutResult.translationY = 50f
        binding.layoutResult.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(400)
            .start()

        binding.tvConvertedValue.text = String.format(Locale.getDefault(), "%.2f %s", result, to)
        binding.tvRateInfo.text = String.format(Locale.getDefault(), "1 %s = %.4f %s", from, rate, to)
    }
}