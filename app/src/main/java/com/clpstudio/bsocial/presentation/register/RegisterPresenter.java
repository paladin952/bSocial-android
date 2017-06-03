package com.clpstudio.bsocial.presentation.register;

import android.content.Context;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.clpstudio.database.services.LoginService;

import javax.inject.Inject;

public class RegisterPresenter extends BaseMvpPresenter<RegisterPresenter.View> {

    @Inject
    Context context;

    @Inject
    LoginService loginService;

    @Inject
    public RegisterPresenter() {
    }

    public void register(String email, String password, String retryPassword) {
        if (Validator.isEmpty(email)) {
            view().showValidationError(context.getString(R.string.validation_empty_username));
        } else if (Validator.isEmpty(password) || Validator.isEmpty(retryPassword)) {
            view().showValidationError(context.getString(R.string.validation_empty_password));
        } else if (!password.equals(retryPassword)) {
            view().showValidationError(context.getString(R.string.validation_password_mismatch));
        } else {
            //TODO the call
            view().showProgress();
            loginService.register(email, password)
                    .subscribe(() -> {
                        view().hideProgress();
                        view().gotoSinchLoginActivity(email);
                    }, err -> {
                        view().hideProgress();
                        view().showRegisterError(err.getMessage());
                    });


        }
    }


    public interface View extends IBaseMvpPresenter.View {

        void showRegisterError(String message);

        void showValidationError(String error);

        void gotoSinchLoginActivity(String userEmail);

        void showProgress();

        void hideProgress();

    }

}
