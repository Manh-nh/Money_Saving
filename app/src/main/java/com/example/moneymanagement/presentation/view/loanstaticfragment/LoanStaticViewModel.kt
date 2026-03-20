package com.example.moneymanagement.presentation.view.loanstaticfragment

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

class LoanStaticViewModel : ViewModel() {
    private val _pieChartData = MutableLiveData<List<PieEntry>>()
    val pieChartData: LiveData<List<PieEntry>> get() = _pieChartData

    private val _barChartData = MutableLiveData<List<BarEntry>>()
    val barChar : LiveData<List<BarEntry>> get() = _barChartData

    private val _dataStatic = MutableLiveData<List<StaticCategoryParentModel>>()
    val getDataStatic : LiveData<List<StaticCategoryParentModel>> get() = _dataStatic

    private lateinit var db : AppDatabase

    fun setAppDataBase(db: AppDatabase){
        this.db = db
    }

    fun setDataPieChart(owner : LifecycleOwner){

        db.addNewDao().getAll().observe(owner){entities ->

            val groupIncome = entities.filter { it.type == "loan" }
            val totalIncome = groupIncome.sumOf { it.amount }

            val group = groupIncome.groupBy { it.nameTypeCategory }
                .map{(nameCategory, money) ->
                    val totalMoneyTypeCategory = money.sumOf { it.amount }
                    val per = (totalMoneyTypeCategory.toDouble() / totalIncome.toDouble()) * 100
                    PieEntry(per.toFloat(), nameCategory)
                }
            _pieChartData.postValue(group)
        }
    }

    fun getDataBarChart(owner: LifecycleOwner) {

        db.addNewDao().getAll().observe(owner) { expendList ->

            val groupType = expendList.filter { it.type == "loan" }
            val grouped = groupType.groupBy { it.nameTypeCategory }

            val barEntries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()

            grouped.entries.forEachIndexed { index, entry ->
                val totalMoney = entry.value.sumOf { it.amount}
                barEntries.add(BarEntry(index.toFloat(), totalMoney.toFloat()))
                labels.add(entry.key)
            }
            _barChartData.postValue(barEntries)
        }
    }

    fun getDataCategoryStatic(own: LifecycleOwner) {
        db.addNewDao().getAll().observe(own) { entity ->
            val groupTypeNameCategory = entity.filter { it.type == "loan" }

            val totalMoneyLoan = groupTypeNameCategory.sumOf { it.amount }

            val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")

            val groupMonth = groupTypeNameCategory.groupBy { date ->

                val date = LocalDate.parse(date.date, formatter)
                "${date.monthValue}/${date.year}"
            }

            val parentNameCategory = groupMonth.map { (dateMonth, items) ->

                val childList = items.groupBy { it.nameTypeCategory }
                    .map { (categoryName, list) ->
                        val totalMoney = list.sumOf { it.amount }

                        val processValue = (totalMoney * 100f) / totalMoneyLoan

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