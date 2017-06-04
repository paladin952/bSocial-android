package com.clpstudio.database.services;

import android.net.Uri;

import com.clpstudio.database.utils.FirebaseStorageContants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

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

    public Single<String> uploadConversationDataAndGetLink(String filename, Uri uri, String conversationId) {
        return uploadDataToStorage(filename, uri, conversationId)
                .andThen(getDownloadLink(filename, conversationId));
    }

    private Completable uploadDataToStorage(String filename, Uri uri, String conversationId) {
        File file = new File(uri.toString());
        return Completable.create(e -> {
            StorageReference reference = firebaseStorage.getReference();
            StorageReference imageFileRef = reference.child(FirebaseStorageContants.CONVERSATION_DATA_PATH + conversationId + "/" + filename);
            UploadTask uploadTask = imageFileRef.putFile(Uri.parse(file.getAbsolutePath()));
            uploadTask
                    .addOnFailureListener(e::onError)
                    .addOnSuccessListener(taskSnapshot -> e.onComplete());
        });
    }

    private Single<String> getDownloadLink(String filename, String conversationId) {
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
