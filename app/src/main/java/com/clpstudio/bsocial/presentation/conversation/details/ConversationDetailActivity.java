package com.clpstudio.bsocial.presentation.conversation.details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.Henson;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.background.FirebaseWaitingService;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.browser.BrowserViewActivity;
import com.clpstudio.bsocial.presentation.calling.PlaceCallSinchActivity;
import com.clpstudio.bsocial.presentation.conversation.main.TakePhotoPresenter;
import com.clpstudio.bsocial.presentation.gifs.GifHorizontalListView;
import com.clpstudio.bsocial.presentation.gifs.GifPresenter;
import com.clpstudio.bsocial.presentation.profile.EditAvatarDialogFragment;
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

public class ConversationDetailActivity extends AppCompatActivity implements ConversationDetailPresenter.View, GifPresenter.View, TakePhotoPresenter.View,
        EditAvatarDialogFragment.OnItemsClickListener {

    @Inject
    ConversationDetailPresenter presenter;
    @Inject
    GifPresenter gifPresenter;
    @Inject
    TakePhotoPresenter takePhotoPresenter;
    @Inject
    FirebaseAuth firebaseAuth;

    @Nullable
    @InjectExtra
    ConversationViewModel conversationViewModel;
    @Nullable
    @InjectExtra("user")
    RegisteredUserViewModel user;

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
    @BindView(R.id.avatar)
    ImageView avatarImage;

    @OnClick(R.id.toolbar_call)
    public void onCallClick() {
        presenter.callClicked();
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
        public void showPhoto(String url) {
            ZoomablePictureActivity.startActivity(ConversationDetailActivity.this, url);
        }
    };

    private ConversationDetailAdapter adapter;

    public static Intent getStartIntent(Context activity, ConversationViewModel conversationViewModel) {
        return Henson.with(activity)
                .gotoConversationDetailActivity()
                .conversationViewModel(conversationViewModel)
                .build();
    }

    public static void startActivity(Activity activity, ConversationViewModel conversationViewModel) {
        Intent intent = Henson.with(activity)
                .gotoConversationDetailActivity()
                .conversationViewModel(conversationViewModel)
                .build();

        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, RegisteredUserViewModel user) {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.bindView(this);
        if (isNewConversation) {
            presenter.bindToNewConversation(user);
        } else {
            presenter.bindToOldConversation(conversationViewModel);
        }
        gifPresenter.bindView(this);
        takePhotoPresenter.bindView(this);
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
        messageEditorView.setEventListener(new MessageEditorView.OnTextListener() {
            @Override
            public void onTextSubmitted(String text) {
                presenter.onTextSubmited(text);
            }

            @Override
            public void onGifSelected(String gifText) {
                gifPresenter.getGifs(gifText);
            }

            @Override
            public void onTapAttachment() {
                EditAvatarDialogFragment.showFromConversationDetail(getSupportFragmentManager(), getString(R.string.conversation_select_attachment), true);
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void call(String userEmail) {
        PlaceCallSinchActivity.startActivity(this, userEmail, true);
    }

    @Override
    public void clearList() {
        adapter.clear();
    }

    @Override
    public void bindFirebaseService(String conversationId) {
        Intent intent = new Intent(this, FirebaseWaitingService.class);
        intent.putExtra(FirebaseWaitingService.BUNDLE_KEY_CONVERSATION, conversationId);
        startService(intent);
    }

    @Override
    public void showData(List<MessageViewModel> data) {
        adapter.addAll(data);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void appendData(MessageViewModel data) {
        adapter.append(data);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }


    @Override
    public void onStop() {
        presenter.unbindView();
        gifPresenter.unbindView();
        takePhotoPresenter.unbindView();
        super.onStop();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePhotoPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePhotoPresenter.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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

    @Override
    public void imagePhotoTaken(String filename, Uri path) {
        presenter.uploadImage(filename, path);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAvatar(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new GlideRoundedImageTarget(avatarImage));
    }

    @Override
    public void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void onClickTakePhoto() {
        takePhotoPresenter.takePhoto(ConversationDetailActivity.this);
    }

    @Override
    public void onClickSelectPhoto() {
        takePhotoPresenter.selectPhoto(ConversationDetailActivity.this);
    }
}
