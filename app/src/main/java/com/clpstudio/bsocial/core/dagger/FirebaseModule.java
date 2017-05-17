package com.clpstudio.bsocial.core.dagger;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
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

    @Provides
    @Singleton
    FirebaseStorage providesImageStorage(Context context) {
        return FirebaseStorage.getInstance();
    }

    @Provides
    @Singleton
    FirebaseDatabase providesDatabase(){
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @RegisteredUsers
    DatabaseReference providesRegisteredUsersRef(FirebaseDatabase database) {
        return database.getReference("registeredUsers");
    }

    @Provides
    @Friends
    DatabaseReference providesFriendsUsersRef(FirebaseDatabase database) {
        return database.getReference("friends");
    }

    @Provides
    @Messages
    DatabaseReference providesMessagesRef(FirebaseDatabase database) {
        return database.getReference("messages");
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RegisteredUsers {
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Friends {
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Messages {
    }

}
