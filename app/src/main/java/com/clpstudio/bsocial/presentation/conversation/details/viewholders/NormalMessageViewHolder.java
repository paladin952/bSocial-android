package com.clpstudio.bsocial.presentation.conversation.details.viewholders;

import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.clpstudio.bsocial.core.listeners.ClickListener;
import com.clpstudio.bsocial.data.models.conversations.Message;

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

    private boolean isUrl;
    private ClickListener<String> clickUrlListener;

    public NormalMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public NormalMessageViewHolder(View itemView, boolean isUrl) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.isUrl = isUrl;
    }

    public void setClickUrlListener(ClickListener<String> clickUrlListener) {
        this.clickUrlListener = clickUrlListener;
    }

    @Override
    public void bindMessage(Message message) {
        messageText.setText(message.getMessage());
        if (isUrl) {
            Linkify.addLinks(messageText, Linkify.WEB_URLS);
            messageText.setOnClickListener(v -> {
                if (clickUrlListener != null) {
                    clickUrlListener.click(message.getMessage());
                }
            });
        } else {
            messageText.setMovementMethod(null);
        }

        Glide.with(itemView.getContext())
                .load(message.getImageUrl())
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new GlideRoundedImageTarget(userImage));
    }
}
