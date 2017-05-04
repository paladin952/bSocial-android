package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.bussiness.utils.FirebaseStorageContants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

public class UploadProfilePhotoService {

    @Inject
    FirebaseStorage firebaseStorage;
    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    public UploadProfilePhotoService() {
    }

    public Completable upload(File file) {
        return firebaseUpload(file)
                .andThen(uploadUrlToProfile());
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

    private Completable uploadUrlToProfile() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return Completable.complete();
        }

        return Completable.create(e -> {
            String url = FirebaseStorageContants.PROFILE_IMAGES_PATH + firebaseUser.getEmail();
            StorageReference storageReference = firebaseStorage.getReference();
            storageReference = storageReference.child(url);
            storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
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
}
