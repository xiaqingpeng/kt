package com.example.news.fragment.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private var requestCode = 0
    private var permissionCallback: ((Boolean) -> Unit)? = null

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
        } catch (_: Exception) {
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
     * 安全执行操作并返回结果
     */
    protected fun <T> safeRunWithResult(defaultValue: T, block: () -> T): T {
        return if (isAdded && !isDetached && context != null && view != null) {
            block()
        } else {
            defaultValue
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

    /**
     * ==================== 权限相关方法 ====================
     */

    /**
     * 检查单个权限
     */
    protected fun checkPermission(permission: String, callback: (Boolean) -> Unit) {
        safeRun {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                callback(true)
            } else {
                permissionCallback = callback
                requestCode++
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
            }
        }
    }

    /**
     * 检查多个权限
     */
    protected fun checkMultiplePermissions(permissions: Array<String>, callback: (Boolean) -> Unit) {
        safeRun {
            val deniedPermissions = permissions.filter {
                ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
            }

            if (deniedPermissions.isEmpty()) {
                callback(true)
            } else {
                permissionCallback = callback
                requestCode++
                ActivityCompat.requestPermissions(requireActivity(), deniedPermissions.toTypedArray(), requestCode)
            }
        }
    }

    /**
     * 检查权限是否已授予
     */
    protected fun isPermissionGranted(permission: String): Boolean {
        return safeRunWithResult(false) {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 检查多个权限是否都已授予
     */
    protected fun arePermissionsGranted(permissions: Array<String>): Boolean {
        return safeRunWithResult(false) {
            permissions.all { permission ->
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
            }
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
}