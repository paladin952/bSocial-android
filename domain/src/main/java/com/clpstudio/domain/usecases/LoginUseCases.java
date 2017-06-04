package com.clpstudio.domain.usecases;

import com.clpstudio.database.services.LoginService;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by clapalucian on 04/06/2017.
 */

public class LoginUseCases {

    @Inject
    LoginService loginService;

    @Inject
    public LoginUseCases() {
    }

    public Completable login(String email, String password) {
        return loginService.login(email, password);
    }

    public Completable register(String email, String password) {
        return loginService.register(email, password);
    }

}
