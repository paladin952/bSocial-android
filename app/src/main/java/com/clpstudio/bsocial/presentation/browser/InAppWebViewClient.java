package com.clpstudio.bsocial.presentation.browser;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by clapalucian on 27/05/2017.
 */

public class InAppWebViewClient  extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}