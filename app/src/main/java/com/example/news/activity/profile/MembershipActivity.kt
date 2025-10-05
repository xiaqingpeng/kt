package com.example.news.activity.profile

import android.os.Bundle
import android.view.MenuItem
import com.example.news.R
import com.example.news.activity.base.BaseTitleActivity
import com.example.news.databinding.ActivityMembershipBinding

class MembershipActivity : BaseTitleActivity<ActivityMembershipBinding>() {

    private var userId: String? = null
    private var userName: String? = null

    override fun getViewBinding(): ActivityMembershipBinding {
        return ActivityMembershipBinding.inflate(layoutInflater)
    }

    override fun getToolbar() = binding.toolbar

    override fun getToolbarTitle(): String {
        return "我的会员"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowInsets()
    }

    /** 初始化视图 */
    override fun initView() {
        super.initView() // 这会自动调用 BaseTitleActivity 的 setupToolbar()

        // 获取传递的参数
        getIntentExtras()
        setupWindowInsets()
        setupMembershipInfo()
        setupButtons()
    }

    /** 获取 Intent 传递的参数 */
    private fun getIntentExtras() {
        userId = intent.getStringExtra("user_id")
        userName = intent.getStringExtra("user_name")

        // 打印参数用于调试
        println("用户ID: $userId, 用户名: $userName")
    }

    private fun setupWindowInsets() {
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun setupMembershipInfo() {
        // 模拟会员数据
        binding.tvMembershipStatus.text = "当前会员状态：已开通"
        binding.tvMembershipName.text = "当前会员名称：${userName ?: "健身达人"}"
        binding.tvExpireDate.text = getString(R.string.membership_expire_time, "2024-12-31")
        binding.tvMembershipType.text = "会员类型：年度会员"
    }

    private fun setupButtons() {
        binding.btnRenew.setOnClickListener {
            showToast("跳转到续费页面")
        }

        binding.btnUpgrade.setOnClickListener {
            showToast("跳转到升级页面")
        }

        binding.cardBenefit1.setOnClickListener {
            showToast("查看专属课程")
        }

        binding.cardBenefit2.setOnClickListener {
            showToast("查看数据分析")
        }

        binding.cardBenefit3.setOnClickListener {
            showToast("查看优先客服")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}