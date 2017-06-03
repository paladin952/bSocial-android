package com.clpstudio.domainlib.services;

import android.net.Uri;

import com.clpstudio.domainlib.utils.FirebaseStorageContants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 28/05/2017.
 */

public class FirebaseStorageService {
    @Inject
    FirebaseStorage firebaseStorage;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseStorageService() {
    }

    public Single<String> uploadConversationDataAndGetLink(String filename, Uri path, String conversationId) {
        return uploadDataToStorage(filename, path, conversationId)
                .andThen(getDownloadLink(filename, conversationId));
    }

    public Completable uploadDataToStorage(String filename, Uri uri, String conversationId) {
        return Completable.create(e -> {
            File file = new File(uri.getPath());
            StorageReference reference = firebaseStorage.getReference();
            StorageReference imageFileRef = reference.child(FirebaseStorageContants.CONVERSATION_DATA_PATH + conversationId + "/" + filename);
            try {
                InputStream stream = new FileInputStream(file);
                UploadTask uploadTask = imageFileRef.putStream(stream);
                uploadTask
                        .addOnFailureListener(e::onError)
                        .addOnSuccessListener(taskSnapshot -> e.onComplete());
            } catch (FileNotFoundException err) {
                e.onError(err);
            }
        });
    }

    public Single<String> getDownloadLink(String filename, String conversationId) {
        return Single.create(e -> {
            StorageReference reference = firebaseStorage.getReference();
            String path = FirebaseStorageContants.CONVERSATION_DATA_PATH + conversationId + "/" + filename;
            StorageReference storageReference = reference.child(path);
            storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    e.onSuccess(task.getResult().toString());
                } else {
                    e.onError(new RuntimeException(task.getException()));
                }
            });
        });
    }
}
