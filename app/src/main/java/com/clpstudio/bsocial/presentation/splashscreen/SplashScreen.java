package com.clpstudio.bsocial.presentation.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.background.FirebaseWaitingService;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.authenticate.AuthenticateActivity;
import com.clpstudio.bsocial.presentation.calling.LoginSinchActivity;

import javax.inject.Inject;

/**
 * Created by clapalucian on 5/3/17.
 */

public class SplashScreen extends AppCompatActivity implements SplashScreenPresenter.View {

    @Inject
    SplashScreenPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);

        presenter.bindView(this);
        presenter.checkLoginStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
    }

    @Override
    public void gotoAuthenticateActivity() {
        AuthenticateActivity.startActivity(this);
        finishAffinity();
    }

    @Override
    public void gotoSinchLogin(String email) {
        LoginSinchActivity.startActivity(this, email);
        finishAffinity();
    }

    @Override
    public void startFirebaseServiceListener() {
        startService(new Intent(this, FirebaseWaitingService.class));
    }
}
