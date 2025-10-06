// DeviceManager.kt
package com.example.news.manager

import com.example.news.model.DeviceInfo
import com.example.news.model.DeviceType

object DeviceManager {
    private val deviceList = mutableListOf<DeviceInfo>()

    fun addDevice(qrContent: String): DeviceInfo {
        val deviceType = parseDeviceType(qrContent)
        val device = DeviceInfo(
            id = generateDeviceId(),
            deviceType = deviceType,
            qrContent = qrContent,
            deviceName = generateDeviceName(deviceType),
            scanTime = java.util.Date(),
            isConnected = true
        )

        deviceList.add(0, device) // 添加到开头，最新的在前面
        return device
    }

    fun getDevices(): List<DeviceInfo> {
        return deviceList.toList()
    }

    fun removeDevice(deviceId: String): Boolean {
        return deviceList.removeAll { it.id == deviceId }
    }

    fun updateDeviceConnection(deviceId: String, isConnected: Boolean) {
        deviceList.find { it.id == deviceId }?.let { device ->
            deviceList[deviceList.indexOf(device)] = device.copy(isConnected = isConnected)
        }
    }

    private fun parseDeviceType(qrContent: String): DeviceType {
        return when {
            qrContent.contains("phone", ignoreCase = true) -> DeviceType.SMART_PHONE
            qrContent.contains("tablet", ignoreCase = true) -> DeviceType.TABLET
            qrContent.contains("laptop", ignoreCase = true) -> DeviceType.LAPTOP
            qrContent.contains("watch", ignoreCase = true) -> DeviceType.SMART_WATCH
            qrContent.contains("home", ignoreCase = true) -> DeviceType.SMART_HOME
            qrContent.contains("iot", ignoreCase = true) -> DeviceType.IOT_DEVICE
            else -> DeviceType.UNKNOWN
        }
    }

    private fun generateDeviceId(): String {
        return "DEV_${System.currentTimeMillis()}"
    }

    private fun generateDeviceName(deviceType: DeviceType): String {
        val count = deviceList.count { it.deviceType == deviceType } + 1
        return "${deviceType.displayName} $count"
    }
}