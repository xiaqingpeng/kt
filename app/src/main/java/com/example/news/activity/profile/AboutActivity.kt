package com.example.news.activity.profile

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.activity.base.BaseActivity


class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
    override fun getLayoutResId(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        super.initView()
        setupWindowInsets()
        setupToolbar()
        setupViews()

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
            title = "关于有品"
        }
    }

    private fun setupViews() {
        // 设置APP名称和版本号
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvVersion = findViewById<TextView>(R.id.tvVersion)
        val tvRecordNumber = findViewById<TextView>(R.id.tvRecordNumber)

        tvAppName.text = getString(R.string.app_name)
        tvVersion.text = "版本 ${getAppVersion()}"
        tvRecordNumber.text = "京ICP备12345678号-1"

        // 检查更新
        findViewById<CardView>(R.id.cardCheckUpdate).setOnClickListener {
            checkForUpdate()
        }

        // 服务协议
        findViewById<CardView>(R.id.cardServiceAgreement).setOnClickListener {
            openWebPage("https://www.example.com/service-agreement.html")
        }

        // 隐私政策
        findViewById<CardView>(R.id.cardPrivacyPolicy).setOnClickListener {
            openWebPage("https://www.example.com/privacy-policy.html")
        }
    }

    private fun getAppVersion(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName ?: "1.0.0"
        } catch (_: Exception) {
            "1.0.0"
        }
    }

    private fun checkForUpdate() {
        // 这里实现检查更新的逻辑
        showToast("当前已是最新版本")
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