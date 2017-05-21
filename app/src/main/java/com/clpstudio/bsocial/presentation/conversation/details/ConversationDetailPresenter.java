package com.clpstudio.bsocial.presentation.conversation.details;

import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.data.models.conversations.Message;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;

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

    private String conversationId;

    @Inject
    public ConversationDetailPresenter() {
    }

    private void subscribeMessageAdded() {
        conversationService.subscribeMessageAdded(conversationId)
                .subscribe(message -> {
                    view().appendData(message);
                    view().clearInput();
                }, err -> {
                    //TODO
                });
    }

    public void setConversationId(String id) {
        this.conversationId = id;
    }

    public void getConversations(String conversationId) {
        conversationService.getMessages(conversationId).subscribe();
    }

    public void onTextSubmited(String text) {
        sendMessage(text);
    }

    public void onGifSelected(String url) {
        sendMessage(url);
    }

    private void sendMessage(String text) {
        Message message = new Message(firebaseAuth.getCurrentUser().getEmail(), text, System.currentTimeMillis());
        conversationService.sendMessage(conversationId, message).subscribe();
    }

    public void subscribeToOldConversation() {
        subscribeMessageAdded();
        getConversations(conversationId);
    }

    public void subscribeToNewConversation(RegisteredUser friend) {
        conversationService.createConversation(friend)
                .subscribe(s -> {
                    conversationId = s;
                    subscribeMessageAdded();
                });
    }

    public interface View extends IBaseMvpPresenter.View {

        void showData(List<Message> data);

        void appendData(Message data);

        void clearInput();

    }

}
