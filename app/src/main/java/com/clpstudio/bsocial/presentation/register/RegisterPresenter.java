package com.clpstudio.bsocial.presentation.register;

import android.content.Context;
import android.util.Log;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.RegisterModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;

import javax.inject.Inject;

public class RegisterPresenter extends BaseMvpPresenter<RegisterPresenter.View> {

    @Inject
    Context context;

    @Inject
    public RegisterPresenter() {
    }

    public void register(String username, String password, String retryPassword) {

        if (Validator.isEmpty(username)) {
            view().showValidationError(context.getString(R.string.validation_empty_username));
        } else if (Validator.isEmpty(password) || Validator.isEmpty(retryPassword)) {
            view().showValidationError(context.getString(R.string.validation_empty_password));
        } else if (!password.equals(retryPassword)) {
            view().showValidationError(context.getString(R.string.validation_password_mismatch));
        } else {
            //TODO the call
            RegisterModel registerModel = new RegisterModel(username, password);
            Log.d("luci", registerModel.toString());
        }
    }


    public interface View extends IBaseMvpPresenter.View {

        void showRegisterError();

        void showValidationError(String error);

    }

}
