package com.nehak.instagramfeed.feedUI.holders

import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.nehak.instagramfeed.databinding.FeedItemLayoutBinding

/**
 * Create By Neha Kushwah
 */
class FeedViewHolder(root: View) :  RecyclerView.ViewHolder(root) {
    lateinit var recyclerViewHorizontal: RecyclerView
    lateinit var binding: FeedItemLayoutBinding

    constructor(binding: FeedItemLayoutBinding) : this(binding.root) {
        this.binding = binding
        recyclerViewHorizontal =
            binding.recyclerViewHorizontal

        /** Keep the item center aligned**/
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewHorizontal)
    }
}