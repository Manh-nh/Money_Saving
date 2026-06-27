package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemBudgetBinding
import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity
import java.text.DecimalFormat

class BudgetDetailAdapter(
    private var items: List<BudgetEntity>,
    private var totalBudget: Int,
    private var onClickUpdateMoney: OnClickListenerUpdateMoney
) : RecyclerView.Adapter<BudgetDetailAdapter.ViewHolder>() {

    fun setData(newItems: List<BudgetEntity>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    fun setTotalBudget(totalBudget: Int) {
        this.totalBudget = totalBudget
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
            binding.jarView.bind(
                budgetDetail,
                totalBudget,
                onEditClick = {
                    onClickUpdateMoney.getJar(budgetDetail.id, budgetDetail.moneyBudget, budgetDetail.nameBudget)
                },
                onItemClick = {
                    onClickUpdateMoney.onItemClick(budgetDetail)
                }
            )
        }
    }
}
