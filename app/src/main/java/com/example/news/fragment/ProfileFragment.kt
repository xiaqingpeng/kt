package com.example.news.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.news.R
import com.example.news.fragment.base.BaseFragment
import com.example.news.model.FunctionItem

class ProfileFragment : BaseFragment() {

    private val functionItems = mapOf(
        // 我的功能区
        R.id.itemTarget to FunctionItem("目标", android.R.drawable.ic_menu_compass),
        R.id.itemBodyMeasure to FunctionItem("体围", android.R.drawable.ic_menu_edit),
        R.id.itemSportAbility to FunctionItem("运动能力", android.R.drawable.ic_menu_send),
        R.id.itemMonthlyReport to FunctionItem("月度报告", android.R.drawable.ic_menu_month),
        R.id.itemCloseFriends to FunctionItem("亲密好友", android.R.drawable.ic_menu_my_calendar),
        R.id.itemCoupon to FunctionItem("卡券", android.R.drawable.ic_menu_slideshow),

        // 健康服务区
        R.id.itemBurnCamp to FunctionItem("燃烧营", android.R.drawable.ic_menu_delete),
        R.id.itemRecipe to FunctionItem("食谱", android.R.drawable.ic_menu_week),
        R.id.itemDiet to FunctionItem("饮食", android.R.drawable.ic_menu_today),
        R.id.itemHealthTest to FunctionItem("健康测评", android.R.drawable.ic_menu_agenda),
        R.id.itemMall to FunctionItem("商城", android.R.drawable.ic_menu_search),

        // 设置区
        R.id.itemDeviceManage to FunctionItem("设备管理", android.R.drawable.ic_menu_preferences),
        R.id.itemDeviceMessage to FunctionItem("设备消息", android.R.drawable.ic_menu_info_details),

        // 有品区
        R.id.itemAbout to FunctionItem("关于有品", android.R.drawable.ic_dialog_info),
        R.id.itemHelp to FunctionItem("帮助与反馈", android.R.drawable.ic_menu_help),
        R.id.itemLab to FunctionItem("有品实验室", android.R.drawable.ic_menu_set_as)
    )

    /**
     * 获取布局资源ID
     */
    override fun getLayoutResId(): Int {
        return R.layout.fragment_profile
    }

    /**
     * 初始化视图
     */
    override fun initView(view: View) {
        setupUserInfo(view)
        setupFunctionItems(view)
    }

    /**
     * 设置监听器
     */
    override fun setListeners() {
        setupClickListeners()
    }

    /**
     * 观察数据变化
     */
    override fun observeData() {
        // 可以在这里添加用户数据观察
        // 例如：viewModel.userData.observe(viewLifecycleOwner) { user -> updateUserInfo(user) }
    }

    /**
     * 设置用户信息
     */
    private fun setupUserInfo(view: View) {
        safeRun {
            val tvNickname = view.findViewByIdOrNull<TextView>(R.id.tvNickname)
            val tvMembership = view.findViewByIdOrNull<TextView>(R.id.tvMembership)

            tvNickname?.text = "健身达人"
            tvMembership?.text = getString(R.string.membership_expire_time, "2024-12-31")
        }
    }

    /**
     * 设置功能项
     */
    private fun setupFunctionItems(view: View) {
        functionItems.forEach { (viewId, functionItem) ->
            safeRun {
                val itemView = view.findViewByIdOrNull<View>(viewId) ?: return@safeRun
                val ivIcon = itemView.findViewByIdOrNull<ImageView>(R.id.ivIcon)
                val tvTitle = itemView.findViewByIdOrNull<TextView>(R.id.tvTitle)

                ivIcon?.setImageResource(functionItem.iconRes)
                tvTitle?.text = functionItem.title
            }
        }
    }

