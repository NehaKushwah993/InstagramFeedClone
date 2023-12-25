package com.nehak.instagramfeed.feedUI.horizontal_pager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nehak.instagramfeed.databinding.ImageItemSingleBinding
import com.nehak.instagramfeed.databinding.VideoItemSingleBinding

/**
 * Create By Neha Kushwah
 */
abstract class PagerViewHolder(root: View) : RecyclerView.ViewHolder(root)

class ImageViewHolder(binding: ImageItemSingleBinding) : PagerViewHolder(binding.root) {
    val imageView =
        binding.imageView
}

class VideoViewHolder(binding: VideoItemSingleBinding) : PagerViewHolder(binding.root) {
    val videoThumbnail = binding.feedThumbnailView
    val customPlayerView = binding.feedPlayerView
    val muteIcon = binding.muteIcon
}