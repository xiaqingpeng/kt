package com.example.news.activity.profile

import android.content.Intent
import com.example.news.activity.base.BaseTitleActivity
import com.example.news.databinding.ActivityDeviceManageBinding

class DeviceManageActivity : BaseTitleActivity<ActivityDeviceManageBinding>() {

    override fun getViewBinding(): ActivityDeviceManageBinding {
        return ActivityDeviceManageBinding.inflate(layoutInflater)
    }

    override fun getToolbar() = binding.toolbar

    override fun getToolbarTitle(): String {
        return "设备管理"
    }

    companion object {
        private const val REQUEST_CODE_SCAN = 1001
    }

    /**
     * 初始化视图
     */
    override fun initView() {
        super.initView() // 这会自动调用 BaseTitleActivity 的 setupToolbar()
        // 不需要额外初始化按钮，因为可以直接使用 binding
    }

    /**
     * 设置监听器
     */
    override fun setListeners() {
        binding.btnScanQrCode.setOnClickListener {
            startQrCodeScan()
        }

        binding.btnAddDevice.setOnClickListener {
            // 手动添加设备
            showToast("手动添加设备")
        }
    }

    /**
     * 观察数据变化（可根据需要实现）
     */
    override fun observeData() {
        // 这里可以添加LiveData观察等
    }

    private fun startQrCodeScan() {
        try {
            // 使用 ZXing 或其他二维码扫描库
            val intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            startActivityForResult(intent, REQUEST_CODE_SCAN)
        } catch (e: Exception) {
            // 如果设备上没有安装二维码扫描应用，提示用户
            showLongToast("请安装二维码扫描应用")

            // 或者使用内置的扫描功能
            // startInternalQrCodeScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == RESULT_OK) {
                val contents = data?.getStringExtra("SCAN_RESULT")
                if (!contents.isNullOrEmpty()) {
                    // 处理扫描结果
                    processScanResult(contents)
                }
            }
        }
    }

    private fun processScanResult(qrContent: String) {
        // 解析二维码内容并添加设备
        showToast("扫描到设备二维码: $qrContent")

        // 这里可以添加设备绑定逻辑
        // bindDevice(qrContent)
    }
}