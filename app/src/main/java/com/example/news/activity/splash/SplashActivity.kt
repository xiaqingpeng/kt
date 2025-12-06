package com.example.news.activity.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.MainActivity
import com.example.news.activity.auth.LoginRegisterActivity
import com.example.news.activity.base.BaseActivity
import com.example.news.databinding.ActivitySplashBinding
import com.example.news.manager.LoginManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    companion object {
        private const val SPLASH_DELAY = 2000L // 2秒延迟
    }

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setupWindowInsets()
        checkAppStateAndNavigate()
    }

    override fun setListeners() {
        // 启动页通常不需要设置监听器
    }

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

    /**
     * 检查应用状态并导航到相应页面
     */
    private fun checkAppStateAndNavigate() {
        // 延迟导航，确保启动页显示足够时间
        Handler(Looper.getMainLooper()).postDelayed({
            val isFirstLaunch = LoginManager.isFirstLaunch(this)
            val isLoggedIn = LoginManager.isLoggedIn(this)
            val isTokenValid = LoginManager.isTokenValid(this)

            // 记录启动信息（用于调试）
            logAppState(isFirstLaunch, isLoggedIn, isTokenValid)

            // 标记为非首次启动（如果不是首次启动）
            if (isFirstLaunch) {
                LoginManager.setNotFirstLaunch(this)
            }

            navigateBasedOnState(isFirstLaunch, isLoggedIn, isTokenValid)
        }, SPLASH_DELAY)
    }

    /**
     * 根据应用状态导航到相应页面
     */
    private fun navigateBasedOnState(
        isFirstLaunch: Boolean,
        isLoggedIn: Boolean,
        isTokenValid: Boolean
    ) {
        when {
            // 情况1：已登录且token有效 -> 直接进入主页面
            isLoggedIn && isTokenValid -> {
                showToast("欢迎回来")
                navigateToMain()
            }

            // 情况2：已登录但token过期 -> 跳转到登录页重新登录
            isLoggedIn && !isTokenValid -> {
                showToast("登录已过期，请重新登录")
                LoginManager.clearLoginInfo(this)
                navigateToLogin()
            }

            // 情况3：首次启动 -> 跳转到登录页
            isFirstLaunch -> {
                showToast("欢迎使用")
                navigateToLogin()
            }

            // 情况4：非首次启动且未登录 -> 跳转到登录页
            else -> {
                navigateToLogin()
            }
        }
    }

    /**
     * 导航到主页面
     */
    private fun navigateToMain() {
        navigateTo(MainActivity::class.java, true)
    }

    /**
     * 导航到登录页面
     */
    private fun navigateToLogin() {
        navigateTo(LoginRegisterActivity::class.java, true)
    }

    /**
     * 记录应用状态（用于调试）
     */
    private fun logAppState(isFirstLaunch: Boolean, isLoggedIn: Boolean, isTokenValid: Boolean) {
        println("应用状态 - 首次启动: $isFirstLaunch, 已登录: $isLoggedIn, Token有效: $isTokenValid")

        if (isLoggedIn) {
            val userSummary = LoginManager.getUserSummary(this)
            println("用户信息: $userSummary")
        }
    }

    /**
     * 处理跳过启动页的情况（可选功能）
     */
    fun skipSplash() {
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        checkAppStateAndNavigate()
    }

    /**
     * 延长启动页显示时间（可选功能）
     */
    fun extendSplash(additionalDelay: Long) {
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
        Handler(Looper.getMainLooper()).postDelayed({
            checkAppStateAndNavigate()
        }, SPLASH_DELAY + additionalDelay)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清理Handler，避免内存泄漏
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
    }
}