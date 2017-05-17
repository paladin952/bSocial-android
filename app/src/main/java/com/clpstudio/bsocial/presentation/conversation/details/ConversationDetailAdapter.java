package com.clpstudio.bsocial.presentation.conversation.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.MessageTypedResolver;
import com.clpstudio.bsocial.data.models.conversations.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationDetailAdapter extends RecyclerView.Adapter<BaseConversationViewHolder> {

    private static final int TYPE_MESSAGE_MINE = 0;
    private static final int TYPE_MESSAGE_OTHERS = 1;
    private static final int TYPE_GIF_MINE = 2;
    private static final int TYPE_GIF_OTHERS = 3;

    private List<Message> data = new ArrayList<>();

    public ConversationDetailAdapter() {
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Message> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void append(Message model) {
        this.data.add(model);
        notifyItemInserted(this.data.size() - 1);
    }

    public void append(List<Message> newData) {
        int start = this.data.size();
        int end = start + newData.size() - 1;
        this.data.addAll(newData);
        notifyItemRangeChanged(start, end);
    }

    @Override
    public int getItemViewType(int position) {
        String message = data.get(position).getMessage();
        String username = data.get(position).getUserName();
        if (MessageTypedResolver.isGifMessage(message)) {
            if (isMine(username)) {
                return TYPE_GIF_MINE;
            } else {
                return TYPE_GIF_OTHERS;
            }
        } else {
            if (isMine(username)) {
                return TYPE_MESSAGE_MINE;
            } else {
                return TYPE_MESSAGE_OTHERS;
            }
        }
    }

    private boolean isMine(String username) {
        return username.equals("luci");
    }

    @Override
    public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MESSAGE_MINE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_mine, parent, false);
        } else if (viewType == TYPE_MESSAGE_OTHERS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_others, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_gif, parent, false);
            boolean isOthers = false;
            if (viewType == TYPE_GIF_OTHERS) {
                isOthers = true;
            }
            return new GifMessageViewHolder(view, isOthers);
        }
        return new NormalMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseConversationViewHolder holder, int position) {
        String message = data.get(position).getMessage();
        if (holder instanceof NormalMessageViewHolder) {
            holder.bindMessage(message);
        } else if (holder instanceof GifMessageViewHolder) {
            holder.bindGiphyView(message);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
