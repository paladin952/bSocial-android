package com.clpstudio.bsocial.core.glide;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class GlideRoundedImageTarget extends BitmapImageViewTarget {
    public GlideRoundedImageTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable circularBitmapDrawable
                = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        view.setImageDrawable(circularBitmapDrawable);
        afterRounding();
    }

    protected void afterRounding() {
        // override if needed
    }
}
