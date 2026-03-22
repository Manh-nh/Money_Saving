package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemHistoryChildBinding
import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
import java.text.DecimalFormat

class JarTransactionAdapter(
    private var data: List<AddNewEntity> = emptyList()
) : RecyclerView.Adapter<JarTransactionAdapter.ViewHolder>() {

    fun setData(newData: List<AddNewEntity>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryChildBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemHistoryChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: AddNewEntity) {
            binding.imgCategory.setImageResource(item.imgTypeCategory)
            binding.txtCategory.text = item.nameTypeCategory
            binding.txtTime.text = item.time
            binding.txtContentCategory.text = item.note
            
            val price = when (item.type) {
                "expend" -> "- ${formatMoney(item.amount)} đ"
                "income" -> "+ ${formatMoney(item.amount)} đ"
                "loan" -> {
                    if (item.nameTypeCategory == "Loan") "+ ${formatMoney(item.amount)} đ"
                    else "- ${formatMoney(item.amount)} đ"
                }
                else -> "${formatMoney(item.amount)} đ"
            }
            
            binding.txtPrice.text = price
            
            // Set color based on type
            val color = if (price.contains("-")) {
                android.graphics.Color.parseColor("#F44336") // Red
            } else {
                android.graphics.Color.parseColor("#4CAF50") // Green
            }
            binding.txtPrice.setTextColor(color)
        }

        private fun formatMoney(amount: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(amount).replace(",", ".")
        }
    }
}
