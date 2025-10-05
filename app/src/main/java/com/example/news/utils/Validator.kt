package com.example.news.utils

import android.util.Patterns

object Validator {

    fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "请输入邮箱"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "邮箱格式不正确"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "请输入密码"
            password.length < 6 -> "密码至少6位"
            else -> null
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isEmpty() -> "请确认密码"
            password != confirmPassword -> "两次输入的密码不一致"
            else -> null
        }
    }

    fun validateUsername(username: String): String? {
        return when {
            username.isEmpty() -> "请输入用户名"
            username.length < 2 -> "用户名至少2位"
            username.length > 20 -> "用户名不能超过20位"
            else -> null
        }
    }
}