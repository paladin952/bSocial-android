package com.clpstudio.bsocial.presentation.conversation.details.viewholders;

import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideGifTarget;
import com.clpstudio.bsocial.presentation.conversation.details.viewholders.BaseConversationViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by clapalucian on 5/12/17.
 */

public class GifMessageViewHolder extends BaseConversationViewHolder {

    private boolean isOthers;

    @BindView(R.id.gif_root)
    LinearLayout root;
    @BindView(R.id.gif_image)
    GifImageView gifImageView;

    public GifMessageViewHolder(View itemView, boolean isOthers) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.isOthers = isOthers;
    }

    @Override
    public void bindGiphyView(String url) {
        if (isOthers) {
            root.setGravity(Gravity.LEFT);
        }

        gifImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Glide.with(itemView.getContext())
                        .load(url)
                        .asGif()
                        .toBytes()
                        .thumbnail(0.1f)
                        .override(gifImageView.getWidth(), gifImageView.getHeight())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new GlideGifTarget(gifImageView));
                gifImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }
}
