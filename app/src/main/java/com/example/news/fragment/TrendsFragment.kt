package com.example.news.fragment

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.example.news.R
import com.example.news.fragment.base.BaseFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.tabs.TabLayout

class TrendsFragment : BaseFragment() {

    private lateinit var lineChart: LineChart
    private lateinit var tabLayout: TabLayout
    private lateinit var tvChartTitle: TextView
    private lateinit var tvCurrentValue: TextView
    private lateinit var tvGrowthRate: TextView
    private lateinit var tvPeakValue: TextView

    // 数据模式枚举
    private enum class DataMode { DAILY, MONTHLY, YEARLY }
    private var currentDataMode = DataMode.DAILY

    // 模拟数据
    private val dailyData = listOf(120f, 135f, 148f, 162f, 175f, 190f, 210f, 195f, 180f, 165f, 150f, 140f)
    private val monthlyData = listOf(3200f, 3500f, 3800f, 4200f, 4500f, 4800f, 5200f, 5000f, 4700f, 4300f, 3900f, 3600f)
    private val yearlyData = listOf(38000f, 42000f, 45000f, 49000f, 52000f, 56000f)

    override fun getLayoutResId(): Int {
        return R.layout.fragment_trends
    }

    override fun initView(view: View) {
        initViews(view)
        setupLineChart()
        setupTabLayout()
        setupLineChartData()
        updateStatistics()
    }

    override fun setListeners() {
        // TabLayout 监听器在 setupTabLayout() 中设置
    }

    private fun initViews(view: View) {
        lineChart = view.findViewById(R.id.lineChart)
        tabLayout = view.findViewById(R.id.tabLayout)
        tvChartTitle = view.findViewById(R.id.tvChartTitle)
        tvCurrentValue = view.findViewById(R.id.tvCurrentValue)
        tvGrowthRate = view.findViewById(R.id.tvGrowthRate)
        tvPeakValue = view.findViewById(R.id.tvPeakValue)
    }

