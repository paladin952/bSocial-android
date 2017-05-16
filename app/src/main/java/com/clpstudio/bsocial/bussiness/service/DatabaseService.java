package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 14/05/2017.
 */

public class DatabaseService {

    private static final String DB_EMAIL_FIELD = "email";

    @Inject
    @FirebaseModule.RegisteredUsers
    DatabaseReference registeredUsersRef;

    @Inject
    public DatabaseService() {
    }

    public Completable addUserInRegisteredList(String email) {
        return Completable.create(e -> registeredUsersRef.push().setValue(new RegisteredUser(email), (databaseError, databaseReference) -> {
            if (databaseError != null) {
                e.onError(databaseError.toException());
            } else {
                e.onComplete();
            }
        }));
    }

    public Single<Boolean> hasUser(String userEmail) {
        return Single.create(e -> registeredUsersRef.orderByChild(DB_EMAIL_FIELD).equalTo(userEmail).addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RegisteredUser user = dataSnapshot.getValue(RegisteredUser.class);
                if (user != null) {
                    e.onSuccess(Boolean.TRUE);
                } else {
                    e.onSuccess(Boolean.FALSE);
                }
            }
        }));
    }
}
