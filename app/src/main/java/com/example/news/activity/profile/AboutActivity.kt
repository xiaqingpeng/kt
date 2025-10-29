package com.example.news.activity.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.activity.base.BaseTitleActivity
import com.example.news.databinding.ActivityAboutBinding

class AboutActivity : BaseTitleActivity<ActivityAboutBinding>() {

    override fun getViewBinding(): ActivityAboutBinding {
        return ActivityAboutBinding.inflate(layoutInflater)
    }

    override fun getToolbar() = binding.toolbar

    override fun getToolbarTitle(): String {
        return "关于有品"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    override fun initView() {
        super.initView() // 这会自动调用 BaseTitleActivity 的 setupToolbar()
        setupWindowInsets()
        setupViews()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun setupViews() {
        // 设置APP名称和版本号
        binding.tvAppName.text = getString(R.string.app_name)
        binding.tvVersion.text = getString(R.string.app_version, getAppVersion())
        binding.tvRecordNumber.text = getString(R.string.record_number)

        // 检查更新
        binding.cardCheckUpdate.setOnClickListener {
            checkForUpdate()
        }

        // 服务协议
        binding.cardServiceAgreement.setOnClickListener {
            openWebPage("https://www.example.com/service-agreement.html")
        }

        // 隐私政策
        binding.cardPrivacyPolicy.setOnClickListener {
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