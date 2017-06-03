package com.clpstudio.domain.usecases;

import com.clpstudio.database.models.DbMessageModel;
import com.clpstudio.database.services.MessagesService;
import com.clpstudio.domain.models.Mapper;
import com.clpstudio.domain.models.Message;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class MessageUseCases {

    @Inject
    MessagesService messagesService;

    @Inject
    public MessageUseCases() {

    }

    public Observable<List<Message>> getMessages(String conversationId) {
        return messagesService
                .getMessages(conversationId)
                .map(Mapper::toMessages)
                .subscribeOn(Schedulers.io());
    }

    public Completable sendMessage(String conversationId, DbMessageModel dbMessageModel) {
        return messagesService.
                sendMessage(conversationId, dbMessageModel)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Message> subscribeMessageAdded(String conversationId) {
        return messagesService
                .subscribeMessageAdded(conversationId)
                .map(Mapper::toMessage)
                .subscribeOn(Schedulers.io());
    }

}
