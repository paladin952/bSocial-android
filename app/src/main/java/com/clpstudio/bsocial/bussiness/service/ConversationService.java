package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.data.models.conversations.ConversationNameModel;
import com.clpstudio.bsocial.data.models.ui.FriendsListItemModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

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
            data.add(new ConversationModel("luci", "tu?"));
            data.add(new ConversationModel("ioana", "si eu"));
            data.add(new ConversationModel("luci", "ai mancat?"));
            data.add(new ConversationModel("ioana", "la livada si sunt smechera"));
            data.add(new ConversationModel("luci", "ai mancat?"));
            data.add(new ConversationModel("luci", "Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example."));
            data.add(new ConversationModel("luci", "Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example."));
            data.add(new ConversationModel("ioana", "lAndroid charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example. Android charting application xml ui design tutorial with example."));
            e.onNext(data);
        });
    }

    public Single<List<ConversationNameModel>> getListOfConversations() {
        return Single.create(e -> {
            List<ConversationNameModel> data = new ArrayList<>();
            data.add(new ConversationNameModel("Lucian Clapa", ""));
            data.add(new ConversationNameModel("Lucian", ""));
            data.add(new ConversationNameModel("Clapa", ""));
            data.add(new ConversationNameModel("Ioana minzat", ""));
            data.add(new ConversationNameModel("blabla", ""));
            e.onSuccess(data);
        });
    }

    public Single<List<FriendsListItemModel>> getFriends() {
        return Single.create(e -> {
            List<FriendsListItemModel> data = new ArrayList<>();
            data.add(new FriendsListItemModel("lucian@dealdash.com"));
            data.add(new FriendsListItemModel("luci1@dealdash.com"));
            data.add(new FriendsListItemModel("luci2@dealdash.com"));
            data.add(new FriendsListItemModel("luci3@dealdash.com"));
            data.add(new FriendsListItemModel("luci4@dealdash.com"));
            e.onSuccess(data);
        });
    }
}
