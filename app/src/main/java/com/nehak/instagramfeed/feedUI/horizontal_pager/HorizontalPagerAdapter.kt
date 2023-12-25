package com.nehak.instagramfeed.feedUI.horizontal_pager

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.nehak.instagramfeed.R
import com.nehak.instagramfeed.dataModels.FeedItem
import com.nehak.instagramfeed.databinding.ImageItemSingleBinding
import com.nehak.instagramfeed.databinding.VideoItemSingleBinding
import com.nehak.instagramfeed.feedUI.FeedAdapter.Companion.FEED_TYPE_IMAGE
import com.nehak.instagramfeed.feedUI.FeedAdapter.Companion.FEED_TYPE_VIDEO
import com.nehak.instagramfeed.player.InstaLikePlayerView.Companion.isMuted

/**
 * Create By Neha Kushwah
 */
class HorizontalPagerAdapter(
    private val context: Context,
    private val parentPosition: Int,
    private val arrayList: List<FeedItem>,
    private val onMuteClickListener: AdapterView.OnItemClickListener,
) :
    ListAdapter<FeedItem, PagerViewHolder>(DIFF_CALLBACK) {
    companion object {
        /** Mandatory implementation inorder to use "ListAdapter" - new JetPack component" **/
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeedItem>() {
            override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                return false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        if (viewType == FEED_TYPE_VIDEO) {
            return VideoViewHolder(
                VideoItemSingleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ImageViewHolder(
                ImageItemSingleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrayList[position].type == "image") {
            FEED_TYPE_IMAGE
        } else {
            FEED_TYPE_VIDEO
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "DiscouragedApi")
    override fun onBindViewHolder(pagerViewHolder: PagerViewHolder, position: Int) {
        if (pagerViewHolder is VideoViewHolder) {
            val holder: VideoViewHolder = pagerViewHolder
            /*Reset ViewHolder */
            holder.customPlayerView.reset()

            (holder.videoThumbnail.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                arrayList[0].ratio

            /*Set separate ID for each player view, to prevent it being overlapped by other player's changes*/
            holder.customPlayerView.id = View.generateViewId()

            /*Set video's direct url*/
            holder.customPlayerView.setVideoUri(Uri.parse(arrayList[position].link))

            /*Set video's thumbnail locally (by drawable), you can set it by remoteUrl too*/
            Glide.with(context).load(arrayList[position].thumbnail).centerCrop()
                .into(holder.videoThumbnail)

            holder.muteIcon.setOnClickListener {
                onMuteClickListener.onItemClick(null, pagerViewHolder.itemView, position, 0)
            }

            holder.muteIcon.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            isMuted.observeForever { value ->
                holder.muteIcon.isSelected = value
            }
        } else if (pagerViewHolder is ImageViewHolder) {
            val holder: ImageViewHolder = pagerViewHolder
            Glide.with(context).load(arrayList[position].thumbnail).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
