package com.clpstudio.bsocial.core.dagger;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;

import com.clpstudio.bsocial.presentation.BSocialApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
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
    Resources providesResources() {
        return application.getResources();
    }

    @Provides
    @Singleton
    Context providesContext() {
        return application.getApplicationContext();
    }

//    @Provides
//    @Singleton
//    ISharedPreferencesUtils providesSharedPreferences(Context context, Session session) {
//        return new SharedPreferencesUtils(context, session);
//    }



    @Provides
    @Singleton
    LocalBroadcastManager provideLocalBroadcastManager() {
        return LocalBroadcastManager.getInstance(application);
    }

    @Provides
    @Singleton
    WindowManager provideWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Provides
    @Singleton
    AlarmManager provideAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}
