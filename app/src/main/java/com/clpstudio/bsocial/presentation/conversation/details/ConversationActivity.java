package com.clpstudio.bsocial.presentation.conversation.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.c.Henson;
import com.clpstudio.bsocial.presentation.gifs.GifHorizontalListView;
import com.clpstudio.bsocial.presentation.gifs.GifPresenter;
import com.clpstudio.bsocial.presentation.views.MessageEditorView;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.clpstudio.bsocial.R.id.messageEditor;

public class ConversationActivity extends AppCompatActivity implements ConversationPresenter.View, GifPresenter.View {

    @Inject
    ConversationPresenter presenter;
    @Inject
    GifPresenter gifPresenter;

    @InjectExtra("name")
    String conversationName;

    @BindView(R.id.conversationsRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(messageEditor)
    MessageEditorView messageEditorView;
    @BindView(R.id.gif_list)
    GifHorizontalListView gifList;

    @OnClick(R.id.toolbar_call)
    public void onCallClick() {
        Log.d("luci", "call clicked!");
    }

    private ConversationAdapter adapter;

    public static void startActivity(Activity activity, String name) {
        Intent intent = Henson.with(activity)
                .gotoConversationActivity()
                .name(name)
                .build();

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);
        ButterKnife.bind(this);
        Dart.inject(this);

        Glide.with(this).load(R.drawable.bg_default_conversation).into(background);
        setupToolbar();
        setupMessageEditor();
        adapter = new ConversationAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        setupGifList();
        presenter.bindView(this);
        gifPresenter.bindView(this);
    }

    private void setupGifList() {
        gifList.setClickListener(element -> {
            presenter.onGifSelected(element);
        });
    }

    private void setupMessageEditor() {
        messageEditorView.setOnTextSubmitedListener(new MessageEditorView.OnTextSubmited() {
            @Override
            public void onTextSubmited(String text) {
                presenter.onTextSubmited(text);
            }

            @Override
            public void onGifSelected(String gifText) {
                gifPresenter.getGifs(gifText);
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(conversationName);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void showData(List<ConversationModel> data) {
        adapter.addAll(data);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void appendData(ConversationModel data) {
        adapter.append(data);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
        gifPresenter.unbindView();
    }

    @Override
    public void showGifs(List<String> urls) {
        gifList.addData(urls);
        gifList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (gifList.getVisibility() == View.VISIBLE) {
            gifList.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
