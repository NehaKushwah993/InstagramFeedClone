package com.nehak.instagramfeed.customViewBinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>?) {
    this?.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}
