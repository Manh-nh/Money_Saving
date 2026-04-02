package com.example.moneymanagement.presentation.utils

import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
import java.util.*

object FinancialContextHelper {

    fun generateFinancialSummary(transactions: List<AddNewEntity>): String {
        if (transactions.isEmpty()) {
            return "No transactions recorded yet."
        }

        val totalIncome = transactions.filter { it.type == "Income" }.sumOf { it.amount.toLong() }
        val totalExpense = transactions.filter { it.type == "Expend" }.sumOf { it.amount.toLong() }
        val totalLoan = transactions.filter { it.type == "Loan" }.sumOf { it.amount.toLong() }

        val categorySpending = transactions.filter { it.type == "Expend" }
            .groupBy { it.nameTypeCategory }
            .mapValues { entry -> entry.value.sumOf { it.amount.toLong() } }
            .toList()
            .sortedByDescending { it.second }
            .take(5)

        val summary = StringBuilder()
        summary.append("Current Financial Status:\n")
        summary.append("- Total Income: $totalIncome\n")
        summary.append("- Total Expenses: $totalExpense\n")
        summary.append("- Total Loans: $totalLoan\n")
        summary.append("- Balance: ${totalIncome - totalExpense - totalLoan}\n\n")

        if (categorySpending.isNotEmpty()) {
            summary.append("Top Spending Categories:\n")
            categorySpending.forEach { (category, amount) ->
                summary.append("- $category: $amount\n")
            }
        }

        return summary.toString()
    }
}
