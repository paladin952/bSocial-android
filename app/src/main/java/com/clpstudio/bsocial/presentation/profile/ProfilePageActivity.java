package com.clpstudio.bsocial.presentation.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.clpstudio.bsocial.presentation.BSocialApplication;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.clpstudio.bsocial.R.id.avatar;

public class ProfilePageActivity extends AppCompatActivity implements ProfilePagePresenter.View, EditAvatarFragment.OnUploadFinishedListener {

    @Inject
    ProfilePagePresenter presenter;

    @BindView(avatar)
    ImageView avatarImage;
    @BindView(R.id.avatar_edit)
    TextView avatarEditText;

    @OnClick(R.id.avatar)
    public void onAvatarClick() {
        EditAvatarFragment.show(getSupportFragmentManager());
    }

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ProfilePageActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ButterKnife.bind(this);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);

        avatarEditText.getBackground().setLevel(3000);
        presenter.bindView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.refreshImage();
    }

    @Override
    public void downloadProfileImage(String storageReference) {

        Glide.with(this)
                .load(storageReference)
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new GlideRoundedImageTarget(avatarImage));

    }

    @Override
    public void refreshProfileImage() {
        presenter.refreshImage();
    }
}
