package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    @FirebaseModule.Friends
    DatabaseReference friendsRef;
    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    public DatabaseService() {
    }

    public Completable addUserInRegisteredList(String email) {
        return Completable.create(e -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                registeredUsersRef.child(userId).setValue(new RegisteredUser(userId, email), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        e.onError(databaseError.toException());
                    } else {
                        e.onComplete();
                    }
                });
            } else {
                e.onError(new RuntimeException("UserId missing"));
            }
        });
    }

    public Single<RegisteredUser> hasUser(String userEmail) {
        return Single.create(e -> registeredUsersRef.orderByChild(DB_EMAIL_FIELD).equalTo(userEmail).addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RegisteredUser user = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user = ds.getValue(RegisteredUser.class);
                }

                if (user != null) {
                    e.onSuccess(user);
                } else {
                    e.onError(new RuntimeException("User not found"));
                }
            }
        }));
    }

    public Completable addFriendToUsersList(String email) {
        return hasUser(email)
                .flatMapCompletable(this::addFriendToDatabaseUserList);
    }

    private Completable addFriendToDatabaseUserList(RegisteredUser registeredUser) {
        return Completable.create(e -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                friendsRef.child(user.getUid()).push().setValue(registeredUser);
                e.onComplete();
            } else {
                e.onError(new RuntimeException("UserId missing"));
            }
        });
    }
}
