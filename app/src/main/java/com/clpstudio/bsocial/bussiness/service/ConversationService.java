package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.data.models.conversations.ConversationModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationService {

    @Inject
    public ConversationService() {
    }

    public Observable<List<ConversationModel>> getMessages(String conversationId) {
        return Observable.create(e -> {
            List<ConversationModel> data = new ArrayList<ConversationModel>();
            data.add(new ConversationModel("luci", "ce faci?"));
            data.add(new ConversationModel("ioana", "bine"));
            data.add(new ConversationModel("luci", "tu"));
            data.add(new ConversationModel("ioana", "si eu"));
            data.add(new ConversationModel("luci", "ai mancat?"));
            data.add(new ConversationModel("ioana", "la livada"));
            e.onNext(data);
        });
    }
}
