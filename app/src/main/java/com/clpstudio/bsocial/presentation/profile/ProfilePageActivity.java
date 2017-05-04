package com.clpstudio.bsocial.presentation.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.email)
    TextView emailText;
    @BindView(R.id.nickname)
    TextView nicknameText;

    @OnClick(R.id.nickname_container)
    public void onNicknameClick() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setText(nicknameText.getText().toString());
        edittext.requestFocus();
        alert.setMessage(getString(R.string.enter_your_nickname))
                .setView(edittext)
                .setPositiveButton(getString(R.string.save), (dialog, whichButton) -> {
                    String nickname = edittext.getText().toString();
                    presenter.updateNickname(nickname);
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, whichButton) -> dialog.dismiss())
                .show();
    }

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
    public void setEmail(String email) {
        emailText.setText(email);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setNickname(String nickname) {
        nicknameText.setText(nickname);
    }

    @Override
    public void refreshProfileImage() {
        presenter.refreshImage();
    }
}
