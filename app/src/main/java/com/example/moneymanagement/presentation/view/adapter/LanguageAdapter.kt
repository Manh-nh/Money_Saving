package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemLanguageBinding
import com.example.moneymanagement.presentation.database.model.LanguageModel
import com.example.moneymanagement.R

class LanguageAdapter(private var data: List<LanguageModel>,
        private val onClickItem : (LanguageModel) -> Unit
) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    fun setSource(list: List<LanguageModel>) {
        this.data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLanguageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        postion: Int
    ) {
        holder.bindView(data[postion], postion)
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(model: LanguageModel, position: Int) {
            binding.imgLanguage.setImageResource(model.imgFlag)
            binding.nameCountry.text = model.nameCountry

            if (model.isSelected) {
                binding.btnChoice.setImageResource(R.drawable.ic_choice)
                binding.root.setBackgroundResource(R.drawable.bg_language_item) // Or a selected background if available
            } else {
                binding.btnChoice.setImageResource(R.drawable.ic_not_choice)
                binding.root.setBackgroundResource(0) // Remove background or use a default one
            }

            binding.root.setOnClickListener {
                data.forEach { it.isSelected = false }
                model.isSelected = true
                notifyDataSetChanged()
                onClickItem(model)
            }

        }


    }
}