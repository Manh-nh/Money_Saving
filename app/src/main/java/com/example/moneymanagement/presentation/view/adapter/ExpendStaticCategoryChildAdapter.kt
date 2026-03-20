package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemStaticCategoryChildBinding
import com.example.moneymanagement.presentation.database.model.StaticCategoryChildModel

class ExpendStaticCategoryChildAdapter(

    val data: List<StaticCategoryChildModel>

) : RecyclerView.Adapter<ExpendStaticCategoryChildAdapter.ViewHolder>() {

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

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val binding: ItemStaticCategoryChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(itemChild: StaticCategoryChildModel) {
            binding.imgCategory.setImageResource(itemChild.imgCategory)
            binding.txtCategory.text = itemChild.nameCategory
            binding.txtTotalMoney.text = itemChild.totalMoneyCategory
            binding.pbCategory.progress = itemChild.progress
        }

    }

}