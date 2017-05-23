package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 14/05/2017.
 */

public class DatabaseService {

    private static final String DB_EMAIL_FIELD = "email";
    private static final String DB_USERID_FIELD = "userId";

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
                String photo = user.getPhotoUrl() != null ? user.getPhotoUrl().toString(): "";
                registeredUsersRef.child(userId).setValue(new RegisteredUser(userId, email, photo), (databaseError, databaseReference) -> {
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

    public Single<List<RegisteredUser>> getFriends() {
        return Single.create(e -> friendsRef.child(firebaseAuth.getCurrentUser().getUid()).orderByChild(DB_USERID_FIELD).addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<RegisteredUser> data = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RegisteredUser user = ds.getValue(RegisteredUser.class);
                    data.add(user);
                }

                e.onSuccess(data);
            }
        }));
    }

    private Completable addFriendToDatabaseUserList(RegisteredUser registeredUser) {
        return Completable.create(e -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                String photo = user.getPhotoUrl() != null ? user.getPhotoUrl().toString(): "";
                friendsRef.child(user.getUid()).push().setValue(registeredUser);
                friendsRef.child(registeredUser.getUserId()).push().setValue(new RegisteredUser(user.getUid(), user.getEmail(), photo));
                e.onComplete();
            } else {
                e.onError(new RuntimeException("UserId missing"));
            }
        });
    }

}
