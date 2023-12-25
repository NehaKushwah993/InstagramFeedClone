package com.nehak.instagramfeed.feedUI

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nehak.instagramfeed.dataModels.FeedItem
import com.nehak.instagramfeed.databinding.FeedItemLayoutBinding
import com.nehak.instagramfeed.feedUI.holders.FeedViewHolder
import com.nehak.instagramfeed.feedUI.horizontal_pager.HorizontalPagerAdapter
import com.nehak.instagramfeed.other.Constants.Companion.dataList


/**
 * Create By Neha Kushwah
 */
class FeedAdapter(
    private val context: Context,
    private val scrollListener: RecyclerView.OnScrollListener,
    private val onMuteClickListener: AdapterView.OnItemClickListener,
) :
    ListAdapter<FeedItem, FeedViewHolder>(DIFF_CALLBACK) {

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
        const val FEED_TYPE_IMAGE = 3
        const val FEED_TYPE_VIDEO = 4
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            FeedItemLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        /*Set ratio according to first video*/
        (holder.recyclerViewHorizontal.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
            dataList[position][0].ratio
        /* Set adapter (items are being used inside adapter, you can setup in your own way*/
        val feedAdapter = HorizontalPagerAdapter(context, position, dataList[position], onMuteClickListener)
        holder.recyclerViewHorizontal.adapter = feedAdapter
        holder.recyclerViewHorizontal.clearOnScrollListeners()

        holder.recyclerViewHorizontal.addOnScrollListener(scrollListener)
        holder.recyclerViewHorizontal.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemPosition: Int =
                    (holder.recyclerViewHorizontal.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                try {
                    holder.binding.dots.getTabAt(itemPosition)?.select()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        /**
         * Add dots (fixed size for now)
         */
        holder.binding.dots.removeAllTabs()
        if (dataList[position].size > 1) {
            for (i in 0 until dataList[position].size) {
                holder.binding.dots.addTab(holder.binding.dots.newTab())
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
