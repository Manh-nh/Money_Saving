package com.example.moneymanagement.presentation.view.fragment.incomestaticfragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.roomdb.AddNewEntity
import com.example.moneymanagement.presentation.database.roomdb.AppDatabase
import com.example.moneymanagement.presentation.database.model.StaticCategoryChildModel
import com.example.moneymanagement.presentation.database.model.StaticCategoryParentModel
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class IncomeStaticViewModel : ViewModel() {
    private val _pieChartData = MutableLiveData<List<PieEntry>>()
    val pieChartData: LiveData<List<PieEntry>> get() = _pieChartData

    private val _barChart = MutableLiveData<List<BarEntry>>()
    val barChar: LiveData<List<BarEntry>> get() = _barChart

    private val _dataStatic = MutableLiveData<List<StaticCategoryParentModel>>()
    val dataStatic: LiveData<List<StaticCategoryParentModel>> get() = _dataStatic

    private lateinit var db: AppDatabase

    private var selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR)

    private var cachedList: List<AddNewEntity> = emptyList()

    fun setAppDataBase(db: AppDatabase) {
        this.db = db
    }

    fun setSelectedDate(month: Int, year: Int) {
        this.selectedMonth = month
        this.selectedYear = year
        processData()
    }

    fun observeData(owner: LifecycleOwner) {
        db.addNewDao().getAll().observe(owner) { allData ->
            cachedList = allData
            processData()
        }
    }

    private fun processData() {
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")

        val groupType = cachedList.filter { it.type == "income" }
            .filter { item ->
                try {
                    val date = LocalDate.parse(item.date, formatter)
                    date.monthValue == selectedMonth && date.year == selectedYear
                } catch (e: Exception) {
                    false
                }
            }

        // Pie chart
        val total = groupType.sumOf { it.amount }
        val pieEntries = groupType.groupBy { it.nameTypeCategory }
            .map { (nameTypeCategory, items) ->
                val totalMoney = items.sumOf { it.amount }
                val per = if (total > 0) (totalMoney.toDouble() / total.toDouble()) * 100 else 0.0
                PieEntry(per.toFloat(), nameTypeCategory, items.first().imgTypeCategory)
            }
        _pieChartData.postValue(pieEntries)

        // Bar chart
        val grouped = groupType.groupBy { it.nameTypeCategory }
        val barEntries = ArrayList<BarEntry>()
        grouped.entries.forEachIndexed { index, entry ->
            val totalMoney = entry.value.sumOf { it.amount }
            barEntries.add(BarEntry(index.toFloat(), totalMoney.toFloat()))
        }
        _barChart.postValue(barEntries)

        // Category static
        val totalMoneyIncome = groupType.sumOf { it.amount }
        val groupMonth = groupType.groupBy { date ->
            val d = LocalDate.parse(date.date, formatter)
            "${d.monthValue}/${d.year}"
        }

        val parentNameCategory = groupMonth.map { (dateMonth, items) ->
            val childList = items.groupBy { it.nameTypeCategory }
                .map { (categoryName, list) ->
                    val totalMoney = list.sumOf { it.amount }
                    val processValue: Float = if (totalMoneyIncome > 0) (totalMoney * 100f) / totalMoneyIncome else 0f
                    StaticCategoryChildModel(
                        imgCategory = list.first().imgTypeCategory,
                        nameCategory = categoryName,
                        totalMoneyCategory = "${formatAmount(totalMoney)} đ",
                        progress = processValue.toInt()
                    )
                }
            StaticCategoryParentModel(
                date = dateMonth,
                list = childList
            )
        }
        _dataStatic.postValue(parentNameCategory)
    }

    private fun formatAmount(amount: Int): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.groupingSeparator = '.'
        val df = DecimalFormat("#,###", symbols)
        return df.format(amount)
    }
}