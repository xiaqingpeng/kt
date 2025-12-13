package com.example.news.network

// 导入StatsInterceptor类
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 自定义拦截器，用于将OkHttp请求转换为curl命令格式并打印
 */
class CurlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val curlCommand = buildCurlCommand(request)
        Log.d("CurlCommand", curlCommand)
        return chain.proceed(request)
    }

    private fun buildCurlCommand(request: Request): String {
        val sb = StringBuilder()
        sb.append("curl -v -X ${request.method}")

        // 添加请求头
        request.headers.forEach { header ->
            sb.append(" -H \"${header.first}: ${header.second}\"")
        }

        // 添加请求体（简化版，避免使用Buffer类）
        val requestBody = request.body
        if (requestBody != null) {
            sb.append(" -d [request body]")
        }

        // 添加URL
        sb.append(" \"${request.url}\"")

        return sb.toString()
    }
}

object RetrofitClient {
    // 真机通过 adb reverse 访问宿主机：127.0.0.1；必须以 / 结尾
    private const val BASE_URL = "http://120.48.95.51:7001/"

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(CurlInterceptor()) // 添加curl命令打印拦截器
            .addInterceptor(StatsInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
