package com.example.news.activity.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.activity.base.BaseActivity
import com.example.news.adapter.NotificationAdapter
import com.example.news.model.NotificationItem

class NotificationActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    /**
     * 获取布局资源ID
     */
    override fun getLayoutResId(): Int {
        return R.layout.activity_notification
    }

    /**
     * 初始化视图
     */
    override fun initView() {
        setupWindowInsets()
        setupToolbar()
        setupRecyclerView()
        loadNotifications()
    }

    /**
     * 设置监听器
     */
    override fun setListeners() {
        // 这里可以添加其他监听器
        // 例如：下拉刷新、按钮点击等
    }

    /**
     * 观察数据变化
     */
    override fun observeData() {
        // 可以在这里添加数据观察
        // 例如：如果使用 ViewModel，可以在这里观察 LiveData
    }

    private fun setupWindowInsets() {
        val appBarLayout = findViewById<com.google.android.material.appbar.AppBarLayout>(R.id.appBarLayout)
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, "通知", true)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotificationAdapter()
        recyclerView.adapter = adapter
    }

    private fun loadNotifications() {
        // 模拟通知数据
        val notifications = listOf(
            NotificationItem(
                "1", // 添加 id 用于 DiffUtil
                "系统通知",
                "欢迎使用健身应用",
                "2024-10-02 10:00",
                false
            ),
            NotificationItem(
                "2",
                "运动提醒",
                "今天还没有完成运动目标哦",
                "2024-10-02 09:00",
                false
            ),
            NotificationItem(
                "3",
                "会员消息",
                "您的会员即将到期，请及时续费",
                "2024-10-01 15:30",
                true
            ),
            NotificationItem(
                "4",
                "活动通知",
                "新的燃烧营活动已开启",
                "2024-09-30 12:00",
                true
            )
        )

        // 使用 BaseAdapter 提供的方法
        if (adapter is com.example.news.adapter.base.BaseAdapter<*, *>) {
            (adapter as com.example.news.adapter.base.BaseAdapter<NotificationItem, *>).updateData(notifications)
        } else {
            // 如果 NotificationAdapter 使用 ListAdapter，使用 submitList
            adapter.submitList(notifications)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * 刷新通知数据
     */
    fun refreshNotifications() {
        showToast("刷新通知")
        loadNotifications()
    }

    /**
     * 标记所有通知为已读
     */
    fun markAllAsRead() {
        showToast("标记所有为已读")
        // 这里可以实现标记所有通知为已读的逻辑
    }

    /**
     * 清空所有通知
     */
    fun clearAllNotifications() {
        showToast("清空所有通知")
        // 这里可以实现清空通知的逻辑
        if (adapter is com.example.news.adapter.base.BaseAdapter<*, *>) {
            (adapter as com.example.news.adapter.base.BaseAdapter<NotificationItem, *>).clear()
        }
    }

    companion object {
        /**
         * 创建新的 NotificationActivity 实例
         */
        fun newIntent(context: android.content.Context): android.content.Intent {
            return android.content.Intent(context, NotificationActivity::class.java)
        }
    }
}