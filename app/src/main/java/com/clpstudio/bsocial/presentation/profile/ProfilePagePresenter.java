package com.clpstudio.bsocial.presentation.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.service.ProfileService;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class ProfilePagePresenter extends BaseMvpPresenter<ProfilePagePresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    Context context;
    @Inject
    ProfileService profileService;

    @Inject
    public ProfilePagePresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        refreshImage();
        setEmail();
        setNickname();
    }

    private void setNickname() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null && !TextUtils.isEmpty(firebaseUser.getDisplayName())) {
            view().setNickname(firebaseUser.getDisplayName());
        } else {
            view().setNickname(context.getString(R.string.profile_nickname_empty));
        }
    }

    public void updateNickname(String nickname) {
        view().setNickname(nickname);
        profileService.updateNickname(nickname)
                .subscribe(
                        () -> view().showToast(context.getString(R.string.nickname_updated_successfuly)),
                        err -> {
                            view().setNickname(context.getString(R.string.profile_nickname_empty));
                            view().showToast(context.getString(R.string.nickname_updated_failed));
                        }
                );
    }

    private void setEmail() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            view().setEmail(firebaseUser.getEmail());
        } else {
            view().setEmail(context.getString(R.string.no_email));
        }
    }

    public void refreshImage() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            if (firebaseUser.getPhotoUrl() != null) {
                view().downloadProfileImage(firebaseUser.getPhotoUrl().toString());
            }
        }
    }

    public interface View extends IBaseMvpPresenter.View {

        void downloadProfileImage(String url);

        void setEmail(String email);

        void showToast(String message);

        void setNickname(String nickname);
    }

}
