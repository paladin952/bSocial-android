package com.clpstudio.bsocial.presentation.login;

import android.content.Context;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.service.LoginService;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.LoginModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;

import javax.inject.Inject;

public class LoginPresenter extends BaseMvpPresenter<LoginPresenter.View> {

    @Inject
    Context context;

    @Inject
    LoginService loginService;

    @Inject
    public LoginPresenter() {
    }

    public void login(LoginModel model) {
        if (Validator.isEmpty(model.getEmail())) {
            view().showValidationError(context.getString(R.string.validation_empty_username));
        } else if (Validator.isEmpty(model.getPassword())) {
            view().showValidationError(context.getString(R.string.validation_empty_password));
        } else {
            view().showProgress();
            loginService.login(model)
                    .subscribe(() -> {
                        view().hideProgress();
                        view().gotoSinchLoginActivity(model.getEmail());
                    }, err -> {
                        view().hideProgress();
                        view().showLoginError(err.getMessage());
                    });
        }
    }


    public interface View extends IBaseMvpPresenter.View {

        void showLoginError(String error);

        void gotoSinchLoginActivity(String userEmail);

        void showValidationError(String error);

        void showProgress();

        void hideProgress();
    }

}
