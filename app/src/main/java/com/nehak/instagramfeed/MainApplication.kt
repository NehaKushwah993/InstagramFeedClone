package com.nehak.instagramfeed

import android.app.Application
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache

class MainApplication : Application() {
    companion object {
        lateinit var cache: SimpleCache
    }

    private lateinit var cacheEvictor: LeastRecentlyUsedCacheEvictor
    private lateinit var exoplayerDatabaseProvider: StandaloneDatabaseProvider
    private val cacheSize: Long = 900 * 1024 * 1024

    override fun onCreate() {
        super.onCreate()
        setUpForPreCaching()
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    fun setUpForPreCaching() {
        cacheEvictor = LeastRecentlyUsedCacheEvictor(cacheSize)
        exoplayerDatabaseProvider = StandaloneDatabaseProvider(this)
        cache =
            SimpleCache(cacheDir, cacheEvictor, exoplayerDatabaseProvider)

    }

}
