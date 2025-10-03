package com.example.news.fragment

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.news.R

class ProfileFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initView(view)
        setupFunctionItems(view)
        setClickListeners(view)
        return view
    }

    private fun initView(view: View) {
        // 初始化用户信息
        val tvNickname = view.findViewById<TextView>(R.id.tvNickname)
        val tvMembership = view.findViewById<TextView>(R.id.tvMembership)

        // 这里可以从用户数据中设置真实信息
        tvNickname.text = "健身达人"
        tvMembership.text = getString(R.string.membership_expire_time, "2024-12-31")
    }

    private fun setupFunctionItems(view: View) {
        functionItems.forEach { (viewId, functionItem) ->
            val itemView = view.findViewById<View>(viewId)
            val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

            ivIcon.setImageResource(functionItem.iconRes)
            tvTitle.text = functionItem.title
        }
    }

    private fun setClickListeners(view: View) {
        // 通知按钮
        view.findViewById<View>(R.id.btnNotification).setOnClickListener {
            // 跳转到通知页面
            showMessage("跳转到通知页面")
            val intent = Intent(requireContext(), com.example.news.activity.NotificationActivity::class.java)
            startActivity(intent)
        }

        // 我的会员按钮
        view.findViewById<View>(R.id.btnMyMembership).setOnClickListener {
            // 跳转到会员页面
            showMessage("跳转到会员页面")
            val intent = Intent(requireContext(), com.example.news.activity.MembershipActivity::class.java)
            startActivity(intent)
        }

        // 设置各个功能项的点击事件
        functionItems.forEach { (viewId, functionItem) ->
            view.findViewById<View>(viewId).setOnClickListener {
                onFunctionItemClick(functionItem)
            }
        }
    }

    private fun onFunctionItemClick(functionItem: FunctionItem) {
        when (functionItem.title) {
            "目标" -> showMessage("进入目标设置")
            "体围" -> showMessage("查看体围数据")
            "运动能力" -> showMessage("运动能力评估")
            "月度报告" -> showMessage("查看月度报告")
            "亲密好友" -> showMessage("好友列表")
            "卡券" -> showMessage("我的卡券")
            "燃烧营" -> showMessage("进入燃烧营")
            "食谱" -> showMessage("查看食谱")
            "饮食" -> showMessage("饮食记录")
            "健康测评" -> showMessage("健康测评")
            "商城" -> showMessage("进入商城")
            "设备管理" -> {
                // 跳转到设备管理页面
                val intent = Intent(requireContext(), com.example.news.activity.DeviceManageActivity::class.java)
                startActivity(intent)
            }
            "设备消息" -> showMessage("设备消息")
            "关于有品" -> {
                val intent = Intent(requireContext(), com.example.news.activity.AboutActivity::class.java)
                startActivity(intent)
            }
            "帮助与反馈" -> showMessage("帮助与反馈")
            "有品实验室" -> showMessage("有品实验室")
        }
    }

    private fun showMessage(message: String) {
        // 这里可以使用 Toast 或 Snackbar 显示消息
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        Log.d("ProfileFragment", message)
    }

    data class FunctionItem(val title: String, val iconRes: Int)
}