package com.clpstudio.bsocial.presentation.conversation.details;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by clapalucian on 5/6/17.
 */

public class BaseConversationViewHolder extends RecyclerView.ViewHolder {
    public BaseConversationViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Show a normal message
     *
     * @param message The message's text
     */
    public void bindMessage(String message) {
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

}
