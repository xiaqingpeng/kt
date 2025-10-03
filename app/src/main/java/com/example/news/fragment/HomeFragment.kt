package com.example.news.fragment

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.news.R
import com.example.news.adapter.TabPagerAdapter
import com.example.news.fragment.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: TabPagerAdapter

    private val tabTitles = listOf("首页", "燃烧营", "饮食", "智慧减脂", "饮食小课")

    /**
     * 获取布局资源ID
     */
    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    /**
     * 初始化视图
     */
    override fun initView(view: View) {
        initTabLayout(view)
        initViewPager(view)
        setupTabLayoutWithViewPager()
        setupViewPagerListener()
    }

    /**
     * 初始化 TabLayout
     */
    private fun initTabLayout(view: View) {
        tabLayout = view.findViewByIdOrNull(R.id.tabLayout_home) ?: run {
            showToast("TabLayout 初始化失败")
            return
        }

        // 设置 TabLayout 属性
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.isTabIndicatorFullWidth = false

        // 为 TabLayout 设置内容描述，解决可访问性警告
        tabLayout.contentDescription = "首页导航标签"
    }

    /**
     * 初始化 ViewPager
     */
    private fun initViewPager(view: View) {
        viewPager = view.findViewByIdOrNull(R.id.viewPager) ?: run {
            showToast("ViewPager 初始化失败")
            return
        }

        // 设置 ViewPager 属性
        viewPager.offscreenPageLimit = 2
        adapter = TabPagerAdapter(tabTitles)
        viewPager.adapter = adapter
    }

    /**
     * 设置 TabLayout 与 ViewPager 的关联
     */
    private fun setupTabLayoutWithViewPager() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val title = tabTitles[position]
            tab.text = title
            // 关键修复：为每个 Tab 设置内容描述，解决可访问性警告
            tab.contentDescription = "${title}标签"
        }.attach()
    }

    /**
     * 设置监听器
     */
    override fun setListeners() {
        setupTabSelectedListener()
    }

    /**
     * 设置 Tab 选择监听器
     */
    private fun setupTabSelectedListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Tab 被选中时的处理
                tab?.let {
                    safeRun {
                        // 为屏幕阅读器提供反馈
                        it.view.announceForAccessibility("已选择${it.text}")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Tab 取消选中时的处理
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Tab 重新选中时的处理（快速点击同一个 Tab）
                tab?.let {
                    safeRun {
                        showToast("当前已在${it.text}页面")
                    }
                }
            }
        })
    }

    /**
     * 观察数据变化
     */
    override fun observeData() {
        // 可以在这里添加 LiveData 观察
        // 例如：viewModel.data.observe(viewLifecycleOwner) { data -> }
    }

    /**
     * 设置 ViewPager 页面改变监听器
     */
    private fun setupViewPagerListener() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 页面切换时的处理
                safeRun {
                    // 移除频繁的 toast 提示，避免打扰用户
                    // showToast("切换到: ${tabTitles[position]}")

                    // 可以在这里执行其他页面切换逻辑
                    handlePageChange(position)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // 页面滚动状态变化的处理
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        // 用户开始拖动
                    }
                    ViewPager2.SCROLL_STATE_SETTLING -> {
                        // 页面正在 settling
                    }
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        // 滚动结束
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                // 页面滚动时的处理
            }
        })
    }

    /**
     * 处理页面切换
     */
    private fun handlePageChange(position: Int) {
        // 根据页面位置执行不同的逻辑
        when (position) {
            0 -> {
                // 首页逻辑 - 可以加载首页数据
                loadHomeData()
            }
            1 -> {
                // 燃烧营逻辑 - 可以加载燃烧营数据
                loadBurnCampData()
            }
            2 -> {
                // 饮食逻辑 - 可以加载饮食数据
                loadDietData()
            }
            3 -> {
                // 智慧减脂逻辑 - 可以加载智慧减脂数据
                loadSmartWeightLossData()
            }
            4 -> {
                // 饮食小课逻辑 - 可以加载饮食小课数据
                loadDietCourseData()
            }
        }
    }

    /**
     * 加载首页数据
     */
    private fun loadHomeData() {
        // 实现首页数据加载逻辑
    }

    /**
     * 加载燃烧营数据
     */
    private fun loadBurnCampData() {
        // 实现燃烧营数据加载逻辑
    }

    /**
     * 加载饮食数据
     */
    private fun loadDietData() {
        // 实现饮食数据加载逻辑
    }

    /**
     * 加载智慧减脂数据
     */
    private fun loadSmartWeightLossData() {
        // 实现智慧减脂数据加载逻辑
    }

    /**
     * 加载饮食小课数据
     */
    private fun loadDietCourseData() {
        // 实现饮食小课数据加载逻辑
    }

    /**
     * 获取当前选中的页面位置
     */
    fun getCurrentPagePosition(): Int {
        return if (::viewPager.isInitialized) viewPager.currentItem else 0
    }

    /**
     * 切换到指定页面
     */
    fun switchToPage(position: Int) {
        if (::viewPager.isInitialized && position in tabTitles.indices) {
            viewPager.currentItem = position
        }
    }

    /**
     * 动态更新 Tab 数据
     */
    fun updateTabTitles(newTitles: List<String>) {
        safeRun {
            // 更新适配器数据
            adapter.updateData(newTitles)

            // 重新设置 Tab 内容描述
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                if (i < newTitles.size) {
                    val title = newTitles[i]
                    tab?.text = title
                    tab?.contentDescription = "${title}标签"
                }
            }
        }
    }

    /**
     * 设置 ViewPager 是否可以滑动
     */
    fun setViewPagerScrollable(scrollable: Boolean) {
        if (::viewPager.isInitialized) {
            viewPager.isUserInputEnabled = scrollable
        }
    }

    /**
     * 获取当前页面标题
     */
    fun getCurrentPageTitle(): String {
        val position = getCurrentPagePosition()
        return if (position in tabTitles.indices) tabTitles[position] else ""
    }

    /**
     * 刷新当前页面数据
     */
    fun refreshCurrentPage() {
        safeRun {
            val currentPosition = getCurrentPagePosition()
            handlePageChange(currentPosition)
        }
    }

    override fun onDestroyView() {
        // 清理资源，避免内存泄漏
        if (::viewPager.isInitialized) {
            viewPager.adapter = null
        }
        super.onDestroyView()
    }

    companion object {
        /**
         * 创建新的 HomeFragment 实例
         */
        fun newInstance(): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    // 可以在这里传递参数
                }
            }
        }
    }
}