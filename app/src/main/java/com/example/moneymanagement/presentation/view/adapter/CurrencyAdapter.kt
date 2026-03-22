package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemCurrencyBinding
import com.example.moneymanagement.presentation.database.model.CurrencyItem

class CurrencyAdapter(
    private var currencies: List<CurrencyItem>,
    private val selectedCode: String,
    private val onCurrencySelected: (CurrencyItem) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private var filteredCurrencies = currencies

    fun filter(query: String) {
        filteredCurrencies = if (query.isEmpty()) {
            currencies
        } else {
            currencies.filter {
                it.code.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = filteredCurrencies[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int = filteredCurrencies.size

    inner class ViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: CurrencyItem) {
            binding.tvCurrencyCode.text = currency.code
            binding.tvCurrencyName.text = currency.name
            binding.ivCheck.visibility = if (currency.code == selectedCode) View.VISIBLE else View.GONE
            binding.root.setOnClickListener { onCurrencySelected(currency) }
        }
    }
}
