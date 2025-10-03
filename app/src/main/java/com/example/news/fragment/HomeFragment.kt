package com.example.news.fragment

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
        // 使用安全的方式查找视图
        tabLayout = view.findViewByIdOrNull(R.id.tabLayout_home) ?: run {
            showToast("TabLayout 初始化失败")
            return
        }

        viewPager = view.findViewByIdOrNull(R.id.viewPager) ?: run {
            showToast("ViewPager 初始化失败")
            return
        }

        // Setup ViewPager2
        val adapter = TabPagerAdapter(tabTitles)
        viewPager.adapter = adapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // 可以添加其他初始化代码
        setupViewPagerListener()
    }

    /**
     * 设置监听器
     */
    override fun setListeners() {
        // 可以在这里添加其他监听器
        // 例如：按钮点击事件等
    }

    /**
     * 观察数据变化
     */
    override fun observeData() {
        // 可以在这里添加 LiveData 观察
        // 例如：viewModel.data.observe(viewLifecycleOwner) { data -> }
    }

    /**
     * 设置 ViewPager 监听器
     */
    private fun setupViewPagerListener() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 页面切换时的处理
                safeRun {
                    showToast("切换到: ${tabTitles[position]}")
                }
            }
        })
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

    override fun onDestroyView() {
        // 清理资源
        super.onDestroyView()
    }
}