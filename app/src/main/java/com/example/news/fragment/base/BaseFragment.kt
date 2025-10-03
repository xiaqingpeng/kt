package com.example.news.fragment.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 增强版 BaseFragment
 * 包含协程支持、生命周期感知等功能
 */
abstract class BaseFragment : Fragment() {

    /**
     * 获取布局资源ID
     */
    @LayoutRes
    abstract fun getLayoutResId(): Int

    /**
     * 初始化视图
     */
    protected open fun initView(view: View) {}

    /**
     * 设置监听器
     */
    protected open fun setListeners() {}

    /**
     * 观察数据变化
     */
    protected open fun observeData() {}

    /**
     * Fragment 参数处理
     */
    protected open fun handleArguments() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 处理参数
        handleArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setListeners()
        observeData()
    }

    /**
     * 显示Toast消息
     */
    protected fun showToast(message: String) {
        safeRun {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 显示长Toast消息
     */
    protected fun showLongToast(message: String) {
        safeRun {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 简化视图查找
     */
    protected fun <T : View> View.findViewByIdOrNull(id: Int): T? {
        return try {
            findViewById(id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 安全执行操作（避免Fragment已销毁时的异常）
     */
    protected fun safeRun(block: () -> Unit) {
        if (isAdded && !isDetached && context != null && view != null) {
            block()
        }
    }

    /**
     * 在生命周期范围内启动协程
     */
    protected fun launchOnLifecycle(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            block()
        }
    }

    /**
     * 添加生命周期观察者
     */
    protected fun addLifecycleObserver(observer: (Lifecycle.Event) -> Unit) {
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            observer(event)
        })
    }

    /**
     * 判断Fragment是否可见
     */
    protected fun isFragmentVisible(): Boolean {
        return isAdded && !isHidden && userVisibleHint
    }

    /**
     * ==================== 路由跳转方法 ====================
     */

    /**
     * 导航到其他Activity
     */
    protected fun navigateTo(clazz: Class<*>) {
        safeRun {
            startActivity(Intent(requireContext(), clazz))
        }
    }

    /**
     * 导航到其他Activity并传递数据
     */
    protected fun navigateToWithExtras(clazz: Class<*>, extras: Bundle) {
        safeRun {
            val intent = Intent(requireContext(), clazz)
            intent.putExtras(extras)
            startActivity(intent)
        }
    }

    /**
     * 导航到其他Activity并传递字符串数据
     */
    protected fun navigateToWithString(clazz: Class<*>, key: String, value: String) {
        safeRun {
            val intent = Intent(requireContext(), clazz)
            intent.putExtra(key, value)
            startActivity(intent)
        }
    }

    /**
     * 导航到其他Activity并传递整型数据
     */
    protected fun navigateToWithInt(clazz: Class<*>, key: String, value: Int) {
        safeRun {
            val intent = Intent(requireContext(), clazz)
            intent.putExtra(key, value)
            startActivity(intent)
        }
    }

    /**
     * 导航到其他Activity并清除任务栈
     */
    protected fun navigateToClearTask(clazz: Class<*>) {
        safeRun {
            val intent = Intent(requireContext(), clazz)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    /**
     * 导航到其他Activity并等待结果
     */
    protected fun navigateForResult(clazz: Class<*>, requestCode: Int) {
        safeRun {
            val intent = Intent(requireContext(), clazz)
            startActivityForResult(intent, requestCode)
        }
    }

    /**
     * 带数据导航到其他Activity并等待结果
     */
    protected fun navigateForResultWithExtras(clazz: Class<*>, requestCode: Int, extras: Bundle) {
        safeRun {
            val intent = Intent(requireContext(), clazz)
            intent.putExtras(extras)
            startActivityForResult(intent, requestCode)
        }
    }
}