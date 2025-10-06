package com.example.news.manager

import android.content.Context
import android.content.SharedPreferences

/**
 * 登录状态管理工具类
 */
object LoginManager {

    private const val PREFS_NAME = "app_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_TOKEN = "user_token"
    private const val KEY_LOGIN_TIME = "login_time"
    private const val KEY_REMEMBER_ME = "remember_me"
    private const val KEY_FIRST_LAUNCH = "is_first_launch"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 设置登录状态
     */
    fun setLoginState(
        context: Context,
        isLoggedIn: Boolean,
        userId: String? = null,
        userName: String? = null,
        userEmail: String? = null,
        userToken: String? = null,
        rememberMe: Boolean = false
    ) {
        val editor = getSharedPreferences(context).edit()

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.putLong(KEY_LOGIN_TIME, System.currentTimeMillis())
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe)

        if (isLoggedIn) {
            // 登录时保存用户信息
            userId?.let { editor.putString(KEY_USER_ID, it) }
            userName?.let { editor.putString(KEY_USER_NAME, it) }
            userEmail?.let { editor.putString(KEY_USER_EMAIL, it) }
            userToken?.let { editor.putString(KEY_USER_TOKEN, it) }
        } else {
            // 登出时清除敏感信息
            if (!rememberMe) {
                editor.remove(KEY_USER_ID)
                editor.remove(KEY_USER_NAME)
                editor.remove(KEY_USER_EMAIL)
                editor.remove(KEY_USER_TOKEN)
            }
        }

        editor.apply()
    }

    /**
     * 检查是否已登录
     */
    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * 获取用户ID
     */
    fun getUserId(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_ID, null)
    }

    /**
     * 获取用户名
     */
    fun getUserName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_NAME, null)
    }

    /**
     * 获取用户邮箱
     */
    fun getUserEmail(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, null)
    }

    /**
     * 获取用户Token
     */
    fun getUserToken(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_TOKEN, null)
    }

    /**
     * 获取登录时间
     */
    fun getLoginTime(context: Context): Long {
        return getSharedPreferences(context).getLong(KEY_LOGIN_TIME, 0)
    }

    /**
     * 检查是否记住登录状态
     */
    fun isRememberMe(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_REMEMBER_ME, false)
    }

    /**
     * 清除所有登录信息
     */
    fun clearLoginInfo(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_IS_LOGGED_IN)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_USER_NAME)
        editor.remove(KEY_USER_EMAIL)
        editor.remove(KEY_USER_TOKEN)
        editor.remove(KEY_LOGIN_TIME)
        editor.remove(KEY_REMEMBER_ME)
        editor.apply()
    }

    /**
     * 检查token是否过期（默认7天）
     */
    fun isTokenExpired(context: Context, expireDays: Int = 7): Boolean {
        val loginTime = getLoginTime(context)
        if (loginTime == 0L) return true

        val expireMillis = expireDays * 24 * 60 * 60 * 1000L
        return System.currentTimeMillis() - loginTime > expireMillis
    }

    /**
     * 检查是否是首次启动
     */
    fun isFirstLaunch(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_FIRST_LAUNCH, true)
    }

    /**
     * 设置非首次启动
     */
    fun setNotFirstLaunch(context: Context) {
        getSharedPreferences(context).edit()
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()
    }

    /**
     * 获取用户信息摘要
     */
    fun getUserSummary(context: Context): Map<String, Any?> {
        return mapOf(
            "isLoggedIn" to isLoggedIn(context),
            "userId" to getUserId(context),
            "userName" to getUserName(context),
            "userEmail" to getUserEmail(context),
            "loginTime" to getLoginTime(context),
            "rememberMe" to isRememberMe(context)
        )
    }

    /**
     * 检查token是否有效
     */
    fun isTokenValid(context: Context, expireDays: Int = 7): Boolean {
        if (!isLoggedIn(context)) return false

        val loginTime = getLoginTime(context)
        if (loginTime == 0L) return false

        val expireMillis = expireDays * 24 * 60 * 60 * 1000L
        return System.currentTimeMillis() - loginTime <= expireMillis
    }
}