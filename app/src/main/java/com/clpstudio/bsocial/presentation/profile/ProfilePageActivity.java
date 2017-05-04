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

public class ProfilePageActivity extends AppCompatActivity implements ProfilePagePresenter.View {

    @Inject
    ProfilePagePresenter presenter;

    @BindView(avatar)
    ImageView avatarImage;
    @BindView(R.id.avatar_edit)
    TextView avatarEditText;

    @OnClick(R.id.avatar)
    public void onAvatarClick() {

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
    public void downloadProfileImage(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new GlideRoundedImageTarget(avatarImage));

    }
}
