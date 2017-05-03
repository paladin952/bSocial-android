package com.clpstudio.bsocial.core.dagger;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by clapalucian on 5/3/17.
 */

@Module
public class FirebaseModule {

    @Provides
    @Singleton
    FirebaseAuth providesAuth(Context context) {
        return FirebaseAuth.getInstance();
    }

}
