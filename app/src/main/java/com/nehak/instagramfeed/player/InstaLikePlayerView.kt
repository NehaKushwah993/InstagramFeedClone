/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nehak.instagramfeed.player

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.ads.AdsLoader.AdViewProvider
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.TextOutput
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.spherical.SingleTapListener
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.nehak.instagramfeed.R
import com.nehak.instagramfeed.other.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InstaLikePlayerView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? =  /* attrs= */null,
    defStyleAttr: Int =  /* defStyleAttr= */0
) : FrameLayout(
    context!!, attrs, defStyleAttr
), AdViewProvider {
    private val componentListener: ComponentListener
    var videoSurfaceView: View?
    private var player: Player? = null
    private var textureViewRotation = 0
    private var isTouching = false

    /**
     * Returns the player currently set on this view, or null if no player is set.
     */
    fun getPlayer(): Player? {
        return player
    }

    /**
     * Set the [Player] to use.
     *
     *
     * To transition a [Player] from targeting one view to another, it's recommended to use
     * [.switchTargetView] rather than this method. If you do
     * wish to use this method directly, be sure to attach the player to the new view *before*
     * calling `setPlayer(null)` to detach it from the old one. This ordering is significantly
     * more efficient and may allow for more seamless transitions.
     *
     * @param player The [Player] to use, or `null` to detach the current player. Only
     * players which are accessed on the main thread are supported (`player.getApplicationLooper() == Looper.getMainLooper()`).
     */
    fun setPlayer(player: Player?) {
        Assertions.checkState(Looper.myLooper() == Looper.getMainLooper())
        Assertions.checkArgument(
            player == null || player.applicationLooper == Looper.getMainLooper()
        )
        if (this.player === player) {
            return
        }
        val oldPlayer = this.player
        if (oldPlayer != null) {
            oldPlayer.removeListener(componentListener)
            val oldVideoComponent = oldPlayer.videoComponent
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener)
                oldVideoComponent.clearVideoSurfaceView(videoSurfaceView as SurfaceView?)
            }
            val oldTextComponent = oldPlayer.textComponent
            oldTextComponent?.removeTextOutput(componentListener)
        }
        this.player = player
        if (player != null) {
            val newVideoComponent = player.videoComponent
            if (newVideoComponent != null) {
                newVideoComponent.setVideoSurfaceView(videoSurfaceView as SurfaceView?)
                newVideoComponent.addVideoListener(componentListener)
            }
            val newTextComponent = player.textComponent
            newTextComponent?.addTextOutput(componentListener)
            player.addListener(componentListener)
        } else {
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        // Work around https://github.com/google/ExoPlayer/issues/3160.
        videoSurfaceView?.setVisibility(visibility)

    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (player != null && player!!.isPlayingAd) {
            return super.dispatchKeyEvent(event)
        }
        val isDpadKey = isDpadKey(event.keyCode)
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (player == null) {
            false
        } else when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouching = true
                true
            }
            MotionEvent.ACTION_UP -> {
                if (isTouching) {
                    isTouching = false
                    performClick()
                    return true
                }
                false
            }
            else -> false
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return false
    }

    override fun onTrackballEvent(ev: MotionEvent): Boolean {
        return false
    }

    override fun getAdViewGroup(): ViewGroup? {
        return null
    }

    override fun getAdOverlayViews(): Array<View?> {
        return arrayOfNulls(0)
    }

    @SuppressLint("InlinedApi")
    private fun isDpadKey(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_UP_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_UP_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
    }

    private inner class ComponentListener : Player.EventListener, TextOutput, VideoListener,
        OnLayoutChangeListener, SingleTapListener, PlayerControlView.VisibilityListener {
        private val period: Timeline.Period
        private var lastPeriodUidWithTracks: Any? = null

        // TextOutput implementation
        override fun onCues(cues: List<Cue>) {}

        // VideoListener implementation
        override fun onVideoSizeChanged(
            width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float
        ) {
            var videoAspectRatio: Float =
                if (height == 0 || width == 0) 1F else width * pixelWidthHeightRatio / height

//      onContentAspectRatioChanged(videoAspectRatio, contentFrame, surfaceView);
        }

        override fun onRenderedFirstFrame() {}
        override fun onTracksChanged(tracks: TrackGroupArray, selections: TrackSelectionArray) {
            // Suppress the update if transitioning to an unprepared period within the same window. This
            // is necessary to avoid closing the shutter when such a transition occurs. See:
            // https://github.com/google/ExoPlayer/issues/5507.
            val player = Assertions.checkNotNull(player)
            val timeline = player.currentTimeline
            if (timeline.isEmpty) {
                lastPeriodUidWithTracks = null
            } else if (!player.currentTrackGroups.isEmpty) {
                lastPeriodUidWithTracks =
                    timeline.getPeriod(player.currentPeriodIndex, period,  /* setIds= */true).uid
            } else if (lastPeriodUidWithTracks != null) {
                val lastPeriodIndexWithTracks = timeline.getIndexOfPeriod(
                    lastPeriodUidWithTracks!!
                )
                if (lastPeriodIndexWithTracks != C.INDEX_UNSET) {
                    val lastWindowIndexWithTracks =
                        timeline.getPeriod(lastPeriodIndexWithTracks, period).windowIndex
                    if (player.currentWindowIndex == lastWindowIndexWithTracks) {
                        // We're in the same window. Suppress the update.
                        return
                    }
                }
                lastPeriodUidWithTracks = null
            }
        }

        // OnLayoutChangeListener implementation
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            applyTextureViewRotation(view as TextureView, textureViewRotation)
        }

        // SingleTapListener implementation
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return false
        }

        // PlayerControlView.VisibilityListener implementation
        override fun onVisibilityChange(visibility: Int) {}

        init {
            period = Timeline.Period()
        }
    }

    companion object {
        /**
         * Switches the view targeted by a given [Player].
         *
         * @param player        The player whose target view is being switched.
         * @param oldPlayerView The old view to detach from the player.
         * @param newPlayerView The new view to attach to the player.
         */
        fun switchTargetView(
            player: Player?,
            oldPlayerView: InstaLikePlayerView?,
            newPlayerView: InstaLikePlayerView?
        ) {
            if (oldPlayerView === newPlayerView) {
                return
            }
            // We attach the new view before detaching the old one because this ordering allows the player
            // to swap directly from one surface to another, without transitioning through a state where no
            // surface is attached. This is significantly more efficient and achieves a more seamless
            // transition when using platform provided video decoders.
            newPlayerView?.setPlayer(player)
            oldPlayerView?.setPlayer(null)
        }

        /**
         * Applies a texture rotation to a [TextureView].
         */
        private fun applyTextureViewRotation(textureView: TextureView, textureViewRotation: Int) {
            val transformMatrix = Matrix()
            val textureViewWidth = textureView.width.toFloat()
            val textureViewHeight = textureView.height.toFloat()
            if (textureViewWidth != 0f && textureViewHeight != 0f && textureViewRotation != 0) {
                val pivotX = textureViewWidth / 2
                val pivotY = textureViewHeight / 2
                transformMatrix.postRotate(textureViewRotation.toFloat(), pivotX, pivotY)

                // After rotation, scale the rotated texture to fit the TextureView size.
                val originalTextureRect = RectF(0f, 0f, textureViewWidth, textureViewHeight)
                val rotatedTextureRect = RectF()
                transformMatrix.mapRect(rotatedTextureRect, originalTextureRect)
                transformMatrix.postScale(
                    textureViewWidth / rotatedTextureRect.width(),
                    textureViewHeight / rotatedTextureRect.height(),
                    pivotX,
                    pivotY
                )
            }
            textureView.setTransform(transformMatrix)
        }
    }

    init {
        componentListener = ComponentListener()
        if (isInEditMode) {
            videoSurfaceView = null

        } else {
            val playerLayoutId = R.layout.exo_simple_player_view
            LayoutInflater.from(context).inflate(playerLayoutId, this)
            descendantFocusability = FOCUS_AFTER_DESCENDANTS

            // Content frame.
            videoSurfaceView = findViewById(R.id.surface_view)
            init()
        }
    }


    private var lastPos: Long? = 0
    private var lastFrameOfVideo: ImageView? = null;
    private var videoUri: Uri? = null;


    var cacheDataSourceFactory = CacheDataSourceFactory(
        Constants.simpleCache,
        DefaultHttpDataSourceFactory(
            Util.getUserAgent(
                context!!, context!!.getString(
                    R.string.app_name
                )
            )
        ),
        CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
    )

    fun init() {
        reset()

        /*Setup player + Adding Cache Directory*/
        val simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE;
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY) {
                    simpleExoPlayer.seekTo(lastPos!!)
                    alpha = 1f

                }
            }

        })

        simpleExoPlayer.playWhenReady = false
        setPlayer(simpleExoPlayer);

    }

    /**
     * This will resuse the player and will play new URI we have provided
     */
    fun startPlaying() {

        val mediaSource =
            ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(videoUri)
        (player as SimpleExoPlayer).prepare(mediaSource)

        player?.seekTo(lastPos!!)
        player?.playWhenReady = true


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
        val playerViewCopy = this;
        playerViewCopy!!.getPlayer()?.setPlayWhenReady(false)
        lastPos = playerViewCopy?.getPlayer()?.currentPosition
        reset()
        playerViewCopy?.getPlayer()?.stop(true)

    }

    fun reset() {
        // This will prevent surface view to show black screen,
        // and we will make it visible when it will be loaded
        alpha = 0f
    }

    fun setVideoUri(uri: Uri?) {
        this.videoUri = uri;
    }
}