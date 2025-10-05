package com.example.news.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.utils.LoginManager.setLoginState
import com.example.news.utils.Validator

class LoginRegisterViewModel : ViewModel() {

    var email: String = ""
    var password: String = ""
    var confirmPassword: String = ""
    var username: String = ""

    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult: LiveData<AuthResult> = _loginResult

    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult: LiveData<AuthResult> = _registerResult

    private val _formValidation = MutableLiveData<FormValidation>()
    val formValidation: LiveData<FormValidation> = _formValidation

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun validateForm(isLoginMode: Boolean): Boolean {
        val emailError =Validator.validateEmail(email)
        val passwordError = Validator.validatePassword(password)
        var confirmPasswordError: String? = null
        var usernameError: String? = null

        if (!isLoginMode) {
            confirmPasswordError = Validator.validateConfirmPassword(password, confirmPassword)
            usernameError = Validator.validateUsername(username)
        }

        val isValid = emailError == null &&
                passwordError == null &&
                (isLoginMode || (confirmPasswordError == null && usernameError == null))

        _formValidation.value = FormValidation(
            isValid = isValid,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            usernameError = usernameError
        )

        return isValid
    }

    fun login(context: Context) {
        _loadingState.value = true

        // 模拟网络请求
        Handler(Looper.getMainLooper()).postDelayed({
            _loadingState.value = false



            // 这里应该是真实的登录逻辑
            if (email == "626143872@qq.com" && password == "1994514xia") {
                setLoginState(
                    isLoggedIn = true,
                    userId = "123456",
                    userName = "夏庆鹏",
                    userEmail = "626143872@qq.com",
                    userToken = "1994514xia",
                    rememberMe = true,
                    context = context
                )
                _loginResult.value = AuthResult.Success
            } else {
                _loginResult.value = AuthResult.Error(
                    Exception("邮箱或密码错误")
                )
            }
        }, 2000)
    }

    fun register() {
        _loadingState.value = true

        // 模拟网络请求
        Handler(Looper.getMainLooper()).postDelayed({
            _loadingState.value = false

            // 这里应该是真实的注册逻辑
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                _registerResult.value = AuthResult.Success
            } else {
                _registerResult.value = AuthResult.Error(
                    Exception("注册失败，请重试")
                )
            }
        }, 2000)
    }


    fun checkAutoLogin() {
        // 检查本地存储的token或用户信息
        // 如果有有效的token，直接跳转到主页面
    }

    sealed class AuthResult {
        object Success : AuthResult()
        data class Error(val exception: Exception) : AuthResult()
    }

    data class FormValidation(
        val isValid: Boolean,
        val emailError: String?,
        val passwordError: String?,
        val confirmPasswordError: String?,
        val usernameError: String?
    )
}