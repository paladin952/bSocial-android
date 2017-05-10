package com.clpstudio.bsocial.presentation.conversation.main;

import android.support.annotation.NonNull;

import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.data.models.conversations.ConversationNameModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IProgressView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by clapalucian on 5/10/17.
 */

public class ConversationsListPresenter extends BaseMvpPresenter<ConversationsListPresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    ConversationService conversationService;

    @Inject
    public ConversationsListPresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        showData();
        showAvatar();
    }

    private void showData() {
        conversationService.getListOfConversations()
                .subscribe(conversationNameModels -> {
                    view().showData(conversationNameModels);
                }, err -> {
                    //todo
                });
    }

    private void showAvatar() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            if (firebaseUser.getPhotoUrl() != null) {
                view().showAvatar(firebaseUser.getPhotoUrl().toString());
            } else {
                view().showAvatar("");
            }
        }
    }

    public interface View extends IProgressView {

        void showAvatar(String url);

        void showData(List<ConversationNameModel> data);

    }
}
