package com.clpstudio.bsocial.presentation.conversation;

import android.support.annotation.NonNull;
import android.util.Log;

import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by clapalucian on 5/4/17.
 */

public class ConversationPresenter extends BaseMvpPresenter<ConversationPresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    ConversationService conversationService;

    @Inject
    public ConversationPresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        getConversations("");
    }

    public void getConversations(String conversationId) {
        conversationService.getMessages(conversationId)
                .subscribe(conversationModels -> {
                    view().showData(conversationModels);
                }, err -> {
                    Log.d("luci", "error = " + err.getMessage());
                });
    }

    public interface View extends IBaseMvpPresenter.View {

        void showData(List<ConversationModel> data);

    }

}
