package com.nehak.instagramfeed.feedUI

import androidx.recyclerview.widget.RecyclerView
import com.nehak.instagramfeed.databinding.ItemFeedBinding

/**
 * Create By Neha Kushwah
 */
class VideoFeedViewHolder(binding: ItemFeedBinding) : FeedViewHolder(binding.root) {
    val videoThumbnail =
        binding.feedThumbnailView;
    val customPlayerView =
        binding.feedPlayerView;

}