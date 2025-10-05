package com.example.news.activity.base

import androidx.viewbinding.ViewBinding

/**
 * 带标题栏的基础 Activity
 */
abstract class BaseTitleActivity<VB : ViewBinding> : BaseLogicActivity<VB>() {

    protected abstract fun getToolbar(): androidx.appcompat.widget.Toolbar?

    override fun initView() {
        super.initView()
        setupToolbar()
    }

    /**
     * 设置 Toolbar
     */
    private fun setupToolbar() {
        getToolbar()?.let { toolbar ->
            setupToolbar(
                toolbar = toolbar,
                title = getToolbarTitle(),
                showBackButton = shouldShowBackButton()
            )
        }
    }

    /**
     * 获取 Toolbar 标题
     */
    protected open fun getToolbarTitle(): String {
        return ""
    }

    /**
     * 是否显示返回按钮
     */
    protected open fun shouldShowBackButton(): Boolean {
        return true
    }

    /**
     * 更新 Toolbar 标题
     */
    protected fun updateToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    /**
     * 隐藏 Toolbar
     */
    protected fun hideToolbar() {
        supportActionBar?.hide()
    }

    /**
     * 显示 Toolbar
     */
    protected fun showToolbar() {
        supportActionBar?.show()
    }
}