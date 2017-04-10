package com.clpstudio.bsocial.core.dagger;

import android.content.res.Resources;

import com.clpstudio.bsocial.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class})
public class HttpModule {
    @Provides
    @Named("backend_hostname")
    String provideBackendHostname(Resources resources) {
        return resources.getString(R.string.backend_hostname);
    }
}

