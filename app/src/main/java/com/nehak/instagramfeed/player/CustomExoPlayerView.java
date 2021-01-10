package com.nehak.instagramfeed.player;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.nehak.instagramfeed.R;

/**
 * In order to remove blackish views from player, we extends this remove them manually
 */
public class CustomExoPlayerView extends PlayerView {
    public CustomExoPlayerView(Context context) {
        super(context);
        removeView();
    }

    public CustomExoPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        removeView();
    }

    public CustomExoPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeView();
    }

    /**
     * This will remove unnecessary views that shows black screen over player
     */
    private void removeView() {
        setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        try {
            View shutter = findViewById(R.id.exo_shutter);
            ((ViewGroup) shutter.getParent()).removeView(shutter);
        } catch (Exception e) {
            // not important
        }

        try {
            View overlayFrameLayout = findViewById(R.id.exo_overlay);

            if (overlayFrameLayout != null)
                ((ViewGroup) overlayFrameLayout.getParent()).removeView(overlayFrameLayout);
        } catch (Exception e) {
            // not important
        }
        try {
            View artworkView = findViewById(R.id.exo_artwork);
            if (artworkView != null)
                ((ViewGroup) artworkView.getParent()).removeView(artworkView);
        } catch (Exception e) {
            // not important
        }
        try {
            View adOverlayFrameLayout = findViewById(R.id.exo_ad_overlay);

            if (adOverlayFrameLayout != null)
                ((ViewGroup) adOverlayFrameLayout.getParent()).removeView(adOverlayFrameLayout);
        } catch (Exception e) {
            // not important
        }
        try {
            View subtitleView = findViewById(R.id.exo_subtitles);

            if (subtitleView != null)
                ((ViewGroup) subtitleView.getParent()).removeView(subtitleView);
        } catch (Exception e) {
            // not important
        }
        try {
            View controller = findViewById(R.id.exo_controller);

            if (controller != null)
                ((ViewGroup) controller.getParent()).removeView(controller);
        } catch (Exception e) {
            // not important
        }
        try {
            if (getVideoSurfaceView() != null)
                ((SurfaceView) getVideoSurfaceView()).getHolder().setFormat(PixelFormat.TRANSPARENT);
        } catch (Exception e) {
            // not important
        }
    }
}
