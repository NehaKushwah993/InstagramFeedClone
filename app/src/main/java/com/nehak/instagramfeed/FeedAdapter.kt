package com.nehak.instagramfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nehak.instagramfeed.databinding.ItemFeedBinding

class FeedAdapter : RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return super.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5;
    }


}