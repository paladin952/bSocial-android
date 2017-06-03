package com.clpstudio.domainlib.services;

import com.clpstudio.domainlib.dagger.FirebaseModule;
import com.clpstudio.domainlib.utils.FirebaseStorageContants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by clapalucian on 5/4/17.
 */

public class ProfileService {

    @Inject
    FirebaseStorage firebaseStorage;
    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    @FirebaseModule.Users
    DatabaseReference registeredUserRef;

    @Inject
    public ProfileService() {
    }

    public Completable updateNickname(String nickname) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            return Completable.create(e -> {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                e.onComplete();
                            } else {
                                e.onError(task.getException());
                            }
                        });
            });
        } else {
            return Completable.error(new RuntimeException("User logged out"));
        }
    }

    public Completable upload(File file) {
        return firebaseUpload(file)
                .andThen(updateProfileImageUrl());
    }

    private Completable firebaseUpload(File file) {
        return Completable.create(e -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                String email = user.getEmail();

                StorageReference reference = firebaseStorage.getReference();
                StorageReference imageFileRef = reference.child(FirebaseStorageContants.PROFILE_IMAGES_PATH + email);
                try {
                    InputStream stream = new FileInputStream(file);
                    UploadTask uploadTask = imageFileRef.putStream(stream);
                    uploadTask
                            .addOnFailureListener(e::onError)
                            .addOnSuccessListener(taskSnapshot -> e.onComplete());
                } catch (FileNotFoundException err) {
                    e.onError(err);
                }
            }
        });
    }

    private Completable updateProfileImageUrl() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return Completable.complete();
        }

        return Completable.create(e -> {
            String url = FirebaseStorageContants.PROFILE_IMAGES_PATH + firebaseUser.getEmail();
            StorageReference storageReference = firebaseStorage.getReference();
            storageReference = storageReference.child(url);
            storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    registeredUserRef.child(firebaseUser.getUid()).child("imageUrl").setValue(task.getResult().toString());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(task.getResult())
                            .build();
                    firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    e.onComplete();
                                } else {
                                    e.onError(task.getException());
                                }
                            });
                } else {
                    e.onError(new RuntimeException(task.getException()));
                }
            });
        });
    }

    public Completable removeProfilePicture() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return Completable.create(e -> {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(null)
                        .build();
                firebaseUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                e.onComplete();
                            } else {
                                e.onError(task.getException());
                            }
                        });
            });
        } else {
            return Completable.error(new RuntimeException("User logged out!"));
        }
    }
}
