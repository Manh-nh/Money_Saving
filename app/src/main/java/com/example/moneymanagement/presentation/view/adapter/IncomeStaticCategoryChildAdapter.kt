package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemStaticCategoryChildBinding
import com.example.moneymanagement.presentation.database.model.StaticCategoryChildModel

class IncomeStaticCategoryChildAdapter(

    var data: List<StaticCategoryChildModel>

) : RecyclerView.Adapter<IncomeStaticCategoryChildAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStaticCategoryChildBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(val binding: ItemStaticCategoryChildBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindView(item: StaticCategoryChildModel) {
            binding.imgCategory.setImageResource(item.imgCategory)
            binding.txtCategory.text = item.nameCategory
            binding.pbCategory.progress = item.progress
            binding.txtTotalMoney.text = item.totalMoneyCategory
        }

    }
}