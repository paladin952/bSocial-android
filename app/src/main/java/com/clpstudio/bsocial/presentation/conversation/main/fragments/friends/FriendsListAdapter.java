package com.clpstudio.bsocial.presentation.conversation.main.fragments.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.listeners.ClickListener;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private List<RegisteredUser> data = new ArrayList<>();
    private ClickListener<RegisteredUser> onClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(v -> onClickListener.click(data.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<RegisteredUser> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void append(RegisteredUser model) {
        this.data.add(model);
        notifyItemInserted(this.data.size());
    }

    public void setOnClickListener(ClickListener<RegisteredUser> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.email)
        TextView emailText;

        @BindView(R.id.avatar)
        ImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RegisteredUser model) {
            emailText.setText(model.getEmail());

            //TODO load profile image for each user
        }
    }

}
