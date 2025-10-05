// DeviceAdapter.kt
package com.example.news.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.model.DeviceInfo
import com.example.news.model.DeviceType
import java.text.SimpleDateFormat
import java.util.*

class DeviceAdapter(
    private var devices: List<DeviceInfo> = emptyList(),
    private val onDeviceClickListener: (DeviceInfo) -> Unit = {},
    private val onDeviceLongClickListener: (DeviceInfo) -> Unit = {}
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDeviceIcon: ImageView = itemView.findViewById(R.id.ivDeviceIcon)
        private val tvDeviceName: TextView = itemView.findViewById(R.id.tvDeviceName)
        private val tvDeviceType: TextView = itemView.findViewById(R.id.tvDeviceType)
        private val tvScanTime: TextView = itemView.findViewById(R.id.tvScanTime)
        private val tvQrContent: TextView = itemView.findViewById(R.id.tvQrContent)
        private val ivConnectionStatus: ImageView = itemView.findViewById(R.id.ivConnectionStatus)

        @SuppressLint("SetTextI18n")
        fun bind(device: DeviceInfo) {
            // 设置设备图标
            ivDeviceIcon.setImageResource(getDeviceIcon(device.deviceType))

            // 设置设备名称和类型
            tvDeviceName.text = device.getDisplayName()
            tvDeviceType.text = device.deviceType.displayName

            // 设置扫描时间和二维码内容
            tvScanTime.text = "扫描时间: ${dateFormat.format(device.scanTime)}"
            tvQrContent.text = "设备ID: ${device.qrContent.take(20)}${if (device.qrContent.length > 20) "..." else ""}"

            // 设置连接状态
            ivConnectionStatus.setImageResource(
                if (device.isConnected)  R.drawable.ic_connected else R.drawable.ic_disconnected
            )

            // 设置点击事件
            itemView.setOnClickListener {
                onDeviceClickListener(device)
            }

            itemView.setOnLongClickListener {
                onDeviceLongClickListener(device)
                true
            }
        }

        private fun getDeviceIcon(deviceType: DeviceType): Int {
            return when (deviceType) {
                DeviceType.SMART_PHONE -> android.R.drawable.stat_sys_phone_call
                DeviceType.TABLET -> android.R.drawable.ic_menu_camera
                DeviceType.LAPTOP -> android.R.drawable.ic_menu_edit
                DeviceType.SMART_WATCH -> android.R.drawable.ic_lock_idle_alarm
                DeviceType.SMART_HOME -> android.R.drawable.ic_menu_help
                DeviceType.IOT_DEVICE -> android.R.drawable.ic_menu_manage
                DeviceType.UNKNOWN -> R.drawable.ic_device_unknown
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun updateDevices(newDevices: List<DeviceInfo>) {
        devices = newDevices
        notifyDataSetChanged()
    }
}