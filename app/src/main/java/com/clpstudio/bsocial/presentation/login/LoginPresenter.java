package com.clpstudio.bsocial.presentation.login;

import android.content.Context;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.clpstudio.domain.usecases.LoginUseCases;

import javax.inject.Inject;

public class LoginPresenter extends BaseMvpPresenter<LoginPresenter.View> {

    @Inject
    Context context;

    @Inject
    LoginUseCases loginUseCases;

    @Inject
    public LoginPresenter() {
    }

    public void login(String email, String password) {
        if (Validator.isEmpty(email)) {
            view().showValidationError(context.getString(R.string.validation_empty_username));
        } else if (Validator.isEmpty(password)) {
            view().showValidationError(context.getString(R.string.validation_empty_password));
        } else {
            view().showProgress();
            loginUseCases.login(email, password)
                    .subscribe(() -> {
                        view().hideProgress();
                        view().gotoSinchLoginActivity(email);
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
