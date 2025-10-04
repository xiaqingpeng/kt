package com.example.news.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.news.R
import com.example.news.adapter.BannerAdapter
import com.example.news.fragment.base.BaseFragment
import com.example.news.model.BannerItem
import com.google.android.material.textfield.TextInputEditText

class ShopFragment : BaseFragment() {

    private lateinit var viewPagerBanner: ViewPager2
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var etSearch: TextInputEditText
    private lateinit var ivMessage: ImageView
    
    private lateinit var bannerAdapter: BannerAdapter
    private val bannerHandler = Handler(Looper.getMainLooper())
    private var bannerRunnable: Runnable? = null
    private var currentPage = 0

    // 模拟轮播图数据
    private val bannerList = listOf(
        BannerItem("1", "新品上市", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204281609.png"),
        BannerItem("2", "限时特惠", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204750632.png"),
        BannerItem("3", "品牌推荐", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204102466.png"),
        BannerItem("4", "热销商品", "https://cms-dumall.cdn.bcebos.com/cms_com_upload_pro/dumall_1756204221037.png")
    )

    override fun getLayoutResId(): Int {
        return R.layout.fragment_shop
    }

    override fun initView(view: View) {
        initViews(view)
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
        
        ivMessage = view.findViewByIdOrNull(R.id.ivMessage) ?: run {
            showToast("消息图标初始化失败")
            return
        }
    }

    private fun setupBanner() {
        bannerAdapter = BannerAdapter(bannerList) { banner ->
            showToast("点击了轮播图: ${banner.title}")
            // 这里可以处理轮播图点击事件，比如跳转到详情页
        }
        
        viewPagerBanner.adapter = bannerAdapter
        
        // 设置轮播图属性
        viewPagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPagerBanner.offscreenPageLimit = 1
        
        // 设置页面变化监听
        viewPagerBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                updateIndicators(position)
            }
        })
        
        // 启动自动轮播
        startAutoScroll()
    }

    private fun setupIndicators() {
        indicatorContainer.removeAllViews()
        
        for (i in bannerList.indices) {
            val indicator = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
                setImageResource(R.drawable.indicator_unselected)
            }
            indicatorContainer.addView(indicator)
        }
        
        // 设置第一个指示器为选中状态
        if (bannerList.isNotEmpty()) {
            updateIndicators(0)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicatorContainer.childCount) {
            val indicator = indicatorContainer.getChildAt(i) as ImageView
            indicator.setImageResource(
                if (i == position) R.drawable.indicator_selected
                else R.drawable.indicator_unselected
            )
        }
    }

    private fun startAutoScroll() {
        bannerRunnable = object : Runnable {
            override fun run() {
                if (bannerList.isNotEmpty()) {
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
        // 搜索框点击事件
        etSearch.setOnClickListener {
            showToast("点击了搜索框")
            // 这里可以跳转到搜索页面
        }
        
        // 消息图标点击事件
        ivMessage.setOnClickListener {
            showToast("点击了消息")
            // 这里可以跳转到消息页面
        }
    }

    override fun observeData() {
        // 观察数据变化（如果使用 ViewModel 等）
    }

    override fun handleArguments() {
        // 处理传递的参数
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
    }
}