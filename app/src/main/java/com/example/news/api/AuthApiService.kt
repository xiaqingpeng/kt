package com.example.news.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// 登录请求参数
data class LoginRequest(
    val email: String,
    val password: String
)

// 注册请求参数
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

// 用户信息数据
data class UserData(
    val id: Int,
    val username: String,
    val email: String,
    val createTime: String,
    val updateTime: String
)

// 登录/注册响应数据
data class AuthResponse(
    val code: Int,
    val msg: String,
    val data: UserData?
)

// 将API响应转换为应用内部使用的格式
data class AppAuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val email: String? = null
)

// 扩展函数：将AuthResponse转换为AppAuthResponse
fun AuthResponse.toAppAuthResponse(): AppAuthResponse {
    // code为0表示成功
    val isSuccess = code == 0
    return AppAuthResponse(
        success = isSuccess,
        message = msg,
        token = null, // 实际API响应中没有token字段，需要根据实际情况调整
        userId = data?.id?.toString(),
        username = data?.username,
        email = data?.email
    )
}

interface AuthApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}