package com.clpstudio.bsocial.presentation.conversation.details;

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

public class ConversationDetailPresenter extends BaseMvpPresenter<ConversationDetailPresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    ConversationService conversationService;

    @Inject
    public ConversationDetailPresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
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

    public void onTextSubmited(String text) {
        //todo add real username and stuff
        ConversationModel model = new ConversationModel("luci", text);
        view().appendData(model);
    }

    public void onGifSelected(String url) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();
            //todo change to email
            view().appendData(new ConversationModel("luci", url));
            view().clearInput();
        }
    }

    public interface View extends IBaseMvpPresenter.View {

        void showData(List<ConversationModel> data);

        void appendData(ConversationModel data);

        void clearInput();

    }

}
