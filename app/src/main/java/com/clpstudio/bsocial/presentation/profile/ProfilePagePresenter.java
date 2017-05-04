package com.clpstudio.bsocial.presentation.profile;

import android.content.Context;
import android.support.annotation.NonNull;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Inject;

public class ProfilePagePresenter extends BaseMvpPresenter<ProfilePagePresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    FirebaseStorage firebaseStorage;
    @Inject
    Context context;

    @Inject
    public ProfilePagePresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        refreshImage();
        setEmail();
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
    }

}
