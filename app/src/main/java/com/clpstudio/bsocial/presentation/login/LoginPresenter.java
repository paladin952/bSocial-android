package com.clpstudio.bsocial.presentation.login;

import android.content.Context;
import android.util.Log;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.LoginModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;

import javax.inject.Inject;

public class LoginPresenter extends BaseMvpPresenter<LoginPresenter.View> {

    @Inject
    Context context;

    @Inject
    public LoginPresenter() {
    }

    public void login(LoginModel model) {
        if (Validator.isEmpty(model.getUsername())) {
            view().showValidationError(context.getString(R.string.validation_empty_username));
        } else if (Validator.isEmpty(model.getPassword())) {
            view().showValidationError(context.getString(R.string.validation_empty_password));
        } else {
            //TODO api call
            Log.d("luci", model.toString());
        }
    }


    public interface View extends IBaseMvpPresenter.View {

        void showLoginError();

        void goToMainActivity();

        void showValidationError(String error);
    }

}
