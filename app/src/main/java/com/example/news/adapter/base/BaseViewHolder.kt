

package com.example.news.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 基础的 ViewHolder 类
 * T: 数据模型类型
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * 绑定数据到视图
     * @param item 数据项
     */
    abstract fun bindData(item: T)
}