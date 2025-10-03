package com.example.news.fragment.base

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected open fun initView(view: View) {}
    protected open fun setListeners() {}
    protected open fun observeData() {}

    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}