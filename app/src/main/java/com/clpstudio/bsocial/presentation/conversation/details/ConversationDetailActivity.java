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
import com.clpstudio.bsocial.Henson;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.data.models.conversations.Message;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.browser.BrowserViewActivity;
import com.clpstudio.bsocial.presentation.gifs.GifHorizontalListView;
import com.clpstudio.bsocial.presentation.gifs.GifPresenter;
import com.clpstudio.bsocial.presentation.views.MessageEditorView;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.clpstudio.bsocial.R.id.messageEditor;

public class ConversationDetailActivity extends AppCompatActivity implements ConversationDetailPresenter.View, GifPresenter.View {

    @Inject
    ConversationDetailPresenter presenter;
    @Inject
    GifPresenter gifPresenter;
    @Inject
    FirebaseAuth firebaseAuth;

    @Nullable
    @InjectExtra
    ConversationModel conversationModel;
    @Nullable
    @InjectExtra("user")
    RegisteredUser user;

    @Nullable
    @InjectExtra
    boolean isNewConversation;

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
        //TODO
        Log.d("luci", "call clicked!");
    }

    @OnClick(R.id.avatar)
    public void onAvatarClick(ImageView imageView) {
        presenter.onAvatarImageClick();
    }

    ConversationDetailAdapter.OnConversationMessagesClickListener messagesClickListener = new ConversationDetailAdapter.OnConversationMessagesClickListener() {
        @Override
        public void openLink(String url) {
            BrowserViewActivity.startActivity(ConversationDetailActivity.this, url);
        }

        @Override
        public void showPhoto(String path) {

        }
    };

    private ConversationDetailAdapter adapter;

    public static void startActivity(Activity activity, ConversationModel conversationModel) {
        Intent intent = Henson.with(activity)
                .gotoConversationDetailActivity()
                .conversationModel(conversationModel)
                .build();

        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, RegisteredUser user) {
        Intent intent = Henson.with(activity)
                .gotoConversationDetailActivity()
                .user(user)
                .isNewConversation(true)
                .build();

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_detail);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);
        ButterKnife.bind(this);
        Dart.inject(this);

        Glide.with(this).load(R.drawable.bg_default_conversation).into(background);
        setupToolbar();
        setupMessageEditor();
        setupList();
        setupGifList();

        presenter.bindView(this);
        if (isNewConversation) {
            presenter.bindToNewConversation(user);
        } else {
            presenter.setConversationId(conversationModel.getId());
            presenter.bindToOldConversation();

        }
        gifPresenter.bindView(this);
    }

    private void setupList() {
        adapter = new ConversationDetailAdapter(firebaseAuth.getCurrentUser().getEmail());
        adapter.setClickListener(messagesClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupGifList() {
        gifList.setClickListener(element -> {
            presenter.onGifSelected(element);
            gifPresenter.onGifSelected();
        });
    }

    private void setupMessageEditor() {
        messageEditorView.setOnTextListenerListener(new MessageEditorView.OnTextListener() {
            @Override
            public void onTextSubmitted(String text) {
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

        if (isNewConversation) {
            String title = user.getEmail();
            actionBar.setTitle(title);
        } else {
            actionBar.setTitle(conversationModel.getTitle());
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void showData(List<Message> data) {
        adapter.addAll(data);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void appendData(Message data) {
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
    public void hideGifs() {
        gifList.setVisibility(View.GONE);
        gifList.clear();
    }

    @Override
    public void onBackPressed() {
        if (gifList.getVisibility() == View.VISIBLE) {
            gifList.setVisibility(View.GONE);
            messageEditorView.clear();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void clearInput() {
        messageEditorView.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
