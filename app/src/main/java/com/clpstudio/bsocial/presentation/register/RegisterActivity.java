package com.clpstudio.bsocial.presentation.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.conversation.ConversationActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterPresenter.View {

    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.retryPassword)
    EditText retryPasswordEditText;
    @BindView(R.id.progressBar)
    View progressBar;

    @Inject
    RegisterPresenter registerPresenter;

    @OnClick(R.id.registerButton)
    public void onRegisterClick() {
        String username = emailEditText.getText().toString().trim();
        String passwowrd = passwordEditText.getText().toString().trim();
        String retryPasword = retryPasswordEditText.getText().toString().trim();
        registerPresenter.register(username, passwowrd, retryPasword);
    }

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);

        registerPresenter.bindView(this);
    }

    @Override
    public void showRegisterError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showValidationError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoMainScreen() {
        ConversationActivity.startActivity(this);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
