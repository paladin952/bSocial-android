package com.clpstudio.domain.usecases;

import com.clpstudio.database.models.DbRegisteredUserModel;
import com.clpstudio.database.services.UserService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 04/06/2017.
 */

public class UserUseCases {

    @Inject
    UserService userService;

    @Inject
    public UserUseCases() {
    }

    public Completable addUserInRegisteredList(String email) {
        return userService.addUserInRegisteredList(email);
    }

    public Single<DbRegisteredUserModel> hasUser(String userEmail) {
        return userService.hasUser(userEmail);
    }

    public Single<List<DbRegisteredUserModel>> getFriends() {
        return userService.getFriends();
    }

    public Completable addFriend(String email) {
        return userService.addFriend(email);
    }

}
