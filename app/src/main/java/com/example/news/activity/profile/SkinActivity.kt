package com.example.news.activity.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.activity.base.BaseTitleActivity
import com.example.news.databinding.ActivitySkinBinding


class SkinActivity : BaseTitleActivity<ActivitySkinBinding>() {

    override fun getViewBinding(): ActivitySkinBinding {
        return ActivitySkinBinding.inflate(layoutInflater)
    }

    override fun getToolbar() = binding.toolbar

    override fun getToolbarTitle(): String {
        return "皮肤主题"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    override fun initView() {
        super.initView() // 这会自动调用 BaseTitleActivity 的 setupToolbar()
        setupWindowInsets()
        setupViews()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    private fun setupViews() {
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