package com.clpstudio.bsocial.presentation.register;

import android.content.Context;
import android.util.Log;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.RegisterModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class RegisterPresenter extends BaseMvpPresenter<RegisterPresenter.View> {

    @Inject
    Context context;

    @Inject
    FirebaseAuth firebaseAuth;

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
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        view().hideProgress();
                        if (task.isSuccessful()) {
                            view().gotoMainScreen();
                        } else {
                            if (task.getException() != null) {
                                view().showRegisterError(task.getException().getMessage());
                            } else {
                                view().showValidationError(context.getString(R.string.unknown_error));
                            }
                        }
                    });


            RegisterModel registerModel = new RegisterModel(email, password);
            Log.d("luci", registerModel.toString());
        }
    }


    public interface View extends IBaseMvpPresenter.View {

        void showRegisterError(String message);

        void showValidationError(String error);

        void gotoMainScreen();

        void showProgress();

        void hideProgress();

    }

}
