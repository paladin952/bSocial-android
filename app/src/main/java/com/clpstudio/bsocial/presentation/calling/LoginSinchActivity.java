package com.clpstudio.bsocial.presentation.calling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.sinch.SinchService;
import com.clpstudio.bsocial.presentation.c.Henson;
import com.clpstudio.bsocial.presentation.conversation.main.ConversationsActivity;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.sinch.android.rtc.SinchError;

public class LoginSinchActivity extends BaseSinchActivity implements SinchService.StartFailedListener {

    @InjectExtra
    String userEmail;

    public static void startActivity(Activity activity, String userEmail) {
        Intent intent = Henson.with(activity)
                .gotoLoginSinchActivity()
                .userEmail(userEmail)
                .build();
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sinch);
        Dart.inject(this);

    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        login();
    }

    @Override
    public void onStartFailed(SinchError error) {
        openMainActivity();
    }

    @Override
    public void onStarted() {
        openMainActivity();
    }

    private void login() {
        if (TextUtils.isEmpty(userEmail)) {
            openMainActivity();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userEmail);
        } else {
            openMainActivity();
        }
    }

    private void openMainActivity() {
        ConversationsActivity.startActivity(this);
        finishAffinity();
    }

}
