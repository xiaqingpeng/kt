package com.example.news.model

data class NotificationItem(
    val id: String,        // 用于 DiffUtil 的唯一标识
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean = false
)