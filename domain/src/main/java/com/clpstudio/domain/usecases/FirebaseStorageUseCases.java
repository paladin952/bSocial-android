package com.clpstudio.domain.usecases;

import android.net.Uri;

import com.clpstudio.database.services.FirebaseStorageService;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 04/06/2017.
 */

public class FirebaseStorageUseCases {

    @Inject
    FirebaseStorageService firebaseStorageService;

    @Inject
    public FirebaseStorageUseCases() {
    }


    public Single<String> uploadConversationDataAndGetLink(String filename, Uri path, String conversationId) {
        return firebaseStorageService.uploadConversationDataAndGetLink(filename, path, conversationId);
    }

    public Completable uploadDataToStorage(String filename, Uri uri, String conversationId) {
        return firebaseStorageService.uploadDataToStorage(filename, uri, conversationId);
    }

    public Single<String> getDownloadLink(String filename, String conversationId) {
        return firebaseStorageService.getDownloadLink(filename, conversationId);
    }

}
