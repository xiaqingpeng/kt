package com.example.news.network

/**
 * 简单的内存 Token 存储，用于拦截器附加认证头。
 * 如需持久化可改为使用 SharedPreferences。
 */
object TokenStore {
    @Volatile
    private var token: String? = null

    fun setToken(value: String?) {
        token = value?.trim()?.takeIf { it.isNotEmpty() }
    }

    fun getToken(): String? = token

    fun clear() { token = null }
}

