package com.clpstudio.bsocial.presentation.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.LoginModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements LoginPresenter.View{

    @BindView(R.id.username)
    EditText usernameEditText;
    @BindView(R.id.password)
    EditText passwordEditText;

    @Inject
    LoginPresenter presenter;

    @OnClick(R.id.loginButton)
    public void onLoginClick() {
        LoginModel loginModel = new LoginModel(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
        presenter.login(loginModel);
    }

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);
        presenter.bindView(this);
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void goToMainActivity() {

    }

    @Override
    public void showValidationError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
