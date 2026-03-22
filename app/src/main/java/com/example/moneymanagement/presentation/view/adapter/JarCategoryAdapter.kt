package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemJarCategoryBinding
import com.example.moneymanagement.presentation.database.model.JarCategory
import java.text.DecimalFormat

class JarCategoryAdapter(
    var data: List<JarCategory>
) : RecyclerView.Adapter<JarCategoryAdapter.ViewHolder>() {

    fun setDataBase(data: List<JarCategory>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemJarCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(val binding: ItemJarCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(budget: JarCategory) {
            val totalMoney = formatMoney(budget.totalMoney)

            binding.imgCategory.setImageResource(budget.imgCategory)
            binding.txtNameCategory.text = budget.nameCategory
            binding.txtTotalMoney.text = "$totalMoney đ"
            binding.progressBar.progress = budget.process
        }

        private fun formatMoney(amount: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(amount).replace(",", ".")
        }


    }

}