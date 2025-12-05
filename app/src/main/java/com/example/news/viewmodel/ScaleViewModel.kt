package com.example.news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.model.NoticeItemEntity
import com.example.news.repository.NoticeRepository
import kotlinx.coroutines.launch

class ScaleViewModel : ViewModel() {

    private val repository = NoticeRepository()

    private val _posts = MutableLiveData<List<NoticeItemEntity>>()
    val posts: LiveData<List<NoticeItemEntity>> = _posts

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _refreshing = MutableLiveData<Boolean>(false)
    val refreshing: LiveData<Boolean> = _refreshing

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var page: Int = 1
    private val limit: Int = 20
    private var isLoadingMore = false
    private var noticeType: String? = "1"

    fun setNoticeType(type: String?) {
        noticeType = type
    }

    fun loadFirstPage() {
        page = 1
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = repository.fetchNoticeList(pageNum = page, pageSize = limit, noticeType = noticeType)
            _loading.value = false
            result.onSuccess { list ->
                _posts.value = list
            }.onFailure { e ->
                _error.value = e.message ?: "加载失败"
            }
        }
    }

    fun refresh() {
        _refreshing.value = true
        _error.value = null
        page = 1
        viewModelScope.launch {
            val result = repository.fetchNoticeList(pageNum = page, pageSize = limit, noticeType = noticeType)
            _refreshing.value = false
            result.onSuccess { list ->
                _posts.value = list
            }.onFailure { e ->
                _error.value = e.message ?: "刷新失败"
            }
        }
    }

    fun loadNextPage() {
        if (isLoadingMore || _loading.value == true || _refreshing.value == true) return
        isLoadingMore = true
        _error.value = null
        viewModelScope.launch {
            val nextPage = page + 1
            val result = repository.fetchNoticeList(pageNum = nextPage, pageSize = limit, noticeType = noticeType)
            isLoadingMore = false
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    page = nextPage
                    val current = _posts.value.orEmpty()
                    _posts.value = current + list
                }
            }.onFailure { e ->
                _error.value = e.message ?: "加载更多失败"
            }
        }
    }
}
