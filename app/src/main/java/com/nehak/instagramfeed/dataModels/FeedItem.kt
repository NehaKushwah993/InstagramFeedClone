package com.nehak.instagramfeed.dataModels

import com.google.gson.annotations.SerializedName

/**
 * Create By Neha Kushwah
 */
data class FeedItem (
    @SerializedName("link") val link : String,
    @SerializedName("type") val type : String,
    @SerializedName("thumbnail") val thumbnail : String,
    @SerializedName("ratio") val ratio : String
)
