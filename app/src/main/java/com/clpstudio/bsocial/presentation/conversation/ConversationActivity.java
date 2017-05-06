package com.clpstudio.bsocial.presentation.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationActivity extends AppCompatActivity implements ConversationPresenter.View {

    @Inject
    ConversationPresenter presenter;

    @BindView(R.id.conversationsRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.background)
    ImageView background;

    private ConversationAdapter adapter;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ConversationActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);
        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.bg_default_conversation).into(background);

        adapter = new ConversationAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        presenter.bindView(this);

//        ProfilePageActivity.startActivity(this);
//        GifTestActivity.startActivity(this);
    }

    @Override
    public void showData(List<ConversationModel> data) {
        adapter.addAll(data);
    }
}
