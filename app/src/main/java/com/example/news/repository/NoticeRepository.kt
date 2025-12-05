package com.example.news.repository

import com.example.news.api.NoticeApiService
import com.example.news.model.NoticeItemEntity
import com.example.news.network.RetrofitClient

class NoticeRepository {
    private val api: NoticeApiService = RetrofitClient.retrofit.create(NoticeApiService::class.java)

    suspend fun fetchNoticeList(pageNum: Int? = null, pageSize: Int? = null, noticeType: String? = null): Result<List<NoticeItemEntity>> {
        return try {
            val response = api.getNoticeList(pageSize = pageSize, pageNum = pageNum, noticeType = noticeType)
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(body?.rows.orEmpty())
            } else {
                Result.failure(RuntimeException("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
