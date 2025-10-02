package com.example.news.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapter.NotificationAdapter
import com.example.news.model.NotificationItem

class NotificationActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)
        
        setupWindowInsets()
        setupToolbar()
        setupRecyclerView()
        loadNotifications()
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
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "通知"
        }
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
                "系统通知",
                "欢迎使用健身应用",
                "2024-10-02 10:00",
                false
            ),
            NotificationItem(
                "运动提醒",
                "今天还没有完成运动目标哦",
                "2024-10-02 09:00",
                false
            ),
            NotificationItem(
                "会员消息",
                "您的会员即将到期，请及时续费",
                "2024-10-01 15:30",
                true
            ),
            NotificationItem(
                "活动通知",
                "新的燃烧营活动已开启",
                "2024-09-30 12:00",
                true
            )
        )
        adapter.submitList(notifications)
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
}