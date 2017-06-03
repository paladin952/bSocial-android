package com.clpstudio.bsocial.presentation.conversation.details.viewholders;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class PhotoMessageViewHolderJ extends BaseConversationViewHolder {

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.root)
    LinearLayout root;

    private boolean isOthers;

    public PhotoMessageViewHolderJ(View itemView, boolean isOthers) {
        super(itemView);
        this.isOthers = isOthers;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindPhoto(String url) {
        super.bindPhoto(url);

        if (isOthers) {
            root.setGravity(Gravity.LEFT);
        }

        Glide.with(itemView.getContext())
                .load(url)
                .error(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo);
    }
}
