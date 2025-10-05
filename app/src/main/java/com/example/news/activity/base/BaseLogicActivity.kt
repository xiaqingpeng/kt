package com.example.news.activity.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * 业务逻辑层 - 添加 ViewModel 支持
 */
abstract class BaseLogicActivity<VB : ViewBinding> : BaseCommonActivity<VB>() {

    /**
     * 创建 ViewModel
     */
    protected inline fun <reified T : ViewModel> createViewModel(): T {
        return ViewModelProvider(this)[T::class.java]
    }

    /**
     * 创建带 Factory 的 ViewModel
     */
    protected inline fun <reified T : ViewModel> createViewModel(factory: ViewModelProvider.Factory): T {
        return ViewModelProvider(this, factory)[T::class.java]
    }

    /**
     * 处理加载状态
     */
    protected open fun handleLoadingState(isLoading: Boolean) {
        if (isLoading) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    /**
     * 处理错误状态
     */
    protected open fun handleError(error: Throwable?) {
        hideLoading()
        showToast(error?.message ?: "发生错误，请重试")
    }

    /**
     * 处理空数据状态
     */
    protected open fun handleEmptyData() {
        hideLoading()
        // 显示空数据UI
    }
}