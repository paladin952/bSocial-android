package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.data.models.LoginModel;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by clapalucian on 14/05/2017.
 */

public class LoginService {

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    DatabaseService databaseService;

    @Inject
    public LoginService() {
    }

    public Completable login(LoginModel model) {
        return Completable.create(e -> firebaseAuth.signInWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        e.onComplete();
                    } else {
                        e.onError(task.getException());
                    }
                }));
    }

    public Completable register(String email, String password) {
        return Completable.create(e -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            e.onComplete();
                        } else {
                            e.onError(task.getException());
                        }
                    });
        }).andThen(databaseService.addUserInRegisteredList(email));
    }
}
