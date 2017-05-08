package com.clpstudio.bsocial.presentation.conversation.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationAdapter extends RecyclerView.Adapter<BaseConversationViewHolder> {

    private static final int TYPE_MESSAGE_MINE = 0;
    private static final int TYPE_MESSAGE_OTHERS = 1;

    private List<ConversationModel> data = new ArrayList<>();

    public ConversationAdapter() {
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ConversationModel> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void append(ConversationModel model) {
        this.data.add(model);
        notifyItemInserted(this.data.size() - 1);
    }

    public void append(List<ConversationModel> newData) {
        int start = this.data.size();
        int end = start + newData.size() - 1;
        this.data.addAll(newData);
        notifyItemRangeChanged(start, end);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) != null && data.get(position).getUserName().equals("luci")) {
            return TYPE_MESSAGE_MINE;
        } else {
            return TYPE_MESSAGE_OTHERS;
        }
    }

    @Override
    public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MESSAGE_MINE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_mine, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_others, parent, false);
        }
        return new NormalMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseConversationViewHolder holder, int position) {
        holder.bindMessage(data.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
