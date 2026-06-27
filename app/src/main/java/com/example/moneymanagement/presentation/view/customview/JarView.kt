package com.example.moneymanagement.presentation.view.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.moneymanagement.databinding.ViewJarBinding
import com.example.moneymanagement.presentation.database.roomdb.BudgetEntity
import com.google.android.material.card.MaterialCardView
import java.text.DecimalFormat

class JarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: ViewJarBinding

    init {
        binding = ViewJarBinding.inflate(LayoutInflater.from(context), this)
    }

    fun bind(
        budget: BudgetEntity,
        totalBudget: Int,
        onEditClick: () -> Unit,
        onItemClick: () -> Unit
    ) {
        binding.txtNameBudget.text = budget.nameBudget

        // Format money display
        val formatMoney = formatMoney(budget.moneyBudget)
        binding.txtMoney.text = "$formatMoney đ"

        // Calculate and format percentage
        val percent = if (totalBudget > 0) {
            ((budget.moneyBudget.toFloat() / totalBudget) * 100)
        } else {
            0f
        }
        val percentDisplay = String.format("%.1f", percent).replace(",", ".")
        binding.txtPer.text = "$percentDisplay%"

        // Update custom jar graphic liquid level
        binding.jarGraphicView.setPercentage(percent)

        // Bind click events
        binding.btnUpdateMoney.setOnClickListener {
            onEditClick()
        }

        binding.root.setOnClickListener {
            onItemClick()
        }
    }

    private fun formatMoney(amount: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(amount).replace(",", ".")
    }
}
