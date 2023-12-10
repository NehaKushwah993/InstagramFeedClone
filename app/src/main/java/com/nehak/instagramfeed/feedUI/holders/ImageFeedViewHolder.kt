package com.nehak.instagramfeed.feedUI.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.nehak.instagramfeed.databinding.HorizontalItemFeedBinding
import com.nehak.instagramfeed.feedUI.adapters.ImageAdapter.Companion.TOTAL_IMAGES

/**
 * Create By Neha Kushwah
 */
class ImageFeedViewHolder(root: View) : FeedViewHolder(root) {
    lateinit var recyclerViewImages: RecyclerView

    constructor(binding: HorizontalItemFeedBinding) : this(binding.root) {
        recyclerViewImages =
            binding.recyclerViewImages

        /** Keep the item center aligned**/
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewImages)


        /**
         * Add dots (fixed size for now)
         */
        for (i in 0 until TOTAL_IMAGES) {
            binding.dots.addTab(binding.dots.newTab())
        }

        recyclerViewImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val itemPosition: Int =
                    (recyclerViewImages.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                try {
                    binding.dots.getTabAt(itemPosition)?.select()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

}