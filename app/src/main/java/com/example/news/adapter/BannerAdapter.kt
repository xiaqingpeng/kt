package com.example.news.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.news.R
import com.example.news.model.BannerItem

/**
 * 轮播图适配器
 */
class BannerAdapter(
    private val banners: List<BannerItem>,
    private val onBannerClick: (BannerItem) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = banners[position]
        holder.bind(banner)
    }

    override fun getItemCount(): Int = banners.size

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBanner: ImageView = itemView.findViewById(R.id.ivBanner)
        private val tvBannerTitle: TextView = itemView.findViewById(R.id.tvBannerTitle)

        fun bind(banner: BannerItem) {
            tvBannerTitle.text = banner.title
            
            // 打印调试信息
            Log.d("BannerAdapter", "Loading image: ${banner.imageUrl}")
            
            // 使用Glide加载网络图片
            Glide.with(itemView.context)
                .load(banner.imageUrl)
                .placeholder(R.drawable.banner_placeholder) // 占位图
                .error(R.drawable.banner_placeholder) // 错误时显示的图片
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16))) // 圆角
                .into(ivBanner)
            
            itemView.setOnClickListener {
                onBannerClick(banner)
            }
        }
    }
}
