package com.nehak.instagramfeed.feedUI.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nehak.instagramfeed.dataModels.FeedItem
import com.nehak.instagramfeed.databinding.HorizontalItemFeedBinding
import com.nehak.instagramfeed.databinding.ItemFeedBinding
import com.nehak.instagramfeed.feedUI.holders.FeedViewHolder
import com.nehak.instagramfeed.feedUI.holders.ImageFeedViewHolder
import com.nehak.instagramfeed.feedUI.holders.VideoFeedViewHolder
import com.nehak.instagramfeed.other.Constants


/**
 * Create By Neha Kushwah
 */
class FeedAdapter(private val context: Context) :
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
        const val TOTAL_ITEMS = 200
        const val FEED_TYPE_VIDEO = 1
        const val FEED_TYPE_IMAGES_MULTIPLE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            FEED_TYPE_VIDEO
        } else {
            FEED_TYPE_IMAGES_MULTIPLE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {

        if (viewType == FEED_TYPE_IMAGES_MULTIPLE) {
            return ImageFeedViewHolder(
                HorizontalItemFeedBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
        return VideoFeedViewHolder(
            ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        if (holder is VideoFeedViewHolder) {
            handleViewHolder(holder, position)
        } else if (holder is ImageFeedViewHolder) {
            handleViewHolder(holder, position)
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables", "DiscouragedApi")
    private fun handleViewHolder(holder: VideoFeedViewHolder, position: Int) {
        /*Reset ViewHolder */
        holder.customPlayerView.reset()

        /*Set separate ID for each player view, to prevent it being overlapped by other player's changes*/
        holder.customPlayerView.id = View.generateViewId()

        /*circular reputation of items*/
        val videoPos = (position % Constants.videoList.size)

        /*Set ratio according to video*/
        (holder.videoThumbnail.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
            Constants.videoList[videoPos].dimension

        /*Set video's direct url*/
        holder.customPlayerView.setVideoUri(Uri.parse(Constants.videoList[videoPos].downloadUrl))

        /*Set video's thumbnail locally (by drawable), you can set it by remoteUrl too*/
        val resID: Int = context.resources.getIdentifier(
            "thumbnail_$videoPos",
            "drawable",
            context.packageName
        )

        val res: Drawable = context.resources.getDrawable(resID, null)
        holder.videoThumbnail.setImageDrawable(res)
    }

    private fun handleViewHolder(holder: ImageFeedViewHolder, position: Int) {

        /* Set adapter (items are being used inside adapter, you can setup in your own way*/
        val feedAdapter = ImageAdapter(position)
        holder.recyclerViewImages.adapter = feedAdapter

    }

    override fun getItemCount(): Int {
        return TOTAL_ITEMS
    }

}
