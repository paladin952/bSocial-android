package com.clpstudio.bsocial.presentation.conversations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.presentation.BSocialApplication;

import javax.inject.Inject;

/**
 * Created by clapalucian on 5/4/17.
 */

public class ConversationsActivity extends AppCompatActivity implements ConversationsPresenter.View{

    @Inject
    ConversationsPresenter presenter;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ConversationsActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);

        presenter.bindView(this);
    }
}
