package com.example.news.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * 统计接口请求耗时并上报到 /system/logs/report。
 * - 仅在 ENABLED=true 时生效
 * - 避免对统计接口自身再次埋点（防递归）
 */
class StatsInterceptor : Interceptor {
    companion object {
        var ENABLED: Boolean = true
    }

    private val logClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!ENABLED) return chain.proceed(request)

        val path = request.url.encodedPath
        // 避免对统计接口自身埋点，防止递归
        if (path.endsWith("/system/logs/report")) {
            return chain.proceed(request)
        }

        val startNs = System.nanoTime()
        var statusCode = -1
        var response: Response? = null
        try {
            response = chain.proceed(request)
            statusCode = response.code
        } catch (e: Exception) {
            // 请求失败也尝试上报（可能同样失败，静默处理）
        }
        val endNs = System.nanoTime()
        val durationMs = (endNs - startNs) / 1_000_000.0

        // 记录时间为 UTC ISO8601
        val isoFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val requestTime = isoFmt.format(java.util.Date())

        // 构造上报 JSON
        val statsJson = """
            {
              "path": "${request.url.encodedPath}",
              "method": "${request.method}",
              "requestTime": "$requestTime",
              "durationMs": ${"%.3f".format(durationMs)},
              "platform": "Android",
              "statusCode": $statusCode
            }
        """.trimIndent()

        // 目标上报 URL：保持同主机同端口，仅替换路径
        val statsUrl: HttpUrl = request.url.newBuilder()
            .encodedPath("/system/logs/report")
            .query(null)
            .build()

        val mediaType = "application/json".toMediaType()
        val body = statsJson.toRequestBody(mediaType)
        val statsReqBuilder = Request.Builder()
            .url(statsUrl)
            .post(body)

        // 复制/补充常用头，尤其是认证头，避免 403
        val originAuth = request.header("Authorization")
        val tokenAuth = TokenStore.getToken()?.let { "Bearer $it" }
        val authToUse = originAuth ?: tokenAuth
        if (!authToUse.isNullOrBlank()) {
            statsReqBuilder.header("Authorization", authToUse)
        }
        statsReqBuilder.header("Accept", "application/json")
        // 虽然 RequestBody 已含有 MediaType，这里显式设置以便部分后端校验
        statsReqBuilder.header("Content-Type", "application/json")

        val statsRequest: Request = statsReqBuilder.build()

        // 异步上报，不阻塞主请求
        logClient.newCall(statsRequest).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                // 静默失败即可，避免噪音
            }

            override fun onResponse(call: okhttp3.Call, statsResp: okhttp3.Response) {
                statsResp.close()
            }
        })

        return response ?: throw java.io.IOException("request failed before reporting")
    }
}
