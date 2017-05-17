package com.clpstudio.bsocial.presentation.conversation.main.fragments.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.ui.FriendsListItemModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private List<FriendsListItemModel> data = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<FriendsListItemModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void append(FriendsListItemModel model) {
        this.data.add(model);
        notifyItemInserted(this.data.size());
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

        public void bind(FriendsListItemModel model) {
            emailText.setText(model.getEmail());

            //TODO load profile image for each user
        }
    }

}
