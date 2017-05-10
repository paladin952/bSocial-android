package com.clpstudio.bsocial.presentation.conversation.main;

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
import com.clpstudio.bsocial.data.models.conversations.ConversationNameModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 5/10/17.
 */

public class ConversationsListAdapter extends RecyclerView.Adapter<ConversationsListAdapter.ViewHolder> {

    private List<ConversationNameModel> data = new ArrayList<>();
    private ClickListener<ConversationNameModel> clickListener;

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

    public void addAll(List<ConversationNameModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener<ConversationNameModel> clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.name)
        TextView nameText;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ConversationNameModel model) {
            nameText.setText(model.getName());

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
