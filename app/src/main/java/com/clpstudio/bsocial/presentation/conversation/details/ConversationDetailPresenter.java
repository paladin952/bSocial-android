package com.clpstudio.bsocial.presentation.conversation.details;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.Mapper;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.clpstudio.domainlib.models.Message;
import com.clpstudio.domainlib.services.ConversationService;
import com.clpstudio.domainlib.services.FirebaseStorageService;
import com.clpstudio.domainlib.services.MessagesService;
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
    @Inject
    FirebaseStorageService firebaseStorageService;

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
                .map(Mapper::toMessageViewModel)
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
        Disposable disposable = messagesService
                .getMessages(conversationId)
                .map(Mapper::toMessageViewModels)
                .subscribe(messages -> {
                    view().showData(messages);
                });
        compositeDisposable.add(disposable);
    }

    public void onTextSubmited(String text) {
        int type = Validator.isUrl(text) ? MessageViewModel.TYPE_LINK : MessageViewModel.TYPE_MESSAGE;
        sendMessage(text, type);
    }

    public void onGifSelected(String url) {
        sendMessage(url, MessageViewModel.TYPE_GIF);
    }

    private void sendMessage(String text, int type) {
        String avatarUrl = firebaseAuth.getCurrentUser().getPhotoUrl() != null ? firebaseAuth.getCurrentUser().getPhotoUrl().toString() : "";

        Message messageViewModel = new Message(firebaseAuth.getCurrentUser().getEmail(),
                text, System.currentTimeMillis(), avatarUrl, type);

        Disposable disposable = messagesService
                .sendMessage(conversationId, messageViewModel).subscribe();
        compositeDisposable.add(disposable);
    }

    public void bindToOldConversation() {
        subscribeMessageAdded();
    }

    public void bindToNewConversation(RegisteredUserViewModel friend) {
        Disposable disposable = conversationService
                .createConversation(Mapper.toRegisteredUser(friend))
                .subscribe(s -> {
                    conversationId = s;
                    subscribeMessageAdded();
                });
        compositeDisposable.add(disposable);
    }

    public void onAvatarImageClick() {

    }

    public void uploadImage(String filename, Uri path) {
        firebaseStorageService.uploadConversationDataAndGetLink(filename, path, conversationId)
                .subscribe(link -> {
                    sendMessage(link, MessageViewModel.TYPE_PHOTO);
                }, err -> {
                    //TODO
                });
    }

    public interface View extends IBaseMvpPresenter.View {

        void showData(List<MessageViewModel> data);

        void appendData(MessageViewModel data);

        void clearInput();

    }

}
