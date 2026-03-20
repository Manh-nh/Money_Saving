package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemBudgetBinding
import com.example.moneymanagement.presentation.database.BudgetEntity
import java.text.DecimalFormat

class BudgetDetailAdapter(
    private var items: List<BudgetEntity>,
    private var onClickUpdateMoney: OnClickListenerUpdateMoney
) : RecyclerView.Adapter<BudgetDetailAdapter.ViewHolder>() {

    fun setData(newItems: List<BudgetEntity>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemBudgetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(budgetDetail: BudgetEntity) {

            val formatMoney = formatMoney(budgetDetail.moneyBudget)

            binding.txtNameBudget.text = budgetDetail.nameBudget
            binding.txtMoney.text = "$formatMoney"
            binding.imgBudget.setBackgroundResource(budgetDetail.imgBudget)

            val moneyJar = binding.txtMoney.text.toString()
            val formatMoneyJar = moneyJar.replace(".", "")
            val nameJar = binding.txtNameBudget.text.toString()

            binding.btnUpdateMoney.setOnClickListener {
                onClickUpdateMoney.getJar(budgetDetail.id, formatMoneyJar.toInt(), nameJar)
            }

            binding.root.setOnClickListener {
                onClickUpdateMoney.onItemClick(budgetDetail)
            }

        }

        private fun formatMoney(amount: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(amount).replace(",", ".")
        }


    }
}
