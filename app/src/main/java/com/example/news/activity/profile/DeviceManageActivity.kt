package com.example.news.activity.profile

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.activity.base.BaseTitleActivity
import com.example.news.adapter.DeviceAdapter
import com.example.news.databinding.ActivityDeviceManageBinding
import com.example.news.manager.DeviceManager
import com.example.news.model.DeviceInfo
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class DeviceManageActivity : BaseTitleActivity<ActivityDeviceManageBinding>() {

    private lateinit var deviceAdapter: DeviceAdapter

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
        setupRecyclerView()
        updateDeviceList()
    }

    override fun setListeners() {

        binding.toolbar.setOnClickListener {
            onBackPressed();
        }
        binding.btnScanQrCode.setOnClickListener {
            startQrCodeScan()
        }

        binding.btnAddDevice.setOnClickListener {
            showToast("手动添加设备")
            // 这里可以添加手动输入设备信息的逻辑
        }
    }

    override fun observeData() {
        // 可以添加LiveData观察等
    }

    private fun setupRecyclerView() {
        deviceAdapter = DeviceAdapter(
            onDeviceClickListener = { device ->
                showToast("点击设备: ${device.getDisplayName()}")
                // 这里可以跳转到设备详情页面
            },
            onDeviceLongClickListener = { device ->
                showDeleteDeviceDialog(device)
            }
        )

        binding.recyclerViewDevices.apply {
            layoutManager = LinearLayoutManager(this@DeviceManageActivity)
            adapter = deviceAdapter
        }
    }

    private fun updateDeviceList() {
        val devices = DeviceManager.getDevices()
        deviceAdapter.updateDevices(devices)

        // 显示/隐藏空状态
        if (devices.isEmpty()) {
            binding.tvNoDevices.visibility = android.view.View.VISIBLE
            binding.recyclerViewDevices.visibility = android.view.View.GONE
        } else {
            binding.tvNoDevices.visibility = android.view.View.GONE
            binding.recyclerViewDevices.visibility = android.view.View.VISIBLE
        }
    }

    private fun startQrCodeScan() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("请对准设备二维码进行扫描")
        options.setCameraId(0)
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        options.setCaptureActivity(PortraitCaptureActivity::class.java)

        barcodeLauncher.launch(options)
    }

    private fun processScanResult(qrContent: String) {
        try {
            // 添加设备到管理器
            val device = DeviceManager.addDevice(qrContent)

            // 显示成功消息
            showToast("成功添加设备: ${device.getDisplayName()}")

            // 更新设备列表
            updateDeviceList()

            // 这里可以添加设备绑定逻辑
            // bindDevice(device)

        } catch (e: Exception) {
            showToast("添加设备失败: ${e.message}")
        }
    }

    private fun showDeleteDeviceDialog(device: DeviceInfo) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("删除设备")
            .setMessage("确定要删除设备 \"${device.getDisplayName()}\" 吗？")
            .setPositiveButton("删除") { dialog, which ->
                val success = DeviceManager.removeDevice(device.id)
                if (success) {
                    showToast("设备已删除")
                    updateDeviceList()
                } else {
                    showToast("删除失败")
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
}