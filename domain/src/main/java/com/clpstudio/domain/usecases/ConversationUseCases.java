package com.clpstudio.domain.usecases;

import com.clpstudio.database.models.DbConversationModel;
import com.clpstudio.database.models.DbRegisteredUserModel;
import com.clpstudio.database.services.ConversationService;
import com.clpstudio.database.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

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

    public Observable<DbConversationModel> subscribeConversationAdded() {
        return conversationService
                .subscribeConversationAdded()
                .delay(1, TimeUnit.SECONDS)
                .flatMap(dbConversationModel -> conversationService.getConversationDetails(dbConversationModel.getId()))
                .flatMap(dbConversationModel -> {
                    List<Observable<DbRegisteredUserModel>> userStreams = new ArrayList<>();
                    for (String id : dbConversationModel.getMembersIds()) {
                        userStreams.add(userService.getUserDetails(id));
                    }

                    return Observable
                            .fromIterable(userStreams)
                            .flatMap(dbRegisteredUserModelObservable -> dbRegisteredUserModelObservable)
                            .toList()
                            .map(dbRegisteredUserModels -> {
                                for (DbRegisteredUserModel model : dbRegisteredUserModels) {
                                    if (dbConversationModel.getMembersIds().indexOf(model.getUserId()) != -1) {
                                        dbConversationModel.addUser(model);
                                    }
                                }

                                return dbConversationModel;
                            })
                            .toObservable();
                });
    }

    public Observable<DbConversationModel> createConversation(DbRegisteredUserModel friend) {
        return conversationService.createConversation(friend)
                .toObservable()
                .flatMap(s -> conversationService.getConversationDetails(s))
                .flatMap(dbConversationModel -> {
                    List<Observable<DbRegisteredUserModel>> userStreams = new ArrayList<>();
                    for (String id : dbConversationModel.getMembersIds()) {
                        userStreams.add(userService.getUserDetails(id));
                    }

                    return Observable
                            .fromIterable(userStreams)
                            .flatMap(dbRegisteredUserModelObservable -> dbRegisteredUserModelObservable)
                            .toList()
                            .map(dbRegisteredUserModels -> {
                                for (DbRegisteredUserModel model : dbRegisteredUserModels) {
                                    if (dbConversationModel.getMembersIds().indexOf(model.getUserId()) != -1) {
                                        dbConversationModel.addUser(model);
                                    }
                                }

                                return dbConversationModel;
                            })
                            .toObservable();
                });
    }

    public Observable<DbConversationModel> getListOfConversations() {
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
                }).flatMap(Observable::fromIterable);
    }

    public Observable<DbConversationModel> getConversationDetails(String id) {
        return conversationService.getConversationDetails(id);
    }

}
