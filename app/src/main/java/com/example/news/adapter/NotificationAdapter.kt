package com.example.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.model.NotificationItem

class NotificationAdapter : ListAdapter<NotificationItem, NotificationAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvNotificationTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvNotificationContent)
        private val tvTime: TextView = itemView.findViewById(R.id.tvNotificationTime)
        private val viewUnread: View = itemView.findViewById(R.id.viewUnreadIndicator)
        
        fun bind(item: NotificationItem) {
            tvTitle.text = item.title
            tvContent.text = item.content
            tvTime.text = item.time
            viewUnread.visibility = if (item.isRead) View.GONE else View.VISIBLE
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<NotificationItem>() {
        override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem.time == newItem.time
        }
        
        override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem == newItem
        }
    }
}