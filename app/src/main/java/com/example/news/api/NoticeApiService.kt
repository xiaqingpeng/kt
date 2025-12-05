package com.example.news.api

import com.example.news.model.NoticeEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NoticeApiService {
    @GET("system/notice/db/list")
    suspend fun getNoticeList(
        @Query("pageSize") pageSize: Int? = null,
        @Query("pageNum") pageNum: Int? = null,
        @Query("noticeType") noticeType: String? = null
    ): Response<NoticeEntity>
}
