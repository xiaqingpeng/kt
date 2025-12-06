package com.example.news.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.api.AuthApiService
import com.example.news.api.AuthResponse
import com.example.news.api.LoginRequest
import com.example.news.api.RegisterRequest
import com.example.news.manager.LoginManager
import com.example.news.manager.ValidatorManager
import com.example.news.network.RetrofitClient
import com.example.news.network.TokenStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val emailError =ValidatorManager.validateEmail(email)
        val passwordError = ValidatorManager.validatePassword(password)
        var confirmPasswordError: String? = null
        var usernameError: String? = null

        if (!isLoginMode) {
            confirmPasswordError = ValidatorManager.validateConfirmPassword(password, confirmPassword)
            usernameError = ValidatorManager.validateUsername(username)
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authApi = RetrofitClient.retrofit.create(AuthApiService::class.java)
                val response = authApi.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        // 根据API返回的code判断成功与否，code=0表示成功
                        if (authResponse.code == 0) {
                            // 保存登录状态和用户信息
                            LoginManager.setLoginState(
                                context = context,
                                isLoggedIn = true,
                                userId = authResponse.data?.id?.toString() ?: "",
                                userName = authResponse.data?.username ?: "",
                                userEmail = authResponse.data?.email ?: "",
                                userToken = "", // 实际API响应中没有token字段
                                rememberMe = true
                            )
                            // 更新TokenStore（实际API响应中没有token字段，这里可以根据需要调整）
                            // TokenStore.setToken("")
                            _loginResult.postValue(AuthResult.Success(authResponse))
                        } else {
                            _loginResult.postValue(AuthResult.Error(
                                Exception(authResponse.msg ?: "登录失败")
                            ))
                        }
                    } else {
                        _loginResult.postValue(AuthResult.Error(
                            Exception("登录失败：服务器返回空响应")
                        ))
                    }
                } else {
                    _loginResult.postValue(AuthResult.Error(
                        Exception("登录失败: ${response.code()} ${response.message()}")
                    ))
                }
            } catch (e: Exception) {
                _loginResult.postValue(AuthResult.Error(e))
            } finally {
                _loadingState.postValue(false)
            }
        }
    }

    fun register() {
        _loadingState.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authApi = RetrofitClient.retrofit.create(AuthApiService::class.java)
                val response = authApi.register(RegisterRequest(username, email, password, confirmPassword))

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        // 根据API返回的code判断成功与否，code=0表示成功
                        if (authResponse.code == 0) {
                            _registerResult.postValue(AuthResult.Success(authResponse))
                        } else {
                            _registerResult.postValue(AuthResult.Error(
                                Exception(authResponse.msg ?: "注册失败")
                            ))
                        }
                    } else {
                        _registerResult.postValue(AuthResult.Error(
                            Exception("注册失败：服务器返回空响应")
                        ))
                    }
                } else {
                    _registerResult.postValue(AuthResult.Error(
                        Exception("注册失败: ${response.code()} ${response.message()}")
                    ))
                }
            } catch (e: Exception) {
                _registerResult.postValue(AuthResult.Error(e))
            } finally {
                _loadingState.postValue(false)
            }
        }
    }


    fun checkAutoLogin() {
        // 检查本地存储的token或用户信息
        // 如果有有效的token，直接跳转到主页面
    }

    sealed class AuthResult {
        data class Success(val response: com.example.news.api.AuthResponse) : AuthResult()
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