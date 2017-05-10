package com.clpstudio.bsocial.core.glide;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.clpstudio.bsocial.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifDrawableBuilder;

/**
 * Created by clapalucian on 5/6/17.
 */

public class GlideGifTarget extends SimpleTarget<byte[]> {

    public ImageView imageView;

    public GlideGifTarget(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onResourceReady(final byte[] resource,
                                final GlideAnimation<? super byte[]> glideAnimation) {
        final GifDrawable gifDrawable;
        try {
            gifDrawable = new GifDrawableBuilder()
                    .from(resource)
                    .build();
            imageView.setImageDrawable(gifDrawable);
        } catch (final IOException e) {
            imageView.setImageResource(R.drawable.ic_loading);
        }
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        imageView.setImageResource(R.drawable.ic_loading);
    }
}
