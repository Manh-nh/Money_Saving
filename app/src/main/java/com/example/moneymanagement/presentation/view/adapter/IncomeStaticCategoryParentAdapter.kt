package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemStaticCategoryParentBinding
import com.example.moneymanagement.presentation.database.model.StaticCategoryParentModel

class IncomeStaticCategoryParentAdapter(
    private var data: List<StaticCategoryParentModel>

) : RecyclerView.Adapter<IncomeStaticCategoryParentAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    fun setData(newData :List<StaticCategoryParentModel> ){
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStaticCategoryParentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
       holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(val binding: ItemStaticCategoryParentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item : StaticCategoryParentModel) {
            binding.date.text = item.date
            val adapter =IncomeStaticCategoryChildAdapter(
                    item.list
                )
            binding.lstStaticCategory.adapter = adapter

            binding.lstStaticCategory.setRecycledViewPool(viewPool)
        }
    }

}