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

    /**
     * The list of activities that are allowed to be shown even if the user is logged out
     *
     * @return The list of anonymous activities
     */
//    @Provides
//    @Singleton
//    @Named("anonymous_activities")
//    List<Class> providesAnonymousActivitiesList() {
//        List<Class> list = new ArrayList<>();
//        list.add(SplashScreen.class);
//        list.add(AuthenticateActivity.class);
//        list.add(LogoutActivity.class);
//        list.add(LoginActivity.class);
//        list.add(ForgotPasswordActivity.class);
//        list.add(SignupActivity.class);
//        list.add(LearnActivity.class);
//        list.add(FacebookActivity.class);
//        list.add(MaintenanceModeActivity.class);
//        return list;
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
