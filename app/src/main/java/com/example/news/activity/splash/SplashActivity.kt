package com.example.news.activity.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.activity.base.BaseActivity
import com.example.news.databinding.ActivitySplashBinding
import androidx.core.content.edit

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 在调用父类 onCreate 之前启用边缘到边缘
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    /** 初始化视图 */
    override fun initView() {
        setupWindowInsets()
        checkFirstLaunch()
    }

    /** 设置监听器 */
    override fun setListeners() {
        // 启动页通常不需要设置监听器
    }

    /** 观察数据变化 */
    override fun observeData() {
        // 启动页通常不需要观察数据
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.splashContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkFirstLaunch() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)

        // 如果不是首次启动，直接跳转到MainActivity
//        if (!isFirstLaunch) {
//            startMainActivity()
//            return
//        }

        // 标记为非首次启动
        sharedPreferences.edit { putBoolean("is_first_launch", false) }

        // 延迟2秒后跳转到主页面
        Handler(Looper.getMainLooper()).postDelayed({
            startMainActivity()
        }, 2000)
    }

    private fun startMainActivity() {
        navigateTo(MainActivity::class.java)
        finish()
    }
}