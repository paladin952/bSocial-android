package com.clpstudio.bsocial.presentation.gifs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideGifTarget;
import com.clpstudio.bsocial.core.listeners.ClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by clapalucian on 5/6/17.
 */

public class GifHorizontalListAdapter extends RecyclerView.Adapter<GifHorizontalListAdapter.ViewHolder> {

    private static final int GIF_IMAGE_HEIGHT_DP = 150;
    private static final int GIF_IMAGE_WIDTH_DP = GIF_IMAGE_HEIGHT_DP;

    private ClickListener<String> clickListener;
    private List<String> data = new ArrayList<>();

    public GifHorizontalListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gifs_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (clickListener != null) {
            holder.itemView.setOnClickListener(view -> clickListener.click(data.get(position)));
        }
        holder.bind(data.get(position));
    }

    public void addAll(List<String> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(ClickListener<String> clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        GifImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String url) {
            image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Glide.with(itemView.getContext())
                            .load(url)
                            .asGif()
                            .toBytes()
                            .thumbnail(0.1f)
                            .override(image.getWidth(), image.getHeight())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(new GlideGifTarget(image));
                    image.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });


        }
    }

}
