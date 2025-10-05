package com.example.news.activity.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

/**
 * 增强版 BaseActivity - 基础功能层
 * 包含权限处理、通用功能等
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    private var permissionCallback: ((Boolean) -> Unit)? = null
    private var requestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        initView()
        setListeners()
        observeData()
        initData()
    }

    /**
     * 获取 ViewBinding
     */
    protected abstract fun getViewBinding(): VB

    /**
     * 初始化视图
     */
    protected open fun initView() {}

    /**
     * 初始化数据
     */
    protected open fun initData() {}

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
    protected fun navigateTo(clazz: Class<*>, finishCurrent: Boolean = false) {
        startActivity(Intent(this, clazz))
        if (finishCurrent) {
            finish()
        }
    }

    /**
     * 导航并传递数据
     */
    protected fun <T> navigateToWithData(clazz: Class<*>, key: String, data: T, finishCurrent: Boolean = false) {
        val intent = Intent(this, clazz).apply {
            putExtra(key, data as? java.io.Serializable)
        }
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
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
     * 检查单个权限
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
     * 检查多个权限
     */
    protected fun checkMultiplePermissions(permissions: Array<String>, callback: (Boolean) -> Unit) {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            callback(true)
        } else {
            permissionCallback = callback
            requestCode++
            ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), requestCode)
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
            val granted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            permissionCallback?.invoke(granted)
            permissionCallback = null
        }
    }

    /**
     * 显示加载对话框
     */
    protected open fun showLoading(message: String = "加载中...") {
        // 实现加载对话框 - 可以使用 ProgressDialog 或自定义 Dialog
    }

    /**
     * 隐藏加载对话框
     */
    protected open fun hideLoading() {
        // 隐藏加载对话框
    }

    /**
     * 安全执行操作（避免在 onDestroy 后执行 UI 操作）
     */
    protected fun safeRunOnUiThread(action: () -> Unit) {
        if (!isFinishing && !isDestroyed) {
            runOnUiThread(action)
        }
    }
}