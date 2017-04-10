package com.clpstudio.bsocial.presentation.authenticate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.presentation.login.LoginActivity;
import com.clpstudio.bsocial.presentation.register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthenticateActivity extends AppCompatActivity {

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AuthenticateActivity.class));
    }

    @OnClick(R.id.loginButton)
    public void onLoginClick() {
        LoginActivity.startActivity(this);
    }

    @OnClick(R.id.registerButton)
    public void onRegisterClick() {
        RegisterActivity.startActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        ButterKnife.bind(this);
    }
}
