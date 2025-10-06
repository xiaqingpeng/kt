package com.example.news.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.fragment.base.BaseFragment
import com.example.news.model.FunctionItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import androidx.core.content.edit
import androidx.core.graphics.scale

class ProfileFragment : BaseFragment() {

    private var tvNotificationBadge: TextView? = null
    private var currentBadgeCount = 0

    // BottomSheet 相关
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var takePhotoFile: File? = null

    // Activity Result Launchers
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            takePhotoFile?.let { file ->
                // 处理拍照后的图片
                handleSelectedImage(Uri.fromFile(file))

            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // 处理从相册选择的图片
            handleSelectedImage(it)
            showToast("头像更新成功")
        }
    }

    private var currentAction = 0

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
        setupNotificationBadge(view)
        setupBottomSheet() // 初始化 BottomSheet
        // 加载保存的头像
        loadSavedAvatar()
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
    }

    /**
     * 初始化 BottomSheet
     */
    private fun setupBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_selector, null)
        bottomSheetDialog.setContentView(view)

        // 设置选项点击监听
        view.findViewById<View>(R.id.takePhoto).setOnClickListener {
            currentAction = ACTION_TAKE_PHOTO
            checkAndRequestCameraPermission()
            bottomSheetDialog.dismiss()
        }

        view.findViewById<View>(R.id.chooseFromGallery).setOnClickListener {
            currentAction = ACTION_CHOOSE_GALLERY
            checkAndRequestStoragePermission()
            bottomSheetDialog.dismiss()
        }

        view.findViewById<View>(R.id.cancel).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    /**
     * 显示头像选择 BottomSheet
     */
    private fun showAvatarBottomSheet() {
        safeRun {
            if (!::bottomSheetDialog.isInitialized) {
                setupBottomSheet()
            }
            bottomSheetDialog.show()
        }
    }

    /**
     * 检查并请求相机权限 - 使用基类方法
     */
    private fun checkAndRequestCameraPermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        checkMultiplePermissions(permissions) { allGranted ->
            if (allGranted) {
                takePhoto()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    /**
     * 检查并请求存储权限 - 使用基类方法
     */
    private fun checkAndRequestStoragePermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        checkMultiplePermissions(permissions) { allGranted ->
            if (allGranted) {
                chooseFromGallery()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    /**
     * 显示权限被拒绝的对话框
     */
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("需要权限")
            .setMessage("此功能需要相关权限才能正常工作。您可以在设置中手动授予权限。")
            .setPositiveButton("去设置") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /**
     * 打开应用设置页面
     */
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    /**
     * 拍照
     */
    private fun takePhoto() {
        safeRun {
            takePhotoFile = createImageFile()
            takePhotoFile?.let { file ->
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    file
                )
                takePictureLauncher.launch(photoURI)
            } ?: showToast("创建图片文件失败")
        }
    }

    /**
     * 从相册选择
     */
    private fun chooseFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    /**
     * 创建临时图片文件
     */
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = requireContext().getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
            File.createTempFile(
                "AVATAR_${timeStamp}_",
                ".jpg",
                storageDir
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 处理选中的图片
     */
    /**
     * 处理选中的图片
     */
    private fun handleSelectedImage(uri: Uri) {
        safeRun {
            val imageView = view?.findViewByIdOrNull<ImageView>(R.id.ivAvatar)
            imageView?.let { iv ->
                try {
                    // 使用 Glide 加载图片（更好的内存管理和缓存）
                    Glide.with(this)
                        .load(uri)
                        .circleCrop() // 圆形裁剪
                        .placeholder(R.drawable.ic_default_avatar) // 占位图
                        .error(R.drawable.ic_default_avatar) // 错误图
                        .into(iv)

                    showToast("头像更新成功")
                    saveAvatarUri(uri.toString())

                } catch (e: Exception) {
                    println("头像设置失败: ${e.message}")
                    showToast("头像设置失败，请重试")
                }
            }
        }
    }

    /**
     * 保存头像URI（可选）
     */
    private fun saveAvatarUri(uriString: String) {
        try {
            val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPref.edit { putString("avatar_uri", uriString) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载保存的头像（可选）
     */
    private fun loadSavedAvatar() {
        safeRun {
            val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val savedUri = sharedPref.getString("avatar_uri", null)
            savedUri?.let { uriString ->
                try {
                    val uri = Uri.parse(uriString)
                    view?.findViewByIdOrNull<ImageView>(R.id.ivAvatar)?.let { imageView ->
                        Glide.with(this)
                            .load(uri)
                            .circleCrop()
                            .placeholder(R.drawable.ic_default_avatar)
                            .error(R.drawable.ic_default_avatar)
                            .into(imageView)
                    }
                } catch (e: Exception) {
                    println("加载保存的头像失败: ${e.message}")
                    // 可以选择清除无效的Uri
                    // sharedPref.edit().remove("avatar_uri").apply()
                }
            }
        }
    }

    /**
     * 压缩图片并保存
     */
    private fun compressAndSaveImage(originalUri: Uri): Uri? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(originalUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // 压缩图片
            val compressedBitmap = compressBitmap(originalBitmap, 800, 800) // 最大宽高800px

            // 保存压缩后的图片
            saveBitmapToFile(compressedBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            originalUri // 压缩失败返回原URI
        }
    }

    /**
     * 压缩Bitmap
     */
    private fun compressBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        if (width > maxWidth || height > maxHeight) {
            val ratio = min(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
            width = (width * ratio).toInt()
            height = (height * ratio).toInt()
        }

        return bitmap.scale(width, height)
    }

    /**
     * 保存Bitmap到文件
     */
    private fun saveBitmapToFile(bitmap: Bitmap): Uri? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "COMPRESSED_AVATAR_${timeStamp}_",
                ".jpg",
                storageDir
            )

            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // 80%质量
            outputStream.close()

            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 设置通知徽章
     */
    private fun setupNotificationBadge(view: View) {
        safeRun {
            tvNotificationBadge = view.findViewByIdOrNull(R.id.tvNotificationBadge)
            updateNotificationBadge(3) // 示例：显示3条未读消息
        }
    }

    /**
     * 更新通知徽章
     */
    fun updateNotificationBadge(count: Int) {
        safeRun {
            currentBadgeCount = count
            when {
                count <= 0 -> {
                    tvNotificationBadge?.visibility = View.GONE
                }
                count > 99 -> {
                    tvNotificationBadge?.visibility = View.VISIBLE
                    tvNotificationBadge?.text = "99+"
                }
                else -> {
                    tvNotificationBadge?.visibility = View.VISIBLE
                    tvNotificationBadge?.text = count.toString()
                }
            }
        }
    }

    /**
     * 增加通知徽章计数
     */
    fun incrementNotificationBadge() {
        updateNotificationBadge(currentBadgeCount + 1)
    }

    /**
     * 减少通知徽章计数
     */
    fun decrementNotificationBadge() {
        val newCount = max(0, currentBadgeCount - 1)
        updateNotificationBadge(newCount)
    }

    /**
     * 清除通知徽章
     */
    fun clearNotificationBadge() {
        updateNotificationBadge(0)
    }

    /**
     * 获取当前徽章计数
     */
    fun getCurrentBadgeCount(): Int {
        return currentBadgeCount
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

            // 头像点击 - 显示 BottomSheet
            view.findViewByIdOrNull<View>(R.id.ivAvatar)?.setOnClickListener {
                showAvatarBottomSheet()
            }

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
     * 导航到通知页面
     */
    private fun navigateToNotification() {
        showToast("跳转到通知页面")
        navigateTo(com.example.news.activity.profile.NotificationActivity::class.java)
    }

    /**
     * 导航到会员页面
     */
    private fun navigateToMembership() {
        val tvNickname = view?.findViewByIdOrNull<TextView>(R.id.tvNickname)
        val bundle = Bundle().apply {
            putString("user_id", "12345")
            putString("user_name", tvNickname?.text.toString())
        }
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
     * 导航到设备管理页面
     */
    private fun navigateToDeviceManage() {
        navigateTo(com.example.news.activity.profile.DeviceManageActivity::class.java)
    }

    /**
     * 导航到关于页面
     */
    private fun navigateToAbout() {
        navigateTo(com.example.news.activity.profile.AboutActivity::class.java)
    }

    /**
     * 更新用户信息
     */
    private fun updateUserInfo(nickname: String, membershipExpire: String) {
        safeRun {
            view?.findViewByIdOrNull<TextView>(R.id.tvNickname)?.text = nickname
            view?.findViewByIdOrNull<TextView>(R.id.tvMembership)?.text =
                getString(R.string.membership_expire_time, membershipExpire)
        }
    }

    /**
     * 动态更新功能项
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
        private const val ACTION_TAKE_PHOTO = 1
        private const val ACTION_CHOOSE_GALLERY = 2

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