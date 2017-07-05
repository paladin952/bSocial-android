package com.clpstudio.bsocial.background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.clpstudio.bsocial.data.models.Mapper;
import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.rxbus.RxBus;
import com.clpstudio.domain.usecases.ConversationUseCases;
import com.clpstudio.domain.usecases.MessageUseCases;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class FirebaseWaitingService extends Service {

    @Inject
    MessageUseCases messageUseCases;
    @Inject
    ConversationUseCases conversationUseCases;
    @Inject
    RxBus rxBus;

    private List<ConversationViewModel> conversations = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);

        conversationUseCases
                .subscribeConversationAdded()
                .subscribe(conversation -> {
                    Log.d("rxbus", "Conversation has observables = " + rxBus.hasObservers());
                    if (rxBus.hasConversationObservers()) {
                        Log.d("rxbus", "Send New conversation!");
                        ConversationViewModel conversationViewModel = Mapper.toConversationViewModel(conversation);
                        rxBus.sendConversation(conversationViewModel);
                        conversations.add(conversationViewModel);
                        registerMessageListener(conversationViewModel.getId());
                    } else {
                        Log.d("rxbus", "Send Notification");
                    }
                });
    }

    private void registerMessageListener(String id) {
        Log.d("rxbus", "Registered message bus for conversation id = " + id);
        compositeDisposable.add(
                messageUseCases.subscribeMessageAdded(id)
                        .map(Mapper::toMessageViewModel)
                        .subscribe(message -> {
                            rxBus.sendMessage(message);
                            Log.d("luci", message.toString());
                        })
        );
    }


    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);

    }
}
