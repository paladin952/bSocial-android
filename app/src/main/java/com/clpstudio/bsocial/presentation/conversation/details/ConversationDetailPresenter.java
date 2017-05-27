package com.clpstudio.bsocial.presentation.conversation.details;

import android.support.annotation.NonNull;

import com.clpstudio.bsocial.bussiness.service.ConversationService;
import com.clpstudio.bsocial.bussiness.service.MessagesService;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.conversations.Message;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by clapalucian on 5/4/17.
 */

public class ConversationDetailPresenter extends BaseMvpPresenter<ConversationDetailPresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    ConversationService conversationService;
    @Inject
    MessagesService messagesService;

    private String conversationId;
    private CompositeDisposable compositeDisposable;

    @Inject
    public ConversationDetailPresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void unbindView() {
        super.unbindView();
        compositeDisposable.dispose();
    }

    private void subscribeMessageAdded() {
        Disposable disposable = messagesService.subscribeMessageAdded(conversationId)
                .subscribe(message -> {
                    view().appendData(message);
                    view().clearInput();
                }, err -> {
                    //TODO
                });
        compositeDisposable.add(disposable);
    }

    public void setConversationId(String id) {
        this.conversationId = id;
    }

    public void getMessages() {
        Disposable disposable = messagesService.getMessages(conversationId).subscribe(messages -> {
            view().showData(messages);
        });
        compositeDisposable.add(disposable);
    }

    public void onTextSubmited(String text) {
        int type = Validator.isUrl(text) ? Message.TYPE_LINK : Message.TYPE_MESSAGE;
        sendMessage(text, type);
    }

    public void onGifSelected(String url) {
        sendMessage(url, Message.TYPE_GIF);
    }

    private void sendMessage(String text, int type) {
        String avatarUrl = firebaseAuth.getCurrentUser().getPhotoUrl() != null ? firebaseAuth.getCurrentUser().getPhotoUrl().toString() : "";

        Message message = new Message(firebaseAuth.getCurrentUser().getEmail(),
                text, System.currentTimeMillis(), avatarUrl, type);
        Disposable disposable = messagesService.sendMessage(conversationId, message).subscribe();
        compositeDisposable.add(disposable);
    }

    public void bindToOldConversation() {
        subscribeMessageAdded();
    }

    public void bindToNewConversation(RegisteredUser friend) {
        Disposable disposable = conversationService.createConversation(friend)
                .subscribe(s -> {
                    conversationId = s;
                    subscribeMessageAdded();
                });
        compositeDisposable.add(disposable);
    }

    public void onAvatarImageClick() {

    }

    public interface View extends IBaseMvpPresenter.View {

        void showData(List<Message> data);

        void appendData(Message data);

        void clearInput();

    }

}
