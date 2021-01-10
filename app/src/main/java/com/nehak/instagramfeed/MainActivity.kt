package com.nehak.instagramfeed

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nehak.instagramfeed.other.Constants
import com.nehak.instagramfeed.autoPlay.VideoPreLoadingService
import com.nehak.instagramfeed.dataModels.FeedItem
import com.nehak.instagramfeed.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        startPreCachingOfVideo()

    }

    /**
     * This method will start service to preCache videos from remoteUrl in to Cache Directory
     * So the Player will not reload videos from server if they are already loaded in cache
     */
    fun startPreCachingOfVideo() {

        val preloadingServiceIntent = Intent(this, VideoPreLoadingService::class.java)
        preloadingServiceIntent.putStringArrayListExtra(
            Constants.VIDEO_LIST,
            Constants.videoList.toStringArray()
        )
        startService(preloadingServiceIntent)

    }
}

private fun <E> ArrayList<E>.toStringArray(): ArrayList<String> {

    val arr = ArrayList<String>();
    forEach {
        arr.add((it as FeedItem).downloadUrl!!);
    }
    return arr;

}
