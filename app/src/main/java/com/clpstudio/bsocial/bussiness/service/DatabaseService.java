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
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.clpstudio.bsocial.core.firebase.FirebaseConstants.DB_EMAIL_FIELD;
import static com.clpstudio.bsocial.core.firebase.FirebaseConstants.FIELD_FRIENDS;

/**
 * Created by clapalucian on 14/05/2017.
 */

public class DatabaseService {


    @Inject
    @FirebaseModule.Users
    DatabaseReference usersRef;
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
                String photo = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
                usersRef.child(userId).setValue(new RegisteredUser(userId, email, photo), (databaseError, databaseReference) -> {
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
        return Single.create(e -> usersRef.orderByChild(DB_EMAIL_FIELD).equalTo(userEmail).addValueEventListener(new AddValueEventSuccessListener() {
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

    public Single<List<RegisteredUser>> getFriends() {

        return getFriendIds()
                .toObservable()
                .map(strings -> {
                    List<Observable<RegisteredUser>> conversationsSingles = new ArrayList<>();
                    for (String id : strings) {
                        conversationsSingles.add(getUserDetails(id));
                    }
                    return conversationsSingles;
                })
                .flatMap(observables -> Observable.fromIterable(observables)
                        .flatMap(conversationModelObservable -> conversationModelObservable))
                .toList();
    }

    private Observable<RegisteredUser> getUserDetails(String userId) {
        return Observable.create(e -> usersRef.child(userId).addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RegisteredUser model = dataSnapshot.getValue(RegisteredUser.class);
                if (model != null) {
                    e.onNext(model);
                    e.onComplete();
                } else {
                    e.onComplete();
                }
            }
        }));
    }

    public Completable addFriend(String email) {
        return hasUser(email)
                .flatMapCompletable(this::addFriendToDatabaseUserList);
    }


    private Single<List<String>> getFriendIds() {
        return Single.create(e -> usersRef.child(firebaseAuth.getCurrentUser().getUid()).child(FIELD_FRIENDS).orderByKey().addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> data = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    data.add(ds.getKey());
                }

                e.onSuccess(data);
            }
        }));
    }

    private Completable addFriendToDatabaseUserList(RegisteredUser friendUser) {
        return Completable.create(e -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            usersRef
                    .child(currentUser.getUid())
                    .child(FIELD_FRIENDS)
                    .child(friendUser.getUserId())
                    .setValue(true);
            usersRef
                    .child(friendUser.getUserId())
                    .child(FIELD_FRIENDS)
                    .child(currentUser.getUid())
                    .setValue(true);
            e.onComplete();
        });
    }

}
