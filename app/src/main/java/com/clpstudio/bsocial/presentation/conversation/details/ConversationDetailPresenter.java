package com.clpstudio.bsocial.presentation.conversation.details;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.Mapper;
import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.clpstudio.bsocial.rxbus.RxBus;
import com.clpstudio.database.models.DbMessageModel;
import com.clpstudio.domain.usecases.ConversationUseCases;
import com.clpstudio.domain.usecases.FirebaseStorageUseCases;
import com.clpstudio.domain.usecases.MessageUseCases;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by clapalucian on 5/4/17.
 */

public class ConversationDetailPresenter extends BaseMvpPresenter<ConversationDetailPresenter.View> {

    @Inject
    Context context;
    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    ConversationUseCases conversationUseCases;
    @Inject
    FirebaseStorageUseCases storageUseCases;

    @Inject
    MessageUseCases messageUseCases;
    @Inject
    RxBus rxBus;

    private ConversationViewModel conversation;
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
        compositeDisposable.dispose();
        super.unbindView();
    }

//    private void subscribeMessageAdded() {
//        Disposable disposable = messageUseCases.subscribeMessageAdded(conversation.getId())
//                .map(Mapper::toMessageViewModel)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(message -> {
//                    view().appendData(message);
//                    view().clearInput();
//                }, err -> {
//                    //TODO
//                });
//        compositeDisposable.add(disposable);
//    }

    public void getMessages() {
        view().clearList();
        Disposable disposable = messageUseCases
                .getMessages(conversation.getId())
                .map(Mapper::toMessageViewModels)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    view().showData(messages);
                    registerEventBus();
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

        DbMessageModel dbMessageModelViewModel = new DbMessageModel(firebaseAuth.getCurrentUser().getEmail(),
                text, System.currentTimeMillis(), avatarUrl, type);

        Disposable disposable = messageUseCases
                .sendMessage(conversation.getId(), dbMessageModelViewModel).subscribe();
        compositeDisposable.add(disposable);
    }

    public void bindToOldConversation(ConversationViewModel conversation) {
        this.conversation = conversation;
        showAvatarFromConversation();
        getMessages();
//        subscribeMessageAdded();
    }

    private void showAvatarFromConversation() {
        for (RegisteredUserViewModel user : conversation.getUserViewModels()) {
            if (!user.getEmail().equals(firebaseAuth.getCurrentUser().getEmail())) {
                view().showAvatar(user.getImageUrl());
                view().setTitle(user.getEmail());
                break;
            }
        }

    }

    public void bindToNewConversation(RegisteredUserViewModel friend) {
        view().showAvatar(friend.getImageUrl());
        view().setTitle(friend.getEmail());

        Disposable disposable = conversationUseCases
                .createConversation(Mapper.toRegisteredUser(friend))
                .map(Mapper::toConversationViewModel)
                .subscribe(s -> {
                    conversation = s;
                    getMessages();
//                    subscribeMessageAdded();
                });
        compositeDisposable.add(disposable);
    }

    public void onAvatarImageClick() {

    }

    public void uploadImage(String filename, Uri path) {
        storageUseCases.uploadConversationDataAndGetLink(filename, path, conversation.getId())
                .subscribe(link -> {
                    sendMessage(link, MessageViewModel.TYPE_PHOTO);
                }, err -> {
                    view().showError(context.getString(R.string.unknown_error));
                });
    }

    public void callClicked() {
        for (RegisteredUserViewModel user : conversation.getUserViewModels()) {
            if (!user.getEmail().equals(firebaseAuth.getCurrentUser().getEmail())) {
                view().call(user.getEmail());
                break;
            }
        }
    }

    private void registerEventBus() {
        compositeDisposable.add(
                rxBus.messageAsFlowable(conversation.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof MessageViewModel) {
                                MessageViewModel model = (MessageViewModel) o;
                                Log.d("rxbus", "Message received = " + o.toString());
                                view().appendData(model);
                            }
                        })
        );
    }

    public interface View extends IBaseMvpPresenter.View {

        void showData(List<MessageViewModel> data);

        void appendData(MessageViewModel data);

        void clearInput();

        void showError(String error);

        void showAvatar(String url);

        void setTitle(String title);

        void call(String userEmail);

        void clearList();

    }

}
