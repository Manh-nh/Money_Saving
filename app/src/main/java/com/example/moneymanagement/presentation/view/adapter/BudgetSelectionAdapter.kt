package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.ItemBudgetSelectionBinding
import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity

class BudgetSelectionAdapter(
    private var items: List<BudgetEntity>,
    private val onItemSelected: (String, Int) -> Unit
) : RecyclerView.Adapter<BudgetSelectionAdapter.ViewHolder>() {

    private var selectedPosition = -1

    fun setData(newItems: List<BudgetEntity>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedPosition = -1
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBudgetSelectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemBudgetSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: BudgetEntity, position: Int) {
            binding.txtNameBudget.text = budget.nameBudget
            binding.imgBudget.setImageResource(budget.imgBudget)

            if (selectedPosition == position) {
                binding.btnSelection.setBackgroundResource(R.drawable.bg_budget_selection)
            } else {
                binding.btnSelection.setBackgroundResource(R.drawable.bg_soft_input)
            }

            binding.root.setOnClickListener {
                selectedPosition = adapterPosition
                onItemSelected(budget.nameBudget, budget.imgBudget)
                notifyDataSetChanged()
            }
        }
    }
}
