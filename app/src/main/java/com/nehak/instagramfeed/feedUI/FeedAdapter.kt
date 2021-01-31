package com.nehak.instagramfeed.feedUI

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nehak.instagramfeed.other.Constants
import com.nehak.instagramfeed.dataModels.FeedItem
import com.nehak.instagramfeed.databinding.ItemFeedBinding


/**
 * Create By Neha Kushwah
 */
class FeedAdapter(val context: Context) : ListAdapter<FeedItem, FeedViewHolder>(DIFF_CALLBACK) {

    companion object {
        /** Mandatory implementation inorder to use "ListAdapter" - new JetPack component" **/
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeedItem>() {
            override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                return false;// oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                return false;//oldItem == newItem
            }

        }
        val TOTAL_ITEMS = 200;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        /*Reset ViewHolder */
//        removeImageFromImageView(holder.videoThumbnail)
        holder.customPlayerView.reset()

        /*Set seperate ID for each player view, to prevent it being overlapped by other player's changes*/
        holder.customPlayerView.id = View.generateViewId()

        /*circlular repeatation of items*/
        val videoPos = (position % Constants.videoList.size);

        /*Set ratio according to video*/
        (holder.videoThumbnail.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = Constants.videoList.get(videoPos).dimension

        /*Set video's direct url*/
        holder.customPlayerView.setVideoUri(Uri.parse(Constants.videoList.get(videoPos).downloadUrl))

        /*Set video's thumbnail locally (by drawable), you can set it by remoteUrl too*/
        val resID: Int = context.getResources().getIdentifier(
            "thumbnail_" + videoPos,
            "drawable",
            context.getPackageName()
        )

        val res: Drawable = context.getResources().getDrawable(resID, null)
        holder.videoThumbnail.setImageDrawable(res);


    }

    override fun getItemCount(): Int {
        return TOTAL_ITEMS;
    }

    fun removeImageFromImageView(imageView: ImageView) {
        try {
            imageView.background = null
            imageView.setImageDrawable(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
