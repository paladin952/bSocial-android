package com.clpstudio.bsocial.presentation.login;

import android.content.Context;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.Validator;
import com.clpstudio.bsocial.data.models.LoginModel;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class LoginPresenter extends BaseMvpPresenter<LoginPresenter.View> {

    @Inject
    Context context;

    @Inject
    FirebaseAuth firebaseAuth;

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
            firebaseAuth.signInWithEmailAndPassword(model.getEmail(), model.getPassword())
                    .addOnCompleteListener(task -> {
                        view().hideProgress();
                        if (task.isSuccessful()) {
                            view().gotoSinchLoginActivity(model.getEmail());
                        } else {
                            if (task.getException() != null) {
                                view().showLoginError(task.getException().getMessage());
                            } else {
                                view().showValidationError(context.getString(R.string.unknown_error));
                            }
                        }
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
