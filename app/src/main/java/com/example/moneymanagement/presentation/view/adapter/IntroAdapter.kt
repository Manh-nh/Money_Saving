package com.example.moneymanagement.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.databinding.ItemIntrolBinding
import com.example.moneymanagement.presentation.database.model.IntroModel

class IntroAdapter(var data: ArrayList<IntroModel>) :
    RecyclerView.Adapter<IntroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIntrolBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(val binding: ItemIntrolBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(intro: IntroModel) {

            binding.imgSlide.setImageResource(intro.imgIntro)
            binding.txtTitle.text = intro.titleIntro
            binding.txtContent.text = intro.contentIntro

        }

    }
}