package com.clpstudio.bsocial.presentation.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.authenticate.AuthenticateActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.clpstudio.bsocial.R.id.avatar;

public class ProfilePageActivity extends AppCompatActivity implements ProfilePagePresenter.View, EditAvatarFragment.RefreshImageListener {

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AlertDialog alertDialog;

    @OnClick(R.id.nickname_container)
    public void onNicknameClick() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setText(nicknameText.getText().toString());
        edittext.requestFocus();
        alertDialog = alert.setMessage(getString(R.string.enter_your_nickname))
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

    @OnClick(R.id.logout)
    public void onLogoutClick() {
        presenter.logout();
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

        setupToolbar();
        avatarEditText.getBackground().setLevel(3000);
        presenter.bindView(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void downloadProfileImage(String url) {
        Glide.with(this)
                .load(url)
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

    @Override
    public void gotoAuthenticatePage() {
        AuthenticateActivity.startActivity(this);
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
