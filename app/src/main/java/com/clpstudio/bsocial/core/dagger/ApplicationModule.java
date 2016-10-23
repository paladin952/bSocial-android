package com.clpstudio.bsocial.core.dagger;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;

import com.clpstudio.bsocial.core.persistance.ISharedPreferencesUtils;
import com.clpstudio.bsocial.core.persistance.SharedPreferencesUtils;
import com.clpstudio.bsocial.data.Session;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.dealdash.auth.FacebookAuthenticator;
import com.dealdash.auth.Session;
import com.dealdash.persistance.ISharedPreferencesUtils;
import com.dealdash.persistance.SharedPreferencesUtils;
import com.dealdash.ui.RoutingActivity;
import com.dealdash.ui.auth.presentation.AuthenticateActivity;
import com.dealdash.ui.auth.presentation.ForgotPasswordActivity;
import com.dealdash.ui.auth.presentation.LoginActivity;
import com.dealdash.ui.auth.presentation.LogoutActivity;
import com.dealdash.ui.auth.presentation.SignupActivity;
import com.dealdash.ui.learn.LearnActivity;
import com.dealdash.ui.learn.LearnTimelineActivity;
import com.dealdash.ui.splashscreen.presentation.SplashScreen;
import com.dealdash.utils.alarm.AlarmManagerUtilImpl;
import com.dealdash.utils.alarm.IAlarmManagerUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
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
