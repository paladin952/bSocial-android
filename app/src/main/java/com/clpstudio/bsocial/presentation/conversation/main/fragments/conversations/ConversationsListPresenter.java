package com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations;

import android.support.annotation.NonNull;
import android.util.Log;

import com.clpstudio.bsocial.data.models.Mapper;
import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IProgressView;
import com.clpstudio.domain.usecases.ConversationUseCases;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by clapalucian on 5/10/17.
 */

public class ConversationsListPresenter extends BaseMvpPresenter<ConversationsListPresenter.View> {

    @Inject
    FirebaseAuth firebaseAuth;
    @Inject
    ConversationUseCases conversationUseCases;

    private CompositeDisposable compositeDisposable;

    @Inject
    public ConversationsListPresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        compositeDisposable = new CompositeDisposable();
        subscribeConversationAdded();
    }

    @Override
    public void unbindView() {
        super.unbindView();
        compositeDisposable.dispose();
    }

    private void showData(boolean progress) {
        if (progress) {
            view().showProgress();
            Log.d("bSocial", "Refresh conversation list, with progress!");
        } else {
            Log.d("bSocial", "Refresh conversation list, no progress!");
        }
        Disposable disposable = conversationUseCases.getListOfConversations()
                .map(Mapper::toConversationViewModels)
                .subscribe(conversationNameModels -> {
                    view().hideProgress();
                    view().showData(conversationNameModels);
                }, err -> {
                    view().hideProgress();
                    //todo
                });
        compositeDisposable.add(disposable);
    }

    public void subscribeConversationAdded() {
        Disposable disposable = conversationUseCases.subscribeConversationAdded()
                .subscribe(message -> {
                    showData(false);
                });
        compositeDisposable.add(disposable);
    }

    public void onStart() {
        showData(true);
    }


    public interface View extends IProgressView {

        void showData(List<ConversationViewModel> data);

    }
}
