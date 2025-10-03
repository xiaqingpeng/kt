package com.example.news.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.news.R

class DeviceManageActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var btnScanQrCode: Button
    private lateinit var btnAddDevice: Button

    companion object {
        private const val REQUEST_CODE_SCAN = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_manage)



        initView()
        setClickListeners()
    }

    private fun initView() {
        // 初始化 Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 显示返回按钮和设置标题
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "设备管理" // 这里设置标题

        btnScanQrCode = findViewById(R.id.btnScanQrCode)
        btnAddDevice = findViewById(R.id.btnAddDevice)
    }

    private fun setClickListeners() {
        btnScanQrCode.setOnClickListener {
            startQrCodeScan()
        }

        btnAddDevice.setOnClickListener {
            // 手动添加设备
            Toast.makeText(this, "手动添加设备", Toast.LENGTH_SHORT).show()
        }

        // Toolbar 返回按钮点击事件
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun startQrCodeScan() {
        try {
            // 使用 ZXing 或其他二维码扫描库
            val intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            startActivityForResult(intent, REQUEST_CODE_SCAN)
        } catch (e: Exception) {
            // 如果设备上没有安装二维码扫描应用，提示用户
            Toast.makeText(this, "请安装二维码扫描应用", Toast.LENGTH_SHORT).show()

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
        Toast.makeText(this, "扫描到设备二维码: $qrContent", Toast.LENGTH_SHORT).show()

        // 这里可以添加设备绑定逻辑
        // bindDevice(qrContent)
    }

    // 支持物理返回键
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}