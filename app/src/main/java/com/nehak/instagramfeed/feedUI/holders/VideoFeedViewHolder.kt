package com.nehak.instagramfeed.feedUI.holders

import com.nehak.instagramfeed.databinding.ItemFeedBinding
import com.nehak.instagramfeed.feedUI.holders.FeedViewHolder

/**
 * Create By Neha Kushwah
 */
class VideoFeedViewHolder(binding: ItemFeedBinding) : FeedViewHolder(binding.root) {
    val videoThumbnail =
        binding.feedThumbnailView;
    val customPlayerView =
        binding.feedPlayerView;

}