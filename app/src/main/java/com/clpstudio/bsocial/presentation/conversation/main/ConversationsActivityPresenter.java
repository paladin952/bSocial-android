package com.clpstudio.bsocial.presentation.conversation.main;

import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class ConversationsActivityPresenter extends BaseMvpPresenter<ConversationsActivityPresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    public ConversationsActivityPresenter() {
    }

    public void showAvatar() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            if (firebaseUser.getPhotoUrl() != null) {
                view().showAvatar(firebaseUser.getPhotoUrl().toString());
            } else {
                view().showAvatar("");
            }
        }
    }

    public interface View extends IBaseMvpPresenter.View {

        void showAvatar(String url);

    }
}
