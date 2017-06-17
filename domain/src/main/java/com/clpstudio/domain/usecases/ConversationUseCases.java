package com.clpstudio.domain.usecases;

import com.clpstudio.database.models.DbConversationModel;
import com.clpstudio.database.models.DbRegisteredUserModel;
import com.clpstudio.database.services.ConversationService;
import com.clpstudio.database.services.UserService;

import java.util.ArrayList;
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
    UserService userService;

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

    public Observable<List<DbConversationModel>> getListOfConversations() {
        return conversationService.getListOfConversations()
                .toObservable()
                .flatMap(dbConversationModels -> {
                    List<Observable<DbRegisteredUserModel>> userStreams = new ArrayList<>();
                    for (DbConversationModel model : dbConversationModels) {
                        for (String id : model.getMembersIds()) {
                            userStreams.add(userService.getUserDetails(id));
                        }
                    }

                    return Observable
                            .fromIterable(userStreams)
                            .flatMap(dbRegisteredUserModelObservable -> dbRegisteredUserModelObservable)
                            .toList()
                            .map(dbRegisteredUserModels -> {
                                for (DbRegisteredUserModel model : dbRegisteredUserModels) {
                                    for (DbConversationModel conversationModel : dbConversationModels) {
                                        if (conversationModel.getMembersIds().indexOf(model.getUserId()) != -1) {
                                            conversationModel.addUser(model);
                                        }
                                    }
                                }

                                return dbConversationModels;
                            })
                            .toObservable();
                });
    }

}
