package com.clpstudio.bsocial.rxbus;

import com.clpstudio.bsocial.data.models.conversations.ConversationViewModel;
import com.clpstudio.bsocial.data.models.conversations.MessageViewModel;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

@Singleton
public class RxBus {

    private final Relay<Object> bus = PublishRelay.create().toSerialized();

    private final Relay<Object> conversationBus = PublishRelay.create().toSerialized();

    private final Relay<Object> messageBus = PublishRelay.create().toSerialized();

    private Map<String, Relay<Object>> messagesBus = new HashMap<>();

    @Inject
    public RxBus() {

    }

    public void sendMessage(MessageViewModel model) {
        Relay<Object> relay = messagesBus.get(model.getConversationId());
        if (relay != null) {
            relay.accept(model);
        }
    }

    public boolean hasMessageObservers(String conversationId) {
        if (messagesBus.get(conversationId) == null) {
            return false;
        }
        return messagesBus.get(conversationId).hasObservers();
    }

    public Flowable<Object> messageAsFlowable(String conversationId) {
        messagesBus.putIfAbsent(conversationId, PublishRelay.create().toSerialized());
        return messagesBus.get(conversationId).toFlowable(BackpressureStrategy.LATEST);
    }

    public void sendConversation(ConversationViewModel model) {
        conversationBus.accept(model);
    }

    public boolean hasConversationObservers() {
        return conversationBus.hasObservers();
    }

    public Flowable<Object> conversationAsFlowable() {
        return conversationBus.toFlowable(BackpressureStrategy.LATEST);
    }

    public void send(Object o) {
        bus.accept(o);
    }

    public Flowable<Object> asFlowable() {
        return bus.toFlowable(BackpressureStrategy.LATEST);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
