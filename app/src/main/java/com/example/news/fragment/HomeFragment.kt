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

        // 设置 TabLayout 属性（可选）
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.isTabIndicatorFullWidth = false
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
            tab.text = tabTitles[position]
        }.attach()
    }

    /**
     * 设置监听器
     */
    override fun setListeners() {
        // 可以在这里添加其他监听器
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
                    // 可以在这里处理选中状态的变化
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Tab 取消选中时的处理
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Tab 重新选中时的处理（快速点击同一个 Tab）
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
                    // 移除 toast 提示，避免频繁打扰用户
                    // showToast("切换到: ${tabTitles[position]}")

                    // 可以在这里执行其他页面切换逻辑
                    handlePageChange(position)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // 页面滚动状态变化的处理
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
                // 首页逻辑
            }
            1 -> {
                // 燃烧营逻辑
            }
            2 -> {
                // 饮食逻辑
            }
            3 -> {
                // 智慧减脂逻辑
            }
            4 -> {
                // 饮食小课逻辑
            }
        }
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

            // 如果需要同时更新 TabLayout 的标题，需要重新创建 TabLayoutMediator
            // 或者通过其他方式更新 Tab 文本
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