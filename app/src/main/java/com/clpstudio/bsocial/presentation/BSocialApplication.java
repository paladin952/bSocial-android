package com.clpstudio.bsocial.presentation;

import android.app.Application;

import com.clpstudio.bsocial.core.dagger.ApplicationModule;
import com.clpstudio.bsocial.core.dagger.DIComponent;
import com.clpstudio.bsocial.core.dagger.DaggerDIComponent;
import com.google.firebase.FirebaseApp;

public class BSocialApplication extends Application {

    private DIComponent diComponent;

    public DIComponent getDiComponent() {
        return diComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this); // keep it before dagger initialization
        diComponent = DaggerDIComponent.builder().applicationModule(new ApplicationModule(this)).build();
        diComponent.inject(this);

    }
}

