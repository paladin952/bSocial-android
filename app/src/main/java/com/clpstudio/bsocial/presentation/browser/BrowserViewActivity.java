package com.clpstudio.bsocial.presentation.browser;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.clpstudio.bsocial.Henson;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clapalucian on 27/05/2017.
 */
public class BrowserViewActivity extends AppCompatActivity implements BrowserActivityPresenter.View {

    private static final String TITLE = "bSocial Browser";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;

    @InjectExtra
    String url;

    @Inject
    BrowserActivityPresenter presenter;

    public static void startActivity(Activity activity, String url) {
        Intent intent = Henson.with(activity)
                .gotoBrowserViewActivity()
                .url(url)
                .build();
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_view);
        Dart.inject(this);
        ButterKnife.bind(this);
        ((BSocialApplication) getApplicationContext()).getDiComponent().inject(this);
        setupToolbar();

        webView.setWebViewClient(new InAppWebViewClient());
        presenter.bindView(this);
        presenter.load(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(TITLE);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void showPage(String url) {
        webView.loadUrl(url);
    }
}
