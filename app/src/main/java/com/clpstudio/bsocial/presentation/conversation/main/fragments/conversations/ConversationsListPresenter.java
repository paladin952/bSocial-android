package com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations;

import android.support.annotation.NonNull;
import android.util.Log;

import com.clpstudio.bsocial.data.models.Mapper;
import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IProgressView;
import com.clpstudio.bsocial.core.rxbus.RxBus;
import com.clpstudio.domain.usecases.ConversationUseCases;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
    @Inject
    RxBus rxBus;

    private CompositeDisposable compositeDisposable;

    @Inject
    public ConversationsListPresenter() {
    }

    @Override
    public void bindView(@NonNull View view) {
        super.bindView(view);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                rxBus.conversationAsFlowable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if (o instanceof ConversationViewModel) {
                                ConversationViewModel model = (ConversationViewModel) o;
                                view().appendData(model);
                                Log.d("luci", "Append 2");
                            }
                        })
        );
    }

    @Override
    public void unbindView() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.unbindView();
    }

    private void showData(boolean progress) {
        if (progress) {
            view().showProgress();
            Log.d("bSocial", "Refresh conversation list, with progress!");
        } else {
            Log.d("bSocial", "Refresh conversation list, no progress!");
        }
        view().clearData();
        Disposable disposable = conversationUseCases.getListOfConversations()
                .map(Mapper::toConversationViewModel)
                .subscribe(conversationNameModel -> {
                    view().hideProgress();
                    view().appendData(conversationNameModel);
                    Log.d("luci", "append");
                }, err -> {
                    view().hideProgress();
                    //todo
                });
        compositeDisposable.add(disposable);
    }

    public void subscribeConversationAdded() {
        Disposable disposable = conversationUseCases.subscribeConversationAdded()
                .subscribe(message -> {
                    view().clearData();
                    showData(false);
                });
        compositeDisposable.add(disposable);
    }

    public void onStart() {
        showData(true);
    }


    public interface View extends IProgressView {

        void appendData(ConversationViewModel model);

        void clearData();

    }
}
