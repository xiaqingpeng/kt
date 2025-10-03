package com.example.news.adapter.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 基础的 Adapter 类
 */
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected val items = mutableListOf<T>()

    /**
     * 创建 ViewHolder
     */
    override abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    /**
     * 绑定数据到 ViewHolder
     */
    override abstract fun onBindViewHolder(holder: VH, position: Int)

    /**
     * 更新所有数据
     */
    fun updateData(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * 提交列表数据（兼容 ListAdapter 的命名）
     */
    fun submitList(newItems: List<T>) {
        updateData(newItems)
    }

    /**
     * 添加单个项目
     */
    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    /**
     * 添加多个项目
     */
    fun addItems(newItems: List<T>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    /**
     * 删除项目
     */
    fun removeItem(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * 获取项目数量
     */
    override fun getItemCount(): Int = items.size

    /**
     * 获取指定位置的项目
     */
    fun getItem(position: Int): T {
        return items[position]
    }

    /**
     * 清空所有数据
     */
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}