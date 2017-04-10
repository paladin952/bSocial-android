package com.clpstudio.bsocial.bussiness.api;

import com.clpstudio.bsocial.data.models.LoginModel;
import com.clpstudio.bsocial.data.models.UserAuthenticatedModel;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by clapalucian on 4/10/17.
 */

public interface ApiService {

    @POST("/api/v1/authenticate/password")
    Single<UserAuthenticatedModel> authenticatePassword(@Body LoginModel model);

}
