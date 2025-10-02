package com.example.news.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R

class MembershipActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_membership)
        
        setupWindowInsets()
        setupToolbar()
        setupMembershipInfo()
        setupButtons()
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
            title = "我的会员"
        }
    }
    
    private fun setupMembershipInfo() {
        val tvMembershipStatus = findViewById<TextView>(R.id.tvMembershipStatus)
        val tvExpireDate = findViewById<TextView>(R.id.tvExpireDate)
        val tvMembershipType = findViewById<TextView>(R.id.tvMembershipType)
        
        // 模拟会员数据
        tvMembershipStatus.text = "当前会员状态：已开通"
        tvExpireDate.text = "到期时间：2024-12-31"
        tvMembershipType.text = "会员类型：年度会员"
    }
    
    private fun setupButtons() {
        findViewById<Button>(R.id.btnRenew).setOnClickListener {
            Toast.makeText(this, "跳转到续费页面", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Button>(R.id.btnUpgrade).setOnClickListener {
            Toast.makeText(this, "跳转到升级页面", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<CardView>(R.id.cardBenefit1).setOnClickListener {
            Toast.makeText(this, "查看专属课程", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<CardView>(R.id.cardBenefit2).setOnClickListener {
            Toast.makeText(this, "查看数据分析", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<CardView>(R.id.cardBenefit3).setOnClickListener {
            Toast.makeText(this, "查看优先客服", Toast.LENGTH_SHORT).show()
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
}