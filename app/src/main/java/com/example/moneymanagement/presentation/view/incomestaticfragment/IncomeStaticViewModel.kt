package com.example.moneymanagement.presentation.view.incomestaticfragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.presentation.database.AppDatabase
import com.example.moneymanagement.presentation.database.model.StaticCategoryChildModel
import com.example.moneymanagement.presentation.database.model.StaticCategoryParentModel
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.component1
import kotlin.collections.component2

class IncomeStaticViewModel : ViewModel() {
    private val _pieChartData = MutableLiveData<List<PieEntry>>()
    val pieChartData: LiveData<List<PieEntry>> get() = _pieChartData

    private val _barChart = MutableLiveData<List<BarEntry>>()
    val barChar: LiveData<List<BarEntry>> get() = _barChart

    private val _dataStatic = MutableLiveData<List<StaticCategoryParentModel>>()
    val dataStatic : LiveData<List<StaticCategoryParentModel>> get() = _dataStatic

    private lateinit var db: AppDatabase

    fun setAppDataBase(db: AppDatabase) {
        this.db = db
    }

    fun getDataPieChart(owner: LifecycleOwner) {

        db.addNewDao().getAll().observe(owner) { expendList ->

            val groupType = expendList.filter { it.type == "income" }
            val total = groupType.sumOf { it.amount }

            val group = groupType.groupBy { it.nameTypeCategory }
                .map { (nameTypeCategory, money) ->
                    val totalMoney = money.sumOf { it.amount }
                    val per = (totalMoney.toDouble() / total.toDouble()) * 100

                    PieEntry(per.toFloat(), nameTypeCategory)
                }
            _pieChartData.postValue(group)
        }

    }

    fun getDataBarChart(owner: LifecycleOwner) {

        db.addNewDao().getAll().observe(owner) { expendList ->

            val groupType = expendList.filter { it.type == "income" }
            val grouped = groupType.groupBy { it.nameTypeCategory }

            val barEntries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()

            grouped.entries.forEachIndexed { index, entry ->
                val totalMoney = entry.value.sumOf { it.amount }
                barEntries.add(BarEntry(index.toFloat(), totalMoney.toFloat()))
                labels.add(entry.key)
            }
            _barChart.postValue(barEntries)
        }
    }

    fun getDataCategoryStatic(own: LifecycleOwner) {
        db.addNewDao().getAll().observe(own) { entity ->
            val groupTypeNameCategory = entity.filter { it.type == "income" }

            val totalMoneyIncome = groupTypeNameCategory.sumOf { it.amount }

            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")

            val groupMonth = groupTypeNameCategory.groupBy { date ->

                val date = LocalDate.parse(date.date, formatter)
                "${date.monthValue}/${date.year}"
            }

            val parentNameCategory = groupMonth.map { (dateMonth, items) ->

                val childList = items.groupBy { it.nameTypeCategory }
                    .map { (categoryName, list) ->
                        val totalMoney = list.sumOf { it.amount }

                        val processValue : Float = (totalMoney * 100f) / totalMoneyIncome

                        StaticCategoryChildModel(
                            imgCategory = list.first().imgTypeCategory,
                            nameCategory = categoryName,
                            totalMoneyCategory = "$totalMoney đ",
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


    }


}