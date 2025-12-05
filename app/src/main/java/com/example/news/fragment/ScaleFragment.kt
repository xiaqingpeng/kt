package com.example.news.fragment

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.news.R
import com.example.news.adapter.PostAdapter
import com.example.news.fragment.base.BaseFragment
import com.example.news.viewmodel.ScaleViewModel

class ScaleFragment : BaseFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var btnRetry: Button
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private val adapter = PostAdapter()
    private lateinit var viewModel: ScaleViewModel
    private var noticeTypeArg: String? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_scale
    }

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.rvList)
        progressBar = view.findViewById(R.id.progressBar)
        tvError = view.findViewById(R.id.tvError)
        btnRetry = view.findViewById(R.id.btnRetry)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[ScaleViewModel::class.java]
        // 应用参数中的公告类型
        viewModel.setNoticeType(noticeTypeArg)
        bindObservers()
        setupInteractions()
        viewModel.loadFirstPage()
    }

    override fun setListeners() {
        // 设置点击监听器等
        // 例如：button.setOnClickListener { }
    }

    override fun observeData() {
        // 观察数据变化（如果使用 ViewModel 等）
    }

    override fun handleArguments() {
        // 处理传递的参数
        val args = arguments
        noticeTypeArg = args?.getString("noticeType")
    }

    private fun bindObservers() {
        viewModel.posts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            tvError.visibility = View.GONE
            btnRetry.visibility = View.GONE
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.refreshing.observe(viewLifecycleOwner) { refreshing ->
            swipeRefresh.isRefreshing = refreshing
        }
        viewModel.error.observe(viewLifecycleOwner) { err ->
            if (err.isNullOrEmpty()) {
                tvError.visibility = View.GONE
                btnRetry.visibility = View.GONE
            } else {
                tvError.text = err
                tvError.visibility = View.VISIBLE
                btnRetry.visibility = View.VISIBLE
            }
        }
    }

    private fun setupInteractions() {
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        btnRetry.setOnClickListener {
            viewModel.loadFirstPage()
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val lm = rv.layoutManager as? LinearLayoutManager ?: return
                val total = lm.itemCount
                val lastVisible = lm.findLastVisibleItemPosition()
                if (total > 0 && lastVisible >= total - 3) {
                    viewModel.loadNextPage()
                }
            }
        })
    }
}
