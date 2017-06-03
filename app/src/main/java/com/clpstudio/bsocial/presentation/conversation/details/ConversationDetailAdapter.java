package com.clpstudio.bsocial.presentation.conversation.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.clpstudio.bsocial.presentation.conversation.details.viewholders.BaseConversationViewHolder;
import com.clpstudio.bsocial.presentation.conversation.details.viewholders.GifMessageViewHolder;
import com.clpstudio.bsocial.presentation.conversation.details.viewholders.NormalMessageViewHolder;
import com.clpstudio.bsocial.presentation.conversation.details.viewholders.PhotoMessageViewHolderJ;

import java.util.ArrayList;
import java.util.List;

import static com.clpstudio.bsocial.data.models.conversations.MessageViewModel.TYPE_LINK;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationDetailAdapter extends RecyclerView.Adapter<BaseConversationViewHolder> {

    private static final int TYPE_MESSAGE_MINE = 0;
    private static final int TYPE_MESSAGE_OTHERS = 1;
    private static final int TYPE_GIF_MINE = 2;
    private static final int TYPE_GIF_OTHERS = 3;
    private static final int TYPE_LINK_MINE = 4;
    private static final int TYPE_LINK_OTHERS = 5;
    private static final int TYPE_PHOTO_MINE = 6;
    private static final int TYPE_PHOTO_OTHERS = 7;

    private List<MessageViewModel> data = new ArrayList<>();
    private String loggedUsername;
    private OnConversationMessagesClickListener clickListener;

    public interface OnConversationMessagesClickListener {
        void openLink(String url);

        void showPhoto(String path);
    }

    public ConversationDetailAdapter(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<MessageViewModel> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void append(MessageViewModel model) {
        this.data.add(model);
        notifyItemInserted(this.data.size() - 1);
    }

    public void append(List<MessageViewModel> newData) {
        int start = this.data.size();
        int end = start + newData.size() - 1;
        this.data.addAll(newData);
        notifyItemRangeChanged(start, end);
    }

    @Override
    public int getItemViewType(int position) {
        String username = data.get(position).getUserName();
        int type = data.get(position).getType();
        if (type == MessageViewModel.TYPE_GIF) {
            if (isMine(username)) {
                return TYPE_GIF_MINE;
            } else {
                return TYPE_GIF_OTHERS;
            }
        } else if (type == TYPE_LINK) {
            if (isMine(username)) {
                return TYPE_LINK_MINE;
            } else {
                return TYPE_LINK_OTHERS;
            }
        } else if (type == MessageViewModel.TYPE_PHOTO) {
            if (isMine(username)) {
                return TYPE_PHOTO_MINE;
            } else {
                return TYPE_PHOTO_OTHERS;
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
        return username.equals(loggedUsername);
    }

    @Override
    public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MESSAGE_MINE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_mine, parent, false);
        } else if (viewType == TYPE_MESSAGE_OTHERS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_others, parent, false);
        } else if (viewType == TYPE_LINK_MINE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_mine, parent, false);
            return new NormalMessageViewHolder(view, true);
        } else if (viewType == TYPE_LINK_OTHERS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_message_others, parent, false);
            return new NormalMessageViewHolder(view, true);
        } else if (viewType == TYPE_PHOTO_MINE || viewType == TYPE_PHOTO_OTHERS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_conversation_list_item_photo, parent, false);

            boolean isOthers = viewType == TYPE_PHOTO_OTHERS;
            return new PhotoMessageViewHolderJ(view, isOthers);

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
            holder.bindMessage(data.get(position));
            ((NormalMessageViewHolder) holder).setClickUrlListener(element -> {
                if (clickListener != null) {
                    clickListener.openLink(element);
                }
            });
        } else if (holder instanceof GifMessageViewHolder) {
            holder.bindGiphyView(message);
        } else if (holder instanceof PhotoMessageViewHolderJ) {
            holder.bindPhoto(message);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(OnConversationMessagesClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