    private fun setupLineChart() {
        // 基本配置
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        // 描述文本 - 隐藏，因为已经有外部标题了
        val description = Description()
        description.isEnabled = false
        lineChart.description = description

        // 图例设置 - 调整到图表外部底部
        val legend = lineChart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.yOffset = 25f  // 增加底部偏移
        legend.xOffset = 0f
        legend.textSize = 11f
        legend.formSize = 12f

        // X轴配置
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.labelCount = 6
        xAxis.textSize = 10f
        xAxis.yOffset = 8f

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (currentDataMode) {
                    DataMode.DAILY -> "${value.toInt() + 1}日"
                    DataMode.MONTHLY -> "${value.toInt() + 1}月"
                    DataMode.YEARLY -> "${(2020 + value).toInt()}年"
                }
            }
        }

        // 左侧Y轴
        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = Color.parseColor("#EEEEEE")
        leftAxis.axisMinimum = 0f
        leftAxis.granularity = 1f
        leftAxis.textColor = Color.GRAY
        leftAxis.textSize = 10f
        leftAxis.setDrawTopYLabelEntry(false)
        leftAxis.xOffset = 5f

        // 右侧Y轴
        val rightAxis = lineChart.axisRight
        rightAxis.isEnabled = false

        // 为图例留出底部空间
        lineChart.setExtraOffsets(0f, 10f, 0f, 45f)
    }

    private fun setupTabLayout() {
        // 添加Tab
        tabLayout.addTab(tabLayout.newTab().setText("日活跃"))
        tabLayout.addTab(tabLayout.newTab().setText("月活跃"))
        tabLayout.addTab(tabLayout.newTab().setText("年活跃"))

        // 设置Tab选择监听器
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> switchToDailyData()
                    1 -> switchToMonthlyData()
                    2 -> switchToYearlyData()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupLineChartData() {
        // 初始显示日活跃数据
        switchToDailyData()
    }

    private fun switchToDailyData() {
        currentDataMode = DataMode.DAILY

        val entries = ArrayList<Entry>()
        dailyData.forEachIndexed { index, value ->
            entries.add(Entry(index.toFloat(), value))
        }

        val dataSet = LineDataSet(entries, "日活跃用户")
        configureDataSet(dataSet, Color.parseColor("#FF6B6B"))

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        updateXAxis()
        updateChartTitle("日活跃用户趋势")
        updateStatistics()
        lineChart.animateY(1000)
    }

    private fun switchToMonthlyData() {
        currentDataMode = DataMode.MONTHLY

        val entries = ArrayList<Entry>()
        monthlyData.forEachIndexed { index, value ->
            entries.add(Entry(index.toFloat(), value))
        }

        val dataSet = LineDataSet(entries, "月活跃用户")
        configureDataSet(dataSet, Color.parseColor("#4ECDC4"))

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        updateXAxis()
        updateChartTitle("月活跃用户趋势")
        updateStatistics()
        lineChart.animateY(1000)
    }

    private fun switchToYearlyData() {
        currentDataMode = DataMode.YEARLY

        val entries = ArrayList<Entry>()
        yearlyData.forEachIndexed { index, value ->
            entries.add(Entry(index.toFloat(), value))
        }

        val dataSet = LineDataSet(entries, "年活跃用户")
        configureDataSet(dataSet, Color.parseColor("#45B7D1"))

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        updateXAxis()
        updateChartTitle("年活跃用户趋势")
        updateStatistics()
        lineChart.animateY(1000)
    }

    private fun configureDataSet(dataSet: LineDataSet, color: Int) {
        dataSet.color = color
        dataSet.setCircleColor(color)
        dataSet.lineWidth = 2.5f
        dataSet.circleRadius = 3f
        dataSet.setDrawCircleHole(false)
        dataSet.valueTextSize = 8f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)
        dataSet.fillColor = color
        dataSet.fillAlpha = 30  // 降低填充透明度

        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (currentDataMode) {
                    DataMode.DAILY -> value.toInt().toString()
                    DataMode.MONTHLY -> "${value.toInt() / 1000}K"
                    DataMode.YEARLY -> "${value.toInt() / 1000}K"
                }
            }
        }
    }

    private fun updateXAxis() {
        val xAxis = lineChart.xAxis
        when (currentDataMode) {
            DataMode.DAILY -> {
                xAxis.labelCount = 12
                xAxis.granularity = 1f
            }
            DataMode.MONTHLY -> {
                xAxis.labelCount = 12
                xAxis.granularity = 1f
            }
            DataMode.YEARLY -> {
                xAxis.labelCount = 6
                xAxis.granularity = 1f
            }
        }
        lineChart.invalidate()
    }

    private fun updateChartTitle(title: String) {
        tvChartTitle.text = title
    }

    private fun updateStatistics() {
        when (currentDataMode) {
            DataMode.DAILY -> {
                val current = dailyData.last().toInt()
                val previous = dailyData[dailyData.size - 2]
                val growthRate = ((current - previous) / previous * 100).toInt()
                val peak = dailyData.maxOrNull()?.toInt() ?: 0

                tvCurrentValue.text = formatNumber(current)
                tvGrowthRate.text = "${if (growthRate > 0) "+" else ""}$growthRate%"
                tvGrowthRate.setTextColor(if (growthRate > 0) Color.GREEN else Color.RED)
                tvPeakValue.text = formatNumber(peak)
            }
            DataMode.MONTHLY -> {
                val current = monthlyData.last().toInt()
                val previous = monthlyData[monthlyData.size - 2]
                val growthRate = ((current - previous) / previous * 100).toInt()
                val peak = monthlyData.maxOrNull()?.toInt() ?: 0

                tvCurrentValue.text = formatNumber(current)
                tvGrowthRate.text = "${if (growthRate > 0) "+" else ""}$growthRate%"
                tvGrowthRate.setTextColor(if (growthRate > 0) Color.GREEN else Color.RED)
                tvPeakValue.text = formatNumber(peak)
            }
            DataMode.YEARLY -> {
                val current = yearlyData.last().toInt()
                val previous = yearlyData[yearlyData.size - 2]
                val growthRate = ((current - previous) / previous * 100).toInt()
                val peak = yearlyData.maxOrNull()?.toInt() ?: 0

                tvCurrentValue.text = formatNumber(current)
                tvGrowthRate.text = "${if (growthRate > 0) "+" else ""}$growthRate%"
                tvGrowthRate.setTextColor(if (growthRate > 0) Color.GREEN else Color.RED)
                tvPeakValue.text = formatNumber(peak)
            }
        }
    }

    private fun formatNumber(number: Int): String {
        return when {
            number >= 10000 -> "${number / 10000}万"
            number >= 1000 -> "${number / 1000}千"
            else -> number.toString()
        }
    }

    override fun observeData() {
        // 观察数据变化
    }

    override fun handleArguments() {
        // 处理传递的参数
    }
}