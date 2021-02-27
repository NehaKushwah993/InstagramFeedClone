package com.nehak.instagramfeed.feedUI.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nehak.instagramfeed.other.Extensions.Companion.getResource
import com.nehak.instagramfeed.dataModels.FeedItem
import com.nehak.instagramfeed.databinding.ImageItemSingleBinding
import com.nehak.instagramfeed.feedUI.holders.ImageViewHolder
import kotlin.random.Random


/**
 * Create By Neha Kushwah
 */
class ImageAdapter(val context: Context, val parentPosition: Int) :
    ListAdapter<FeedItem, ImageViewHolder>(DIFF_CALLBACK) {

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

        public val TOTAL_IMAGES = 4;
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        return ImageViewHolder(
            ImageItemSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        /* Show any random image from drawable*/
        val posToPick = (parentPosition+position)%7;
        holder.imageView.setImageDrawable(holder.itemView.context.getResource("image_"+posToPick));

    }

    override fun getItemCount(): Int {
        return TOTAL_IMAGES;
    }

    fun removeImageFromImageView(imageView: ImageView) {
        try {
            imageView.background = null
            imageView.setImageDrawable(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        val rand = Random(System.nanoTime())
        return (start..end).random(rand)
    }
}
