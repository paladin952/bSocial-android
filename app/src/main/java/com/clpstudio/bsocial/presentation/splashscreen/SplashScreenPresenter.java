package com.clpstudio.bsocial.presentation.splashscreen;

import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

/**
 * Created by clapalucian on 5/3/17.
 */

public class SplashScreenPresenter extends BaseMvpPresenter<SplashScreenPresenter.View> {

    @Inject FirebaseAuth firebaseAuth;

    @Inject
    public SplashScreenPresenter(){

    }

    public void checkLoginStatus() {
        if (firebaseAuth.getCurrentUser() != null) {
            view().gotoSinchLogin(firebaseAuth.getCurrentUser().getEmail());
        } else {
            view().gotoAuthenticateActivity();
        }
    }

    public interface View extends IBaseMvpPresenter.View {
        void gotoAuthenticateActivity();

        void gotoSinchLogin(String userEmail);
    }

}
