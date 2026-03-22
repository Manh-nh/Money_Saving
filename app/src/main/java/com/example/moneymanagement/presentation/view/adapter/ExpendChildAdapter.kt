package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemHistoryChildBinding
import com.example.moneymanagement.presentation.database.model.TransactionChild
import java.text.DecimalFormat

class ExpendChildAdapter(
    private val data: List<TransactionChild>,
    private val date: String,
    private val itemClick: OnClickItemTransaction
) :
    RecyclerView.Adapter<ExpendChildAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryChildBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val binding: ItemHistoryChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(itemChild: TransactionChild) {
            binding.imgCategory.setImageResource(itemChild.imgCategory)
            binding.txtCategory.text = itemChild.nameCategory
            binding.txtTime.text = itemChild.time
            binding.txtContentCategory.text = itemChild.note
            binding.txtPrice.text = " - " + itemChild.expendPrice.toString() + " vnđ"
            binding.txtPrice.text = " - " + formatMoney(itemChild.expendPrice) + " vnđ"

            binding.root.setOnClickListener {
                itemClick.onItemClick(itemChild, date)
            }

        }

        private fun formatMoney(amount: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(amount).replace(",", ".")
        }

    }


}