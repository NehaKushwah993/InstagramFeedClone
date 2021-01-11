package com.nehak.instagramfeed.player

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.nehak.instagramfeed.R

/**
 * Create By Neha Kushwah
 * In order to remove blackish views from player, we extends this remove them manually
 */
class CustomExoPlayerView : PlayerView {
    constructor(context: Context?) : super(context!!) {
        removeView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        removeView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        removeView()
    }

    /**
     * This will remove unnecessary views that shows black screen over player
     */
    private fun removeView() {
        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        try {
            val shutter = findViewById<View>(R.id.exo_shutter)
            (shutter.parent as ViewGroup).removeView(shutter)
        } catch (e: Exception) {
            // not important
        }
        try {
            val overlayFrameLayout = findViewById<View>(R.id.exo_overlay)
            if (overlayFrameLayout != null) (overlayFrameLayout.parent as ViewGroup).removeView(
                overlayFrameLayout
            )
        } catch (e: Exception) {
            // not important
        }
        try {
            val artworkView = findViewById<View>(R.id.exo_artwork)
            if (artworkView != null) (artworkView.parent as ViewGroup).removeView(artworkView)
        } catch (e: Exception) {
            // not important
        }
        try {
            val adOverlayFrameLayout = findViewById<View>(R.id.exo_ad_overlay)
            if (adOverlayFrameLayout != null) (adOverlayFrameLayout.parent as ViewGroup).removeView(
                adOverlayFrameLayout
            )
        } catch (e: Exception) {
            // not important
        }
        try {
            val subtitleView = findViewById<View>(R.id.exo_subtitles)
            if (subtitleView != null) (subtitleView.parent as ViewGroup).removeView(subtitleView)
        } catch (e: Exception) {
            // not important
        }
        try {
            val controller = findViewById<View>(R.id.exo_controller)
            if (controller != null) (controller.parent as ViewGroup).removeView(controller)
        } catch (e: Exception) {
            // not important
        }
        try {
            if (videoSurfaceView != null) (videoSurfaceView as SurfaceView?)!!.holder.setFormat(
                PixelFormat.TRANSPARENT
            )
        } catch (e: Exception) {
            // not important
        }
    }
}