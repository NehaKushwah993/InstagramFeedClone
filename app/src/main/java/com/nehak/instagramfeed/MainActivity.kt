package com.nehak.instagramfeed

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nehak.instagramfeed.dataModels.FeedItem
import com.nehak.instagramfeed.databinding.ActivityMainBinding
import com.nehak.instagramfeed.other.Constants.Companion.dataList
import com.nehak.instagramfeed.other.readJSONFromAssets
import java.lang.reflect.Type

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonString = readJSONFromAssets(baseContext, "feed_data.json")
        val listType: Type = object : TypeToken<List<List<FeedItem>>>() {}.type

        dataList = (Gson().fromJson(jsonString, listType) as List<List<FeedItem>>)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
}
