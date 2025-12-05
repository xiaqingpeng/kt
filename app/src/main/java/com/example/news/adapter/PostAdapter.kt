package com.example.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapter.base.BaseAdapter
import com.example.news.model.NoticeItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class PostAdapter : BaseAdapter<NoticeItemEntity, PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvBody: TextView = view.findViewById(R.id.tvBody)

        private val updateBy: TextView = view.findViewById(R.id.updateBy)
        private val updateTime: TextView = view.findViewById(R.id.updateTime)

        fun bind(item: NoticeItemEntity) {
            tvTitle.text = item.noticeTitle
            tvBody.text = item.noticeContent

            updateBy.text = item.updateBy
            updateTime.text = formatDisplayTime(item.updateTime)
        }
    }

    private fun formatDisplayTime(raw: String?): String {
        if (raw.isNullOrBlank()) return ""

        // Target display format (local time)
        val outFmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        outFmt.timeZone = TimeZone.getDefault()

        val candidates = listOf(
            // ISO8601 with millis and Z
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            },
            // ISO8601 without millis
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            },
            // Plain datetime (assume local)
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            },
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            }
        )

        for (fmt in candidates) {
            try {
                val date: Date = fmt.parse(raw) ?: continue
                return outFmt.format(date)
            } catch (_: Exception) {
            }
        }

        // Fallback: return original
        return raw
    }
}
