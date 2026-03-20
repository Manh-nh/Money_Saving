package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemHistoryParentBinding
import com.example.moneymanagement.presentation.database.model.TransactionParent

class ExpendParentAdapter(
    private val itemClick: OnClickItemTransaction,
    private var data: List<TransactionParent>

) :RecyclerView.Adapter<ExpendParentAdapter.ExpendParentViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    fun setData(newData: List<TransactionParent>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpendParentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryParentBinding.inflate(inflater, parent, false)
        return ExpendParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpendParentViewHolder, position: Int) {
        val itemParent = data[position]
        holder.bindView(itemParent)
    }

    override fun getItemCount(): Int = data.size

    inner class ExpendParentViewHolder(private val binding: ItemHistoryParentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(itemParent: TransactionParent) {
            binding.txtDateParent.text = itemParent.date

            val parentAdapter = ExpendChildAdapter(itemParent.child, itemParent.date,itemClick,  )
            binding.listHistoryChild.adapter = parentAdapter

            binding.listHistoryChild.setRecycledViewPool(viewPool)
        }

    }


}