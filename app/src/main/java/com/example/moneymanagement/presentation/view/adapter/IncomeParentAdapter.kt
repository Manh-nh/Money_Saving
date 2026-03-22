package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemHistoryParentBinding
import com.example.moneymanagement.presentation.database.model.TransactionParent

class IncomeParentAdapter(
    private val onClickListner: OnClickItemTransaction,
    private var data: List<TransactionParent>) :
    RecyclerView.Adapter<IncomeParentAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    fun setData(data: List<TransactionParent>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryParentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(val binding: ItemHistoryParentBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindView(itemParent: TransactionParent) {
            binding.txtDateParent.text = itemParent.date

            val parentAdapter =IncomeChildAdapter(
                    onClickListner,
                    itemParent.date,
                    itemParent.child
                )
            binding.listHistoryChild.adapter = parentAdapter

            binding.listHistoryChild.setRecycledViewPool(viewPool)


        }
    }
}