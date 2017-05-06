package com.clpstudio.bsocial.core.sinch;

import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by clapalucian on 5/7/17.
 */

@Singleton
public class SinchController {

    @Inject
    public SinchController() {

        SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId("current-user-id")
                .applicationKey("app-key")
                .applicationSecret("app-secret")
                .environmentHost("sandbox.sinch.com")
                .build();

    }


}
