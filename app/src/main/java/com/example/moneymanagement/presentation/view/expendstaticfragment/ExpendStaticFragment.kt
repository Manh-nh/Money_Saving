package com.example.moneymanagement.presentation.view.expendstaticfragment

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.FragmentExpendStaticBinding
import com.example.moneymanagement.presentation.database.DataManager
import com.example.moneymanagement.presentation.view.base.BaseFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.core.graphics.toColorInt
import com.example.moneymanagement.presentation.view.adapter.ExpendStaticCategoryParentAdapter
import com.example.moneymanagement.presentation.view.homeactivity.HomeViewModel
import com.example.moneymanagement.presentation.view.selectmonthdialog.SelectionYearPopup

class ExpendStaticFragment :
    BaseFragment<FragmentExpendStaticBinding>(FragmentExpendStaticBinding::inflate) {

    private lateinit var viewModel: ExpendStaticViewModel
    private var chartIsFirst: Boolean = true
    private var yearPopup: SelectionYearPopup? = null
    private lateinit var adapter : ExpendStaticCategoryParentAdapter
    private lateinit var homeViewModel: HomeViewModel


    override fun initializeComponent() {
        super.initializeComponent()

        val appDatabase = DataManager.getDataBase(requireContext())

        adapter = ExpendStaticCategoryParentAdapter(emptyList())
        binding.lstStaticCategory.adapter = adapter

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        viewModel = ViewModelProvider(this)[ExpendStaticViewModel::class.java]
        viewModel.setAppDataBase(appDatabase)
        viewModel.getDataPieChart(this)
        viewModel.getDataBarChart(this)
        viewModel.getDataStaticCategory(this)

        viewModel.pieChartData.observe(viewLifecycleOwner) {
            updatePieChart(it)
        }

        viewModel.barChar.observe(viewLifecycleOwner) {
            updateColumChart(it)
        }

        viewModel.dataStatic.observe(viewLifecycleOwner){
            adapter.setData(it)
        }

        homeViewModel.selectedMonthYear.observe(viewLifecycleOwner){(month, year , monthFormat) ->
            binding.txtMonth.text = monthFormat
        }
    }

    override fun initializeEvents() {

        binding.btnStaticChart.setOnClickListener {
            chartIsFirst = !chartIsFirst
            if (chartIsFirst) {
                binding.btnStaticChart.setImageResource(R.drawable.ic_columg_chart)
                binding.columChart.isVisible = true
                binding.pieChart.isVisible = false
            } else {
                binding.btnStaticChart.setImageResource(R.drawable.ic_static)
                binding.pieChart.isVisible = true
                binding.columChart.isVisible = false
            }
        }

        binding.btnCalender.setOnClickListener { showYearPopup() }
    }


    override fun initializeData() {
        super.initializeData()

    }

    override fun bindView() {
        super.bindView()
    }

    private fun updatePieChart(entries: List<PieEntry>) {
        val pieChart: PieChart = binding.pieChart

        binding.pieChart.setUsePercentValues(true)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false // chú thích màu
        pieChart.isRotationEnabled = false // tăt bật xoay

        pieChart.setTransparentCircleAlpha(0)

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))

        pieChart.data = data
        pieChart.invalidate()
    }

    private fun updateColumChart(entries: List<BarEntry>) {
        val column: BarChart = binding.columChart

        val dataSet = BarDataSet(entries, "Doanh số")
        dataSet.color = "#4F80FC".toColorInt()
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        dataSet.isHighlightEnabled = false

        val data = BarData(dataSet)
        data.barWidth = 0.2f

        column.data = data
        column.setFitBars(true)
        column.description.isEnabled = false
        column.legend.isEnabled = false
        column.axisRight.isEnabled = false
        column.animateY(1000)

        val xAxis = column.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.setDrawLabels(false)

        val yAxis = column.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.setDrawGridLines(false)

        column.isHighlightPerTapEnabled = false
        column.isHighlightFullBarEnabled = false

        column.invalidate()
    }


    private fun showYearPopup() {
        if (yearPopup == null) {
            yearPopup = SelectionYearPopup(requireContext(), this)
        }
        yearPopup?.showPopup(binding.btnCalender)
    }


}