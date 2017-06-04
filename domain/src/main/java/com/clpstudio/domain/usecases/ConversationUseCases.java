package com.clpstudio.domain.usecases;

import com.clpstudio.database.models.DbConversationModel;
import com.clpstudio.database.models.DbRegisteredUserModel;
import com.clpstudio.database.services.ConversationService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 04/06/2017.
 */

public class ConversationUseCases {

    @Inject
    ConversationService conversationService;

    @Inject
    public ConversationUseCases() {
    }

    public Observable<Boolean> subscribeConversationAdded() {
        return conversationService
                .subscribeConversationAdded();
    }

    public Single<String> createConversation(DbRegisteredUserModel friend) {
        return conversationService.createConversation(friend);
    }

    public Single<List<DbConversationModel>> getListOfConversations() {
        return conversationService.getListOfConversations();
    }

}
