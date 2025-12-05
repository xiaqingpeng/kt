package com.example.news.model

data class NoticeEntity(
    val code: Int = 0,
    val msg: String = "",
    val rows: List<NoticeItemEntity> = emptyList(),
    val total: Int = 0
)

data class NoticeItemEntity(
    val createBy: String = "",
    val createTime: String = "",
    val noticeContent: String = "",
    val noticeId: Int = 0,
    val noticeTitle: String = "",
    val noticeType: String = "",
    val remark: String = "",
    val status: String = "",
    val updateBy: String = "",
    val updateTime: String = ""
)

