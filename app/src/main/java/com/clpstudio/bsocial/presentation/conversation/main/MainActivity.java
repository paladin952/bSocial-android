package com.clpstudio.bsocial.presentation.conversation.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.core.glide.GlideRoundedImageTarget;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.profile.ProfilePageActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.clpstudio.bsocial.R.id.avatar;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class MainActivity extends AppCompatActivity implements ConversationsActivityPresenter.View, GoToPageListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabLayout;

    @Inject
    ConversationsActivityPresenter presenter;

    private MainPagerAdapter adapter;
    private ImageView avatarImageView;
    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);

        setupViewPager();
        setupToolbar();
        presenter.bindView(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.showAvatar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        slidingTabLayout.removeOnTabSelectedListener(onTabSelectedListener);
        presenter.unbindView();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        avatarImageView = (ImageView) toolbar.findViewById(avatar);
        avatarImageView.setOnClickListener(v -> ProfilePageActivity.startActivity(this));
        slidingTabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    private void setupViewPager() {
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        slidingTabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void showAvatar(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new GlideRoundedImageTarget(avatarImageView));
    }

    @Override
    public void gotoPage(int num) {
        viewPager.setCurrentItem(num, true);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == MainPagerAdapter.FRIENDS_POSITION) {
            viewPager.setCurrentItem(MainPagerAdapter.CONVERSATIONS_POSITION);
        } else {
            super.onBackPressed();
        }
    }
}
