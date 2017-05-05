package com.clpstudio.bsocial.presentation.gifs;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.listeners.ClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 5/6/17.
 */

public class GifHorizontalListView extends RelativeLayout {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private GifHorizontalListAdapter adapter;
    private Context context;
    private ClickListener<String> clickListener;

    public GifHorizontalListView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public GifHorizontalListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public GifHorizontalListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GifHorizontalListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_gif_horizontal_list_view, this, true);

        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GifHorizontalListAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void setClickListener(ClickListener<String> clickListener) {
        this.clickListener = clickListener;
        adapter.setClickListener(clickListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void addData(List<String> data) {
        adapter.addAll(data);
    }
}
