package com.clpstudio.bsocial.core.dagger;

import android.app.Application;
import android.content.Context;

import com.clpstudio.bsocial.core.persistance.ISharedPreferencesUtils;
import com.clpstudio.bsocial.core.persistance.SharedPreferencesUtils;
import com.clpstudio.bsocial.data.Session;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    Application application;
    RefWatcher refWatcher;

    public ApplicationModule(Application application) {
        this.application = application;
        refWatcher = LeakCanary.install(application);
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    BSocialApplication providesDealdashApplication() {
        return (BSocialApplication) application;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    ISharedPreferencesUtils providesSharedPreferences(Context context, Session session) {
        return new SharedPreferencesUtils(context, session);
    }


    @Provides
    @Singleton
    RefWatcher provideRefWatcher() {
        return refWatcher;
    }

}
