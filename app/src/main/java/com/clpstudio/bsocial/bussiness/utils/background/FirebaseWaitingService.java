package com.clpstudio.bsocial.bussiness.utils.background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.clpstudio.bsocial.core.rxbus.RxBus;
import com.clpstudio.bsocial.data.models.Mapper;
import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.conversation.details.ConversationDetailActivity;
import com.clpstudio.bsocial.presentation.notifications.NotificationController;
import com.clpstudio.domain.usecases.ConversationUseCases;
import com.clpstudio.domain.usecases.MessageUseCases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class FirebaseWaitingService extends Service {

    public static String BUNDLE_KEY_CONVERSATION = "conversation_id";

    @Inject
    MessageUseCases messageUseCases;
    @Inject
    ConversationUseCases conversationUseCases;
    @Inject
    RxBus rxBus;
    @Inject
    NotificationController notificationController;

    private List<ConversationViewModel> conversations = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Map<String, Disposable> disposableMap = new HashMap<>();

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
                    }
                });
    }

    private void registerMessageListener(String id) {
        if (disposableMap.containsKey(id)) {
            return;
        }
        Log.d("rxbus", "Registered message bus for conversation id = " + id);
        Disposable disposable = messageUseCases.subscribeMessageAdded(id)
                .map(Mapper::toMessageViewModel)
                .subscribe(message -> {
                    if (rxBus.hasMessageObservers(message.getConversationId())) {
                        rxBus.sendMessage(message);
                    } else {
                        messageNotification(message);
                        Log.d("rxbus", "NOTIFICATION");
                    }
                });
        disposableMap.put(id, disposable);
    }

    private void messageNotification(MessageViewModel messageViewModel) {
        conversationUseCases.getConversationDetails(messageViewModel.getConversationId())
                .map(Mapper::toConversationViewModel)
                .subscribe(dbConversationModel -> {
                    notificationController
                            .showGeneralNotification(
                                    ConversationDetailActivity.getStartIntent(FirebaseWaitingService.this, dbConversationModel),
                                    "New message", messageViewModel.getMessage(), ""
                            );
                });

    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        disposableMap.values().forEach(Disposable::dispose);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String conversationId = intent.getStringExtra(BUNDLE_KEY_CONVERSATION);
        if (!TextUtils.isEmpty(conversationId)) {
            registerMessageListener(conversationId);
        }
        return super.onStartCommand(intent, flags, startId);

    }
}
