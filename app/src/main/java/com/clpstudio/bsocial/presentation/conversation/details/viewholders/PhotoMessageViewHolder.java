package com.clpstudio.bsocial.presentation.conversation.details.viewholders;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.listeners.ClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class PhotoMessageViewHolder extends BaseConversationViewHolder {

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.root)
    LinearLayout root;

    private boolean isOthers;
    private String url;
    private ClickListener<String> clickListener;

    public PhotoMessageViewHolder(View itemView, boolean isOthers) {
        super(itemView);
        this.isOthers = isOthers;
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.photo)
    public void onPhotoClick() {
        if(clickListener != null && !TextUtils.isEmpty(url)) {
            clickListener.click(url);
        }
    }

    @Override
    public void bindPhoto(String url) {
        super.bindPhoto(url);
        this.url = url;

        if (isOthers) {
            root.setGravity(Gravity.LEFT);
        }

        Glide.with(itemView.getContext())
                .load(url)
                .error(R.drawable.default_avatar)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photo);
    }

    public void setClickListener(ClickListener<String> clickListener) {
        this.clickListener = clickListener;
    }
}
