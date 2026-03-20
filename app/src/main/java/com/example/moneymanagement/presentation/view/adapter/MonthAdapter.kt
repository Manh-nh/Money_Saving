package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemMonthBinding
import com.example.moneymanagement.presentation.database.model.MonthModel

class MonthAdapter(
    private val data: List<MonthModel>,
    val onClickListener: OnClickItemMonth
) : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMonthBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindView(data[position])
        holder.binding.txtMonth.isSelected = (position == selectedPosition)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.txtMonth.setOnClickListener {
                if(absoluteAdapterPosition != RecyclerView.NO_POSITION){
                    val previousPosition = selectedPosition
                    selectedPosition = absoluteAdapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    onClickListener.onClickListenerCategory(data[absoluteAdapterPosition], absoluteAdapterPosition + 1)
                }

            }
        }

        fun bindView(monthModel: MonthModel) {
            binding.txtMonth.text = monthModel.month
        }

    }

}