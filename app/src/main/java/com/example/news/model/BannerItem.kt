package com.example.news.model

/**
 * 轮播图数据模型
 */
data class BannerItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val linkUrl: String? = null,
    val description: String? = null
)
