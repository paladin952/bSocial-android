package com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations;

import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IProgressView;
import com.google.firebase.auth.FirebaseAuth;

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

    public void showData() {
        view().showProgress();
        conversationService.getListOfConversations()
                .subscribe(conversationNameModels -> {
                    view().hideProgress();
                    view().showData(conversationNameModels);
                }, err -> {
                    view().hideProgress();
                    //todo
                });
    }


    public interface View extends IProgressView {

        void showData(List<ConversationModel> data);

    }
}
