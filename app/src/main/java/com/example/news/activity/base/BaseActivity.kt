package com.example.news.activity.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * 增强版 BaseActivity
 * 包含权限处理、通用功能等
 */
abstract class BaseActivity : AppCompatActivity() {

    private var permissionCallback: ((Boolean) -> Unit)? = null
    private var requestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        initView()
        setListeners()
        observeData()
    }

    /**
     * 获取布局资源ID
     */
    abstract fun getLayoutResId(): Int

    /**
     * 初始化视图
     */
    protected open fun initView() {}

    /**
     * 设置监听器
     */
    protected open fun setListeners() {}

    /**
     * 观察数据变化
     */
    protected open fun observeData() {}

    /**
     * 显示Toast消息
     */
    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示长Toast消息
     */
    protected fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 导航到其他Activity
     */
    protected fun navigateTo(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

    /**
     * 设置Toolbar
     */
    protected fun setupToolbar(toolbar: androidx.appcompat.widget.Toolbar, title: String, showBackButton: Boolean = true) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        if (showBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    /**
     * 打开网页
     */
    protected open fun openWebPage(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            showToast("无法打开网页")
        }
    }

    /**
     * 检查权限
     */
    protected fun checkPermission(permission: String, callback: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            callback(true)
        } else {
            permissionCallback = callback
            requestCode++
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }

    /**
     * 处理权限请求结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            permissionCallback?.invoke(granted)
            permissionCallback = null
        }
    }

    /**
     * 显示加载对话框（需要子类实现或使用第三方库）
     */
    protected open fun showLoading() {
        // 实现加载对话框
    }

    /**
     * 隐藏加载对话框
     */
    protected open fun hideLoading() {
        // 隐藏加载对话框
    }
}