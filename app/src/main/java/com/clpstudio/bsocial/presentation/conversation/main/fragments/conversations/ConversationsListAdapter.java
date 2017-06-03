package com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.clpstudio.bsocial.core.listeners.ClickListener;
import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 5/10/17.
 */

public class ConversationsListAdapter extends RecyclerView.Adapter<ConversationsListAdapter.ViewHolder> {

    private List<ConversationViewModel> data = new ArrayList<>();
    private ClickListener<ConversationViewModel> clickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversations_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));

        if (clickListener != null) {
            holder.itemView.setOnClickListener(v -> clickListener.click(data.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<ConversationViewModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public ConversationViewModel getConversation(RegisteredUserViewModel user) {
        for (ConversationViewModel model : data) {
            if (model.getMembersIds().contains(user.getUserId())) {
                return model;
            }
        }
        return null;
    }

    public void setClickListener(ClickListener<ConversationViewModel> clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.title)
        TextView titleText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ConversationViewModel model) {
            titleText.setText(model.getTitle());

            Glide.with(itemView.getContext())
                    .load(model.getImageUrl())
                    .asBitmap()
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(new GlideRoundedImageTarget(imageView));
        }
    }

}
