package com.clpstudio.bsocial.presentation.conversation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 5/6/17.
 */

public class NormalMessageViewHolder extends BaseConversationViewHolder {

    @BindView(R.id.message)
    TextView messageText;
    @BindView(R.id.userImage)
    ImageView userImage;

    public NormalMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindMessage(String message) {
        messageText.setText(message);


        //todo replace with real user's profile picture
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String url = "";
        if (firebaseUser != null) {
            if (firebaseUser.getPhotoUrl() != null) {
                url = firebaseUser.getPhotoUrl().toString();
            }
        }
        Glide.with(itemView.getContext())
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new GlideRoundedImageTarget(userImage));
    }
}
