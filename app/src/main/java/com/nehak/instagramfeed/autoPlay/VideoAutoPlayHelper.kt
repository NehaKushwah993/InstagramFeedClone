package com.nehak.instagramfeed.autoPlay

import android.graphics.Rect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nehak.instagramfeed.feedUI.adapters.FeedAdapter
import com.nehak.instagramfeed.feedUI.holders.FeedViewHolder
import com.nehak.instagramfeed.feedUI.holders.VideoFeedViewHolder
import com.nehak.instagramfeed.player.InstaLikePlayerView

/**
 * Create By Neha Kushwah
 */
class VideoAutoPlayHelper(var recyclerView: RecyclerView) {

    private var lastPlayerView: InstaLikePlayerView? = null
    val MIN_LIMIT_VISIBILITY =
        20; // When playerView will be less than 20% visible than it will stop the player

    var currentPlayingVideoItemPos = -1; // -1 indicates nothing playing

    fun onScrolled(recyclerView: RecyclerView, feedAdapter: FeedAdapter) {

        val firstVisiblePosition: Int = findFirstVisibleItemPosition()
        val lastVisiblePosition: Int = findLastVisibleItemPosition()

        val pos = getMostVisibleItem(firstVisiblePosition, lastVisiblePosition)

        if (pos == -1) {
            /*check if current view is more than MIN_LIMIT_VISIBILITY*/
            if (currentPlayingVideoItemPos != -1) {
                val viewHolder: RecyclerView.ViewHolder =
                    recyclerView?.findViewHolderForAdapterPosition(currentPlayingVideoItemPos)!!

                val currentVisibility = getVisiblePercentage(viewHolder);
                if (currentVisibility < MIN_LIMIT_VISIBILITY) {
                    lastPlayerView?.removePlayer()
                }
                currentPlayingVideoItemPos = -1;
            }


        } else {

            if (currentPlayingVideoItemPos != pos) {
                currentPlayingVideoItemPos = pos;
                attachVideoPlayerAt(pos);
            }

        }

    }

    private fun attachVideoPlayerAt(pos: Int) {
        val feedViewHolder: FeedViewHolder =
            (recyclerView.findViewHolderForAdapterPosition(pos) as FeedViewHolder?)!!

        if(feedViewHolder is VideoFeedViewHolder) {
            /** in case its a video**/
            if (lastPlayerView==null || lastPlayerView != feedViewHolder.customPlayerView) {
                feedViewHolder.customPlayerView.startPlaying()
                // stop last player
                lastPlayerView?.removePlayer();
            }
            lastPlayerView = feedViewHolder.customPlayerView;

        } else {
            /** in case its a image**/
            if (lastPlayerView != null) {
                // stop last player
                lastPlayerView?.removePlayer();
                lastPlayerView = null
            }

        }
    }

    private fun getMostVisibleItem(firstVisiblePosition: Int, lastVisiblePosition: Int): Int {

        var maxPercentage = -1;
        var pos = 0;
        for (i in firstVisiblePosition..lastVisiblePosition) {
            val viewHolder: RecyclerView.ViewHolder =
                recyclerView.findViewHolderForAdapterPosition(i)!!

            var currentPercentage = getVisiblePercentage(viewHolder);
            if (currentPercentage > maxPercentage) {
                maxPercentage = currentPercentage.toInt();
                pos = i;
            }

        }

        if (maxPercentage == -1 || maxPercentage < MIN_LIMIT_VISIBILITY) {
            return -1;
        }

        return pos;
    }

    private fun getVisiblePercentage(
        holder: RecyclerView.ViewHolder
    ): Float {
        val rect_parent = Rect()
        recyclerView.getGlobalVisibleRect(rect_parent)
        val location = IntArray(2)
        holder.itemView.getLocationOnScreen(location)

        val rect_child = Rect(
            location[0],
            location[1],
            location[0] + holder.itemView.getWidth(),
            location[1] + holder.itemView.getHeight()
        )

        val rect_parent_area =
            ((rect_child.right - rect_child.left) * (rect_child.bottom - rect_child.top)).toFloat()
        val x_overlap = Math.max(
            0,
            Math.min(rect_child.right, rect_parent.right) - Math.max(
                rect_child.left,
                rect_parent.left
            )
        ).toFloat()
        val y_overlap = Math.max(
            0,
            Math.min(rect_child.bottom, rect_parent.bottom) - Math.max(
                rect_child.top,
                rect_parent.top
            )
        ).toFloat()
        val overlapArea = x_overlap * y_overlap
        val percent = overlapArea / rect_parent_area * 100.0f

        return percent
    }


    private fun findFirstVisibleItemPosition(): Int {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            return (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        }

        return -1
    }

    private fun findLastVisibleItemPosition(): Int {
        if (recyclerView.getLayoutManager() is LinearLayoutManager) {
            return (recyclerView.getLayoutManager() as LinearLayoutManager).findLastVisibleItemPosition()
        }
        return -1
    }

    fun startObserving() {

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.adapter is FeedAdapter) {
                    onScrolled(recyclerView, recyclerView.adapter as FeedAdapter)
                } else {
                    throw IllegalStateException("Adapter should be FeedAdapter or extend FeedAdapter")
                }
            }
        })
    }

}