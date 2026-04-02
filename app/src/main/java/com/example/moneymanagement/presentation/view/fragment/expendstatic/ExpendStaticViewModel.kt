package com.example.moneymanagement.presentation.view.fragment.expendstatic

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

class ExpendStaticViewModel : ViewModel() {

    private val _pieChartData = MutableLiveData<List<PieEntry>>()
    val pieChartData: LiveData<List<PieEntry>> get() = _pieChartData

    private val _barChart = MutableLiveData<List<BarEntry>>()
    val barChar: LiveData<List<BarEntry>> get() = _barChart

    private lateinit var db: AppDatabase

    private val _dataStatic = MutableLiveData<List<StaticCategoryParentModel>>()
    val dataStatic: LiveData<List<StaticCategoryParentModel>> get() = _dataStatic

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

        val groupType = cachedList.filter { it.type == "expend" }
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
            .mapNotNull { (nameTypeCategory, items) ->
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
        val totalExpends = groupType.sumOf { it.amount }
        val groupedByMonth = groupType.groupBy { item ->
            val date = LocalDate.parse(item.date, formatter)
            "${date.monthValue}/${date.year}"
        }

        val bindView = groupedByMonth.map { (monthYear, items) ->
            val childList = items.groupBy { it.nameTypeCategory }
                .map { (categoryName, list) ->
                    val totalCategory = list.sumOf { it.amount }
                    val processValue: Float = if (totalExpends > 0) (totalCategory * 100f) / totalExpends else 0f
                    StaticCategoryChildModel(
                        imgCategory = list.first().imgTypeCategory,
                        nameCategory = categoryName,
                        totalMoneyCategory = "${formatAmount(totalCategory)} đ",
                        progress = processValue.toInt()
                    )
                }
            StaticCategoryParentModel(
                date = monthYear,
                list = childList
            )
        }
        _dataStatic.postValue(bindView)
    }

    private fun formatAmount(amount: Int): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.groupingSeparator = '.'
        val df = DecimalFormat("#,###", symbols)
        return df.format(amount)
    }
}