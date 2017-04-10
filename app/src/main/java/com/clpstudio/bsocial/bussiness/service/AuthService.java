package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.bussiness.api.ApiService;
import com.clpstudio.bsocial.data.models.LoginModel;

import javax.inject.Inject;

public class AuthService {

    @Inject
    ApiService apiService;

    @Inject
    public AuthService() {
    }

    public void login(LoginModel loginModel) {
        apiService.authenticatePassword(loginModel);
    }
}
