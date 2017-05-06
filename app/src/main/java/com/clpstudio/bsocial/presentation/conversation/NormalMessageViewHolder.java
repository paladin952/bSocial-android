package com.clpstudio.bsocial.presentation.conversation;

import android.view.View;
import android.widget.TextView;

import com.clpstudio.bsocial.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 5/6/17.
 */

public class NormalMessageViewHolder extends BaseConversationViewHolder {

    @BindView(R.id.message)
    TextView messageText;

    public NormalMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindMessage(String message) {
        messageText.setText(message);
    }
}
