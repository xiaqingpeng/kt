package com.example.news.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.news.R
import com.example.news.adapter.BannerAdapter
import com.example.news.fragment.base.BaseFragment
import com.example.news.model.BannerItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopFragment : BaseFragment() {

    private lateinit var viewPagerBanner: ViewPager2
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var etSearch: TextInputEditText
    private lateinit var searchInputLayout: TextInputLayout
    private lateinit var ivCart: ImageView

    private lateinit var bannerAdapter: BannerAdapter
    private val bannerHandler = Handler(Looper.getMainLooper())
    private var bannerRunnable: Runnable? = null
    private var currentPage = 0
    private var isAutoScrolling = true

    // 原始轮播图数据
    private val originalBannerList = listOf(
        BannerItem("1", "新品上市", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204281609.png"),
        BannerItem("2", "限时特惠", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204750632.png"),
        BannerItem("3", "品牌推荐", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204102466.png"),
        BannerItem("4", "热销商品", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204221037.png")
    )

    // 为无缝轮播处理后的数据
    private val bannerList by lazy {
        if (originalBannerList.size > 1) {
            // 在首尾添加额外数据以实现无缝轮播
            val lastItem = originalBannerList.last()
            val firstItem = originalBannerList.first()
            listOf(lastItem) + originalBannerList + listOf(firstItem)
        } else {
            originalBannerList
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_shop
    }

    override fun initView(view: View) {
        initViews(view)
        setupSearchView()
        setupBanner()
        setupIndicators()
    }

    private fun initViews(view: View) {
        viewPagerBanner = view.findViewByIdOrNull(R.id.viewPagerBanner) ?: run {
            showToast("ViewPager初始化失败")
            return
        }

        indicatorContainer = view.findViewByIdOrNull(R.id.indicatorContainer) ?: run {
            showToast("指示器容器初始化失败")
            return
        }

        etSearch = view.findViewByIdOrNull(R.id.etSearch) ?: run {
            showToast("搜索框初始化失败")
            return
        }

        searchInputLayout = view.findViewByIdOrNull(R.id.searchInputLayout) ?: run {
            showToast("搜索布局初始化失败")
            return
        }

        ivCart = view.findViewByIdOrNull(R.id.ivCart) ?: run {
            showToast("购物车图标初始化失败")
            return
        }
    }

    /**
     * 设置搜索框功能
     */
    private fun setupSearchView() {
        // 设置搜索图标点击监听
        searchInputLayout.setStartIconOnClickListener {
            etSearch.requestFocus()
            showKeyboard()
            performSearch(etSearch.text.toString())
        }

        // 设置文本变化监听
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // 实时搜索或显示搜索结果
                s?.let {
                    if (it.isNotEmpty()) {
                        performSearch(it.toString())
                    } else {
                        // 清空搜索时隐藏搜索结果
                        clearSearchResults()
                    }
                }
            }
        })

        // 设置键盘搜索按钮监听
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.text.toString())
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        // 设置焦点变化监听
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 获得焦点时的动画效果
                searchInputLayout.boxStrokeColor = resources.getColor(R.color.gradient_start, null)
            } else {
                // 失去焦点时恢复默认颜色
                searchInputLayout.boxStrokeColor = resources.getColor(R.color.gray, null)
            }
        }
    }

    /**
     * 执行搜索
     */
    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            // 执行搜索逻辑
            showToast("搜索关键词: $query")
            // 这里可以添加实际的搜索逻辑，比如请求API、过滤数据等
        }
    }

    /**
     * 清空搜索结果
     */
    private fun clearSearchResults() {
        // 清空搜索结果的逻辑
        showToast("搜索已清空")
    }

    /**
     * 显示键盘
     */
    private fun showKeyboard() {
        etSearch.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 隐藏键盘
     */
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    private fun setupBanner() {
        bannerAdapter = BannerAdapter(bannerList) { banner ->
            val realPosition = getRealPosition(currentPage)
            val realBanner = originalBannerList[realPosition]
            showToast("点击了轮播图: ${realBanner.title}")
        }

        viewPagerBanner.adapter = bannerAdapter

        // 设置轮播图属性
        viewPagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPagerBanner.offscreenPageLimit = 3 // 设置预加载数量，确保无缝切换

        // 如果是无缝轮播，初始位置设为1（因为我们在开头添加了一个）
        if (bannerList.size > originalBannerList.size) {
            viewPagerBanner.setCurrentItem(1, false)
            currentPage = 1
        }

        // 设置页面变化监听 - 关键：实现无缝循环
        viewPagerBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position

                // 更新指示器，考虑无缝轮播的偏移
                val indicatorPosition = getRealPosition(position)
                updateIndicators(indicatorPosition)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                // 在滑动结束时处理边界跳转
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    handleInfiniteScroll()
                }

                // 用户操作时暂停自动轮播，操作结束后恢复
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        isAutoScrolling = false
                        stopAutoScroll()
                    }
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        if (!isAutoScrolling) {
                            isAutoScrolling = true
                            startAutoScroll()
                        }
                    }
                }
            }
        })

        // 启动自动轮播
        startAutoScroll()
    }

    /**
     * 处理无缝轮播的边界跳转
     */
    private fun handleInfiniteScroll() {
        if (bannerList.size <= originalBannerList.size) return

        when (currentPage) {
            0 -> {
                // 滑动到开头添加的额外项，无声跳转到倒数第二项（真实数据的最后一项）
                viewPagerBanner.setCurrentItem(bannerList.size - 2, false)
                currentPage = bannerList.size - 2
            }
            bannerList.size - 1 -> {
                // 滑动到末尾添加的额外项，无声跳转到第二项（真实数据的第一项）
                viewPagerBanner.setCurrentItem(1, false)
                currentPage = 1
            }
        }
    }

    /**
     * 获取真实的指示器位置（去除无缝轮播添加的额外项）
     */
    private fun getRealPosition(position: Int): Int {
        return if (bannerList.size > originalBannerList.size) {
            when (position) {
                0 -> originalBannerList.size - 1 // 开头额外项对应真实数据的最后一项
                bannerList.size - 1 -> 0 // 末尾额外项对应真实数据的第一项
                else -> position - 1 // 中间项需要减1
            }
        } else {
            position
        }
    }

    private fun setupIndicators() {
        indicatorContainer.removeAllViews()

        // 指示器数量使用原始数据数量
        for (i in originalBannerList.indices) {
            val indicator = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.indicator_width),
                    resources.getDimensionPixelSize(R.dimen.indicator_height)
                ).apply {
                    setMargins(
                        resources.getDimensionPixelSize(R.dimen.indicator_margin),
                        0,
                        resources.getDimensionPixelSize(R.dimen.indicator_margin),
                        0
                    )
                }
                setImageResource(R.drawable.indicator_unselected)
            }
            indicatorContainer.addView(indicator)
        }

        // 设置第一个指示器为选中状态
        if (originalBannerList.isNotEmpty()) {
            updateIndicators(0)
        }
    }

    private fun updateIndicators(realPosition: Int) {
        for (i in 0 until indicatorContainer.childCount) {
            val indicator = indicatorContainer.getChildAt(i) as ImageView
            indicator.setImageResource(
                if (i == realPosition) R.drawable.indicator_selected
                else R.drawable.indicator_unselected
            )
        }
    }

    private fun startAutoScroll() {
        stopAutoScroll() // 先停止之前的轮播

        bannerRunnable = object : Runnable {
            override fun run() {
                if (bannerList.isNotEmpty() && isAutoScrolling) {
                    currentPage = (currentPage + 1) % bannerList.size
                    viewPagerBanner.setCurrentItem(currentPage, true)
                    bannerHandler.postDelayed(this, 3000) // 3秒切换一次
                }
            }
        }
        bannerHandler.postDelayed(bannerRunnable!!, 3000)
    }

    private fun stopAutoScroll() {
        bannerRunnable?.let { bannerHandler.removeCallbacks(it) }
    }

    override fun setListeners() {
        // 购物车点击事件
        ivCart.setOnClickListener {
            showToast("点击了购物车")
            // 这里可以跳转到购物车页面
        }

        // 搜索框点击事件（保留原有的点击逻辑）
        etSearch.setOnClickListener {
            showToast("点击了搜索框")
            // 这里可以跳转到搜索页面或展开搜索功能
        }
    }

    override fun observeData() {
        // 观察数据变化
    }

    override fun handleArguments() {
        // 处理传递的参数
    }

    override fun onResume() {
        super.onResume()
        isAutoScrolling = true
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        isAutoScrolling = false
        stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isAutoScrolling = false
        stopAutoScroll()
        bannerHandler.removeCallbacksAndMessages(null)
    }
}