    /**
     * 设置点击监听器
     */
    private fun setupClickListeners() {
        safeRun {
            val view = view ?: return@safeRun

            // 通知按钮
            view.findViewByIdOrNull<View>(R.id.btnNotification)?.setOnClickListener {
                navigateToNotification()
            }

            // 我的会员按钮
            view.findViewByIdOrNull<View>(R.id.btnMyMembership)?.setOnClickListener {
                navigateToMembership()
            }

            // 设置各个功能项的点击事件
            functionItems.forEach { (viewId, functionItem) ->
                view.findViewByIdOrNull<View>(viewId)?.setOnClickListener {
                    onFunctionItemClick(functionItem)
                }
            }
        }
    }

    /**
     * 导航到通知页面 - 使用基类路由方法
     */
    private fun navigateToNotification() {
        showToast("跳转到通知页面")
        // 使用基类的 navigateTo 方法
        navigateTo(com.example.news.activity.profile.NotificationActivity::class.java)
    }

    /**
     * 导航到会员页面 - 使用基类路由方法
     */
    private fun navigateToMembership() {
//        showToast("跳转到会员页面")

        val tvNickname = view?.findViewByIdOrNull<TextView>(R.id.tvNickname)

        val bundle = Bundle().apply {
            putString("user_id", "12345")
            putString("user_name", tvNickname?.text.toString())
        }
        // 使用基类的 navigateToWithExtras 方法
        navigateToWithExtras(
            com.example.news.activity.profile.MembershipActivity::class.java, bundle
        )
    }

    /**
     * 功能项点击处理
     */
    private fun onFunctionItemClick(functionItem: FunctionItem) {
        safeRun {
            when (functionItem.title) {
                "目标" -> showToast("进入目标设置")
                "体围" -> showToast("查看体围数据")
                "运动能力" -> showToast("运动能力评估")
                "月度报告" -> showToast("查看月度报告")
                "亲密好友" -> showToast("好友列表")
                "卡券" -> showToast("我的卡券")
                "燃烧营" -> showToast("进入燃烧营")
                "食谱" -> showToast("查看食谱")
                "饮食" -> showToast("饮食记录")
                "健康测评" -> showToast("健康测评")
                "商城" -> showToast("进入商城")
                "设备管理" -> navigateToDeviceManage()
                "设备消息" -> showToast("设备消息")
                "关于有品" -> navigateToAbout()
                "帮助与反馈" -> showToast("帮助与反馈")
                "有品实验室" -> showToast("有品实验室")
            }
        }
    }

    /**
     * 导航到设备管理页面 - 使用基类路由方法
     */
    private fun navigateToDeviceManage() {
        // 使用基类的 navigateTo 方法
        navigateTo(com.example.news.activity.profile.DeviceManageActivity::class.java)
    }

    /**
     * 导航到关于页面 - 使用基类路由方法
     */
    private fun navigateToAbout() {
        // 使用基类的 navigateTo 方法
        navigateTo(com.example.news.activity.profile.AboutActivity::class.java)
    }

    /**
     * 更新用户信息（可用于数据观察回调）
     */
    private fun updateUserInfo(nickname: String, membershipExpire: String) {
        safeRun {
            view?.findViewByIdOrNull<TextView>(R.id.tvNickname)?.text = nickname
            view?.findViewByIdOrNull<TextView>(R.id.tvMembership)?.text =
                getString(R.string.membership_expire_time, membershipExpire)
        }
    }

    /**
     * 动态更新功能项（可选）
     */
    fun updateFunctionItem(itemId: Int, newFunctionItem: FunctionItem) {
        safeRun {
            val itemView = view?.findViewByIdOrNull<View>(itemId) ?: return@safeRun
            val ivIcon = itemView.findViewByIdOrNull<ImageView>(R.id.ivIcon)
            val tvTitle = itemView.findViewByIdOrNull<TextView>(R.id.tvTitle)

            ivIcon?.setImageResource(newFunctionItem.iconRes)
            tvTitle?.text = newFunctionItem.title
        }
    }

    companion object {
        /**
         * 创建新的 ProfileFragment 实例
         */
        fun newInstance(): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    // 可以在这里传递参数
                }
            }
        }
    }
}