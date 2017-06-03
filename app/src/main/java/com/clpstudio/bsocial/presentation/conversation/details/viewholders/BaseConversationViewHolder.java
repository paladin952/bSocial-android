package com.clpstudio.bsocial.presentation.conversation.details.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;

/**
 * Created by clapalucian on 5/6/17.
 */

public class BaseConversationViewHolder extends RecyclerView.ViewHolder {
    public BaseConversationViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Show a normal messageViewModel
     *
     * @param messageViewModel The messageViewModel's text
     */
    public void bindMessage(MessageViewModel messageViewModel) {
        //override when needed
    }

    /**
     * Load a gif view
     *
     * @param url The url to be loaded
     */
    public void bindGiphyView(String url) {
        //override when needed
    }

    /**
     * Use with a webView
     *
     * @param url The url to be loaded
     */
    public void bindWebPage(String url) {
        //override when needed
    }

    public void bindPhoto(String url){
        //override when needed
    }

}
