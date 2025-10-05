// DeviceInfo.kt
package com.example.news.model

import java.util.Date

data class DeviceInfo(
    val id: String = "",
    val deviceType: DeviceType = DeviceType.UNKNOWN,
    val qrContent: String = "",
    val deviceName: String = "",
    val scanTime: Date = Date(),
    val isConnected: Boolean = false
) {
    fun getDisplayName(): String {
        return if (deviceName.isNotEmpty()) deviceName else "${deviceType.displayName}-${id.takeLast(4)}"
    }
}

enum class DeviceType(val displayName: String) {
    SMART_PHONE("智能手机"),
    TABLET("平板电脑"),
    LAPTOP("笔记本电脑"),
    SMART_WATCH("智能手表"),
    SMART_HOME("智能家居"),
    IOT_DEVICE("物联网设备"),
    UNKNOWN("未知设备")
}