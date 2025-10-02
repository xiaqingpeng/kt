package com.example.news

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.news.fragment.HomeFragment
import com.example.news.fragment.ProfileFragment
import com.example.news.fragment.ScaleFragment
import com.example.news.fragment.ShopFragment
import com.example.news.fragment.TrendsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            for (key in savedInstanceState.keySet()) {
                @Suppress("DEPRECATION")
                val value = savedInstanceState.get(key)
                Log.d("BundleDebug", "$key: $value")
            }
        } else {
            Log.d("BundleDebug", "savedInstanceState is null")
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Initialize ViewPager and BottomNavigationView
        setupViewPager()
        setupBottomNavigation()
    }
    
    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)
        
        // Disable swipe when dragging on the ViewPager
        viewPager.isUserInputEnabled = true
        
        // Handle page changes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Update the selected item in the bottom navigation
                when (position) {
                    0 -> bottomNavigation.selectedItemId = R.id.navigation_home
                    1 -> bottomNavigation.selectedItemId = R.id.navigation_trends
                    2 -> bottomNavigation.selectedItemId = R.id.navigation_scale
                    3 -> bottomNavigation.selectedItemId = R.id.navigation_shop
                    4 -> bottomNavigation.selectedItemId = R.id.navigation_profile
                }
            }
        })
    }
    
    private fun setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        
        // Handle navigation item clicks
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.navigation_trends -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.navigation_scale -> {
                    viewPager.currentItem = 2
                    true
                }
                R.id.navigation_shop -> {
                    viewPager.currentItem = 3
                    true
                }
                R.id.navigation_profile -> {
                    viewPager.currentItem = 4
                    true
                }
                else -> false
            }
        }
    }
    
    // ViewPager adapter
    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 5
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> TrendsFragment()
                2 -> ScaleFragment()
                3 -> ShopFragment()
                4 -> ProfileFragment()
                else -> HomeFragment()
            }
        }
    }
}