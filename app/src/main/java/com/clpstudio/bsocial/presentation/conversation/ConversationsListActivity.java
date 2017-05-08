package com.clpstudio.bsocial.presentation.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.presentation.calling.PlaceCallSinchActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by clapalucian on 5/8/17.
 */

public class ConversationsListActivity extends AppCompatActivity{

    @OnClick(R.id.call_button)
    public void callButtonClick() {
        PlaceCallSinchActivity.startActivity(this, "clapalucian10@gmail.com", false);
    }

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ConversationsListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        ButterKnife.bind(this);
    }

}
