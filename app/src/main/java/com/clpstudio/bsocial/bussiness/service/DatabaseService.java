package com.clpstudio.bsocial.bussiness.service;

import android.util.Log;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by clapalucian on 14/05/2017.
 */

public class DatabaseService {

    @Inject
    @FirebaseModule.RegisteredUsers
    DatabaseReference registeredUsersRef;

    @Inject
    public DatabaseService() {
    }

    public Completable addUserInRegisteredList(String email) {
        return Completable.create(e -> registeredUsersRef.child(email.replace(".", "@")).setValue("", (databaseError, databaseReference) -> {
            if (databaseError != null) {
                e.onError(databaseError.toException());
            } else {
                e.onComplete();
            }
        }));
    }

    public void getData() {
        registeredUsersRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("luci", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("luci", "canceled");
            }
        });
    }
}
