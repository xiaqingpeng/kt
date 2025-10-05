package com.example.news.activity.profile

import com.example.news.activity.base.BaseTitleActivity
import com.example.news.databinding.ActivityDeviceManageBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class DeviceManageActivity : BaseTitleActivity<ActivityDeviceManageBinding>() {

    override fun getViewBinding(): ActivityDeviceManageBinding {
        return ActivityDeviceManageBinding.inflate(layoutInflater)
    }

    override fun getToolbar() = binding.toolbar

    override fun getToolbarTitle(): String {
        return "设备管理"
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            showToast("扫描已取消")
        } else {
            processScanResult(result.contents)
        }
    }

    override fun initView() {
        super.initView()
    }

    override fun setListeners() {
        binding.btnScanQrCode.setOnClickListener {
            startQrCodeScan()
        }

        binding.btnAddDevice.setOnClickListener {
            showToast("手动添加设备")
        }
    }

    override fun observeData() {
        // 可以添加LiveData观察等
    }

    private fun startQrCodeScan() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("请对准二维码进行扫描")
        options.setCameraId(0)
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)

        // 关键修改：创建自定义的竖屏扫描 Activity
        options.setCaptureActivity(PortraitCaptureActivity::class.java)

        barcodeLauncher.launch(options)
    }

    private fun processScanResult(qrContent: String) {
        showToast("扫描到设备二维码: $qrContent")
    }
}