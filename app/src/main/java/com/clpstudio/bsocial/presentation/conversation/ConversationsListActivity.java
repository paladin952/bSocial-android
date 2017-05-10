package com.clpstudio.bsocial.presentation.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.profile.ProfilePageActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.clpstudio.bsocial.R.id.avatar;

/**
 * Created by clapalucian on 5/8/17.
 */

public class ConversationsListActivity extends AppCompatActivity implements ConversationsListPresenter.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ConversationsListPresenter presenter;

    private ImageView avatarImageView;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ConversationsListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        ButterKnife.bind(this);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);

        setupToolbar();
        presenter.bindView(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        avatarImageView = (ImageView) toolbar.findViewById(avatar);
        avatarImageView.setOnClickListener(v -> ProfilePageActivity.startActivity(this));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showAvatar(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new GlideRoundedImageTarget(avatarImageView));
    }
}
