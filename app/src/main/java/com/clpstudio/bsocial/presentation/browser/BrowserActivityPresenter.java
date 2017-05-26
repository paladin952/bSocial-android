package com.clpstudio.bsocial.presentation.browser;

import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;

import javax.inject.Inject;

/**
 * Created by clapalucian on 27/05/2017.
 */

public class BrowserActivityPresenter extends BaseMvpPresenter<BrowserActivityPresenter.View> {

    private static final String DEFAULT_PROTOCOL_S = "https://";
    private static final String DEFAULT_PROTOCOL = "http://";

    @Inject
    public BrowserActivityPresenter() {
    }

    public void load(String url) {
        if (!url.startsWith(DEFAULT_PROTOCOL_S) && !url.startsWith(DEFAULT_PROTOCOL)) {
            url = DEFAULT_PROTOCOL_S +url;
        }
        view().showPage(url);
    }

    public interface View extends IBaseMvpPresenter.View {
        void showPage(String url);
    }

}
