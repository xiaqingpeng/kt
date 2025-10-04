package com.example.news.activity.profile

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.activity.base.BaseActivity

class MembershipActivity : BaseActivity() {

    private var userId: String? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupWindowInsets()
        setupToolbar()
        setupMembershipInfo()
        setupButtons()
    }

    /** 获取布局资源ID */
    override fun getLayoutResId(): Int {
        return R.layout.activity_membership
    }

    /** 初始化视图 */
    override fun initView() {

        // 获取传递的参数
        getIntentExtras()

        setupWindowInsets()
        setupToolbar()

        setupMembershipInfo()
        setupButtons()
    }

    /** 获取 Intent 传递的参数 */
    private fun getIntentExtras() {
        // 方式1：通过 intent.extras 获取 Bundle
        //        val extras = intent.extras
        //        userId = extras?.getString("user_id")
        //        userName = extras?.getString("user_name")

        // 方式2：直接通过 intent 获取参数（推荐）
        userId = intent.getStringExtra("user_id")
        userName = intent.getStringExtra("user_name")

        // 方式3：使用安全的方式获取，提供默认值
        //        userId = intent.getStringExtra("user_id") ?: "未知用户"
        //        userName = intent.getStringExtra("user_name") ?: "健身达人"

        // 打印参数用于调试
        println("用户ID: $userId, 用户名: $userName")
    }

    private fun setupWindowInsets() {
        val appBarLayout =
                findViewById<com.google.android.material.appbar.AppBarLayout>(R.id.appBarLayout)
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
        val tvMembershipName = findViewById<TextView>(R.id.tvMembershipName)

        val tvExpireDate = findViewById<TextView>(R.id.tvExpireDate)
        val tvMembershipType = findViewById<TextView>(R.id.tvMembershipType)

        // 模拟会员数据
        tvMembershipStatus.text = "当前会员状态：已开通"
        tvMembershipName.text = "当前会员名称：${userName}"
        tvExpireDate?.text = getString(R.string.membership_expire_time, "2024-12-31")
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
