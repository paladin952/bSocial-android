package com.clpstudio.bsocial.presentation;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.clpstudio.bsocial.core.dagger.ApplicationModule;
import com.clpstudio.bsocial.core.dagger.DIComponent;
import com.clpstudio.bsocial.core.dagger.DaggerDIComponent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class BSocialApplication extends Application {

    private DIComponent diComponent;

    public DIComponent getDiComponent() {
        return diComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this); // keep it before dagger initialization
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        diComponent = DaggerDIComponent.builder().applicationModule(new ApplicationModule(this)).build();
        diComponent.inject(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}

