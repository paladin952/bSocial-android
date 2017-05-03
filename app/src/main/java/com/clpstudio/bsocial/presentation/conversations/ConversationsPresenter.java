package com.clpstudio.bsocial.presentation.conversations;

import android.support.annotation.NonNull;

import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

/**
 * Created by clapalucian on 5/4/17.
 */

public class ConversationsPresenter extends BaseMvpPresenter<ConversationsPresenter.View>{

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    public ConversationsPresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void getConversations() {

    }

    public interface View extends IBaseMvpPresenter.View {

    }

}
