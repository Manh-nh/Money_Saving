package com.example.moneymanagement.presentation.view.fragment.income

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.R
import com.example.moneymanagement.databinding.FragmentIncomeStaticBinding
import com.example.moneymanagement.presentation.database.roomdb.DataManager
import com.example.moneymanagement.presentation.view.adapter.IncomeStaticCategoryParentAdapter
import com.example.moneymanagement.presentation.view.base.BaseFragment
import com.example.moneymanagement.presentation.view.activity.home.HomeViewModel
import com.example.moneymanagement.presentation.view.selectmonthdialog.SelectionYearPopup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.Calendar

class IncomeStaticFragment :
    BaseFragment<FragmentIncomeStaticBinding>(FragmentIncomeStaticBinding::inflate) {

    private lateinit var viewModel: IncomeStaticViewModel
    private var chartIsFirst: Boolean = true
    private lateinit var adapter: IncomeStaticCategoryParentAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var yearPopup: SelectionYearPopup? = null

    override fun initializeComponent() {
        super.initializeComponent()
        viewModel = ViewModelProvider(this)[IncomeStaticViewModel::class.java]
        val appDatabase = DataManager.getDataBase(requireContext())

        adapter = IncomeStaticCategoryParentAdapter(emptyList())
        binding.lstStaticCategory.adapter = adapter

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        viewModel.setAppDataBase(appDatabase)

        // Set default month text
        val calendar = Calendar.getInstance()
        val monthNames = arrayOf("January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")
        binding.txtMonth.text = "${monthNames[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}"

        // Observe DB once
        viewModel.observeData(this)

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
            binding.txtMonth.text = "$monthFormat $year"
            viewModel.setSelectedDate(month + 1, year)
        }

    }

    override fun initializeEvents() {
        binding.btnStatic.setOnClickListener {
            chartIsFirst = !chartIsFirst
            if (chartIsFirst) {
                binding.btnStatic.setImageResource(R.drawable.ic_columg_chart)
                binding.columChart.isVisible = true
                binding.pieChart.isVisible = false
            } else {
                binding.btnStatic.setImageResource(R.drawable.ic_static)
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

        if (entries.isEmpty()) {
            pieChart.clear()
            pieChart.invalidate()
            return
        }

        pieChart.setUsePercentValues(false)
        pieChart.isHighlightPerTapEnabled = false
        pieChart.setExtraOffsets(5f, 5f, 5f, 5f)

        val iconSize = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)

        entries.forEach { entry ->
            val iconResId = entry.data as? Int ?: return@forEach
            val icon = ContextCompat.getDrawable(requireContext(), iconResId)
            if (icon != null) {
                val bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                icon.setBounds(0, 0, iconSize, iconSize)
                icon.draw(canvas)
                entry.icon = BitmapDrawable(resources, bitmap)
            }
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.setDrawValues(true) // Enable values to force icon rendering
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "" // Hide value text
            }
        }
        dataSet.setDrawIcons(true)
        dataSet.iconsOffset = com.github.mikephil.charting.utils.MPPointF(0f, 0f)

        dataSet.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)

        pieChart.setTransparentCircleAlpha(0)
        pieChart.holeRadius = 60f

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }

    private fun updateColumChart(entries: List<BarEntry>){
        val column: BarChart = binding.columChart

        if (entries.isEmpty()) {
            column.clear()
            column.invalidate()
            return
        }

        val dataSet = BarDataSet(entries, "Doanh số")
        dataSet.color = "#4F80FC".toColorInt()
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        dataSet.isHighlightEnabled = false

        val data = BarData(dataSet)
        data.barWidth = 0.12f

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
            yearPopup = SelectionYearPopup(requireContext(), requireActivity())
        }
        yearPopup?.showPopup(binding.btnCalender)
    }


}