package com.nehak.instagramfeed.player

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.nehak.instagramfeed.R
import com.nehak.instagramfeed.other.Constants.Companion.simpleCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created By : Neha Kushwah
 * This player will do the magic...
 */
class InstaLikePlayerView : FrameLayout {
    private var lastPos: Long? = 0
    private lateinit var customPlayerContainer: FrameLayout;
    private var playerView: CustomExoPlayerView? = null;
    private var lastFrameOfVideo: ImageView? = null;
    private var videoUri: Uri? = null;

    var cacheDataSourceFactory = CacheDataSourceFactory(
        simpleCache,
        DefaultHttpDataSourceFactory(
            Util.getUserAgent(
                context, context.getString(
                    R.string.app_name
                )
            )
        ),
        CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
    )

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
        init()
    }

    fun init() {

        inflate(context, R.layout.player_view, this)

        playerView = findViewById(R.id.customExoPlayerView)
        customPlayerContainer = findViewById(R.id.customPlayerContainer)
        lastFrameOfVideo = findViewById(R.id.lastFrameOfVideo)
        lastFrameOfVideo?.visibility = View.INVISIBLE
        customPlayerContainer.alpha = 0f

        /*Setup player + Adding Cache Directory*/
        val simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE;
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY) {

                    simpleExoPlayer.seekTo(lastPos!!)
                    lastFrameOfVideo?.visibility = View.INVISIBLE
                    customPlayerContainer.alpha = 1f


                }
            }

        })

        playerView!!.useController = false
        simpleExoPlayer.playWhenReady = false
        playerView!!.player = simpleExoPlayer;

    }

    /**
     * This will resuse the player and will play new URI we have provided
     */
    fun startPlaying() {

        val mediaSource =
            ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(videoUri)
        (playerView!!.player as SimpleExoPlayer).prepare(mediaSource)

        playerView!!.player?.seekTo(lastPos!!)
        playerView!!.player?.playWhenReady = true


    }

    /**
     * This will stop the player, but stopping the player shows blackscreen
     * so to cover that we set alpha to 0 of player
     * and lastFrame of player using imageView over player to make it look like paused player
     *
     * (If we will not stop the player, only pause , then it can cause memory issue due to overload of player
     * and paused player can not be payed with new URL, after stopping the player we can reuse that with new URL
     *
     */
    fun removePlayer() {
        if (playerView != null) {
            val playerViewCopy = playerView;
            playerViewCopy!!.player?.setPlayWhenReady(false)
            lastPos = playerViewCopy?.player?.currentPosition
            usePixelCopy(playerViewCopy.getVideoSurfaceView() as SurfaceView) { bitmap: Bitmap? ->
                post {
                    // Run this on UI thread
                    if (bitmap != null) {
                        lastFrameOfVideo?.setImageBitmap(bitmap)
                        lastFrameOfVideo?.visibility = View.VISIBLE
                        customPlayerContainer?.alpha = 0f
                    }
                    playerViewCopy?.player?.stop(true)
                }
            }

        }


    }

    /**
     * Pixel copy to copy SurfaceView/VideoView into BitMap
     */
    fun usePixelCopy(videoView: SurfaceView, callback: (Bitmap?) -> Unit) {
        val width = videoView.width;
        val height = videoView.height;
        GlobalScope.launch(Dispatchers.IO) {
            val bitmap: Bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.RGB_565
            );
            try {
                // Create a handler thread to offload the processing of the image.
                val handlerThread = HandlerThread("PixelCopier");
                handlerThread.start();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    PixelCopy.request(
                        videoView, bitmap,
                        PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                            if (copyResult == PixelCopy.SUCCESS) {
                                callback(bitmap)
                            }
                            handlerThread.quitSafely();
                        },
                        Handler(handlerThread.looper)
                    )
                }
            } catch (e: IllegalArgumentException) {
                callback(null)
                // PixelCopy may throw IllegalArgumentException, make sure to handle it
                e.printStackTrace()
            }
        }.start()
    }

    fun reset() {
        lastFrameOfVideo?.visibility = View.INVISIBLE
        customPlayerContainer.alpha = 0f

    }

    fun setVideoUri(uri: Uri?) {
        this.videoUri = uri;
    }

}