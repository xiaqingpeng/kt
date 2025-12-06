package com.example.news.activity.auth

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.activity.base.BaseLogicActivity
import com.example.news.databinding.ActivityLoginRegisterBinding
import com.example.news.viewmodel.LoginRegisterViewModel

class LoginRegisterActivity : BaseLogicActivity<ActivityLoginRegisterBinding>() {

    private lateinit var viewModel: LoginRegisterViewModel

    // 当前模式：true为登录，false为注册
    private var isLoginMode = true

    override fun getViewBinding(): ActivityLoginRegisterBinding {
        return ActivityLoginRegisterBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        setupToolbar()
        initViewModel()
        setupViewPager()
        setupListeners()
        setupTextWatchers()
        updateUIForMode(isLoginMode)
    }

    override fun initData() {
        // 检查是否有自动登录的token或保存的用户信息
        viewModel.checkAutoLogin()
    }

    override fun setListeners() {
        // 已经在initView中设置
    }

    override fun observeData() {
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.login_title)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initViewModel() {
        viewModel = createViewModel()
    }

    private fun setupViewPager() {
        // 如果使用ViewPager来切换登录注册，可以在这里设置
        // 这里我们使用简单的布局切换
    }

    private fun setupListeners() {
        // 登录/注册按钮
        binding.btnSubmit.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performRegister()
            }
        }

        // 切换模式按钮
        binding.tvSwitchMode.setOnClickListener {
            toggleMode()
        }

        // 忘记密码
        binding.tvForgotPassword.setOnClickListener {
            navigateToForgotPassword()
        }

        // 第三方登录
        binding.ivWechatLogin.setOnClickListener {
            performWechatLogin()
        }

        binding.ivQqLogin.setOnClickListener {
            performQQLogin()
        }

        binding.ivWeiboLogin.setOnClickListener {
            performWeiboLogin()
        }
    }

    private fun setupTextWatchers() {
        // 实时验证输入
        binding.etEmail.addTextChangedListener(afterTextChanged = { text ->
            viewModel.email = text?.toString() ?: ""
            validateForm()
        })

        binding.etPassword.addTextChangedListener(afterTextChanged = { text ->
            viewModel.password = text?.toString() ?: ""
            validateForm()
        })

        binding.etConfirmPassword.addTextChangedListener(afterTextChanged = { text ->
            viewModel.confirmPassword = text?.toString() ?: ""
            validateForm()
        })

        binding.etUsername.addTextChangedListener(afterTextChanged = { text ->
            viewModel.username = text?.toString() ?: ""
            validateForm()
        })
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            handleLoadingState(false)
            when (result) {
                    is LoginRegisterViewModel.AuthResult.Success -> {
                        showToast(getString(R.string.login_success))
                        // 跳转到主页面
                        navigateToMain()
                    }

                is LoginRegisterViewModel.AuthResult.Error -> {
                    handleError(result.exception)
                }
            }
        }

        viewModel.registerResult.observe(this) {
            handleLoadingState(false)
            when (it) {
                is LoginRegisterViewModel.AuthResult.Success -> {
                    showToast(getString(R.string.register_success))
                    // 注册成功后自动切换到登录模式
                    toggleMode(true)
                }

                is LoginRegisterViewModel.AuthResult.Error -> {
                    handleError(it.exception)
                }
            }
        }

        viewModel.formValidation.observe(this) { validation ->
            updateSubmitButton(validation.isValid)
            if (!validation.emailError.isNullOrEmpty()) {
                binding.tilEmail.error = validation.emailError
            } else {
                binding.tilEmail.error = null
            }

            if (!validation.passwordError.isNullOrEmpty()) {
                binding.tilPassword.error = validation.passwordError
            } else {
                binding.tilPassword.error = null
            }

            if (!validation.confirmPasswordError.isNullOrEmpty()) {
                binding.tilConfirmPassword.error = validation.confirmPasswordError
            } else {
                binding.tilConfirmPassword.error = null
            }

            if (!validation.usernameError.isNullOrEmpty()) {
                binding.tilUsername.error = validation.usernameError
            } else {
                binding.tilUsername.error = null
            }
        }

        viewModel.loadingState.observe(this) { isLoading ->
            handleLoadingState(isLoading)
        }
    }

    private fun toggleMode(toLogin: Boolean? = null) {
        isLoginMode = toLogin ?: !isLoginMode
        updateUIForMode(isLoginMode)
        clearForm()
    }

    private fun updateUIForMode(isLogin: Boolean) {
        if (isLogin) {
            // 登录模式
            supportActionBar?.title = getString(R.string.login_title)
            binding.btnSubmit.text = getString(R.string.login)
            binding.tvSwitchMode.text = getString(R.string.switch_to_register)
            binding.tvSwitchModeHint.text = getString(R.string.or_login_with)

            // 隐藏注册相关字段
            binding.tilUsername.visibility = View.GONE
            binding.tilConfirmPassword.visibility = View.GONE

            // 显示忘记密码
            binding.tvForgotPassword.visibility = View.VISIBLE
        } else {
            // 注册模式
            supportActionBar?.title = getString(R.string.register_title)
            binding.btnSubmit.text = getString(R.string.register)
            binding.tvSwitchMode.text = getString(R.string.switch_to_login)
            binding.tvSwitchModeHint.text = getString(R.string.or_register_with)

            // 显示注册相关字段
            binding.tilUsername.visibility = View.VISIBLE
            binding.tilConfirmPassword.visibility = View.VISIBLE

            // 隐藏忘记密码
            binding.tvForgotPassword.visibility = View.GONE
        }

        validateForm()
    }

    private fun performLogin() {
        if (validateForm()) {
            handleLoadingState(true)
            viewModel.login(this)
        }
    }

    private fun performRegister() {
        if (validateForm()) {
            handleLoadingState(true)
            viewModel.register()
        }
    }

    private fun validateForm(): Boolean {
        return viewModel.validateForm(isLoginMode)
    }

    private fun updateSubmitButton(isValid: Boolean) {
        binding.btnSubmit.isEnabled = isValid
        binding.btnSubmit.alpha = if (isValid) 1.0f else 0.5f
    }

    private fun clearForm() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etConfirmPassword.text?.clear()
        binding.etUsername.text?.clear()

        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
        binding.tilUsername.error = null
    }

    private fun navigateToForgotPassword() {
        showToast(getString(R.string.navigate_to_forgot_password))
        // navigateTo(ForgotPasswordActivity::class.java)
    }

    private fun navigateToMain() {
        // 跳转到主页面并关闭当前Activity，同时清除任务栈
        navigateTo(MainActivity::class.java, finishCurrent = true, clearTask = true)
    }

    private fun performWechatLogin() {
        showToast(getString(R.string.wechat_login))
        // 实现微信登录逻辑
    }

    private fun performQQLogin() {
        showToast(getString(R.string.qq_login))
        // 实现QQ登录逻辑
    }

    private fun performWeiboLogin() {
        showToast(getString(R.string.weibo_login))
        // 实现微博登录逻辑
    }

    override fun handleLoadingState(isLoading: Boolean) {
        super.handleLoadingState(isLoading)
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSubmit.isEnabled = !isLoading
    }

    override fun handleError(error: Throwable?) {
        super.handleError(error)
        // 可以在这里添加特定的错误处理逻辑
        showToast(error?.message ?: getString(R.string.operation_failed))
    }

    companion object
}