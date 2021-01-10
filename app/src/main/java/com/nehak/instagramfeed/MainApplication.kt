package com.nehak.instagramfeed

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.nehak.instagramfeed.other.Constants.Companion.simpleCache
import java.io.File

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpForPreCaching()
    }


    fun setUpForPreCaching() {

        val exoPlayerCacheSize = 50 * 1024 * 1024.toLong()// Set the size of cache for video
        var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor? = null
        var exoDatabaseProvider: ExoDatabaseProvider? = null

        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = ExoDatabaseProvider(this)
        }

        if (simpleCache == null) {
            val cache: File = File(getCacheDir(), "Video_Cache")
            if (!cache.exists()) {
                cache.mkdirs()
            }
            simpleCache =
                SimpleCache(cache, leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
        }

    }

}
