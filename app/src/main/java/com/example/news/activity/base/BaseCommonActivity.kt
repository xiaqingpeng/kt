package com.example.news.activity.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding

/**
 * 通用功能层 - 添加网络状态监听、生命周期管理等
 */
abstract class BaseCommonActivity<VB : ViewBinding> : BaseActivity<VB>() {

    private var isNetworkAvailable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 注册网络状态监听
        registerNetworkReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消网络状态监听
        unregisterNetworkReceiver()
    }

    /**
     * 注册网络状态监听
     */
    private fun registerNetworkReceiver() {
        // 实现网络状态监听
    }

    /**
     * 取消网络状态监听
     */
    private fun unregisterNetworkReceiver() {
        // 取消网络状态监听
    }

    /**
     * 网络状态变化回调
     */
    protected open fun onNetworkStateChanged(isAvailable: Boolean) {
        isNetworkAvailable = isAvailable
        if (!isAvailable) {
            showToast("网络连接不可用")
        }
    }

    /**
     * 检查网络是否可用
     */
    protected fun isNetworkAvailable(): Boolean {
        return isNetworkAvailable
    }

    /**
     * 带网络检查的请求
     */
    protected fun <T> withNetworkCheck(block: () -> T) {
        if (isNetworkAvailable()) {
            block()
        } else {
            showToast("请检查网络连接")
        }
    }
}