package com.clpstudio.database.services;

import android.util.Log;

import com.clpstudio.database.dagger.FirebaseModule;
import com.clpstudio.database.firebase.AddValueEventSuccessListener;
import com.clpstudio.database.firebase.FirebaseAddChildAddedListener;
import com.clpstudio.database.models.DbMessageModel;
import com.clpstudio.database.utils.FirebaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 21/05/2017.
 */

public class MessagesService {

    private static final String LOG_TAG = MessagesService.class.getSimpleName();

    @Inject
    @FirebaseModule.Conversations
    DatabaseReference conversationsRef;

    @Inject
    public MessagesService() {
    }


    public Observable<List<DbMessageModel>> getMessages(String conversationId) {
        return getInitialMessages(conversationId).toObservable();
    }

    public Completable sendMessage(String conversationId, DbMessageModel dbMessageModel) {
        return Completable.create(e -> conversationsRef
                .child(conversationId)
                .child(FirebaseConstants.NODE_MESSAGES)
                .push()
                .setValue(dbMessageModel, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(LOG_TAG, "Send dbMessageModel successful!");
                        e.onError(databaseError.toException());
                    } else {
                        Log.d(LOG_TAG, "Send dbMessageModel failed!");
                        e.onComplete();
                    }
                }));
    }

    public Observable<DbMessageModel> subscribeMessageAdded(String conversationId) {
        return Observable.create(e -> conversationsRef
                .child(conversationId)
                .child(FirebaseConstants.NODE_MESSAGES)
                .addChildEventListener(new FirebaseAddChildAddedListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(LOG_TAG, "New dbMessageModel received!");
                        DbMessageModel dbMessageModel;

                        dbMessageModel = dataSnapshot.getValue(DbMessageModel.class);
                        if (dbMessageModel != null) {
                            Log.d(LOG_TAG, "New dbMessageModel = " + dbMessageModel.toString());
                            dbMessageModel.setConversationId(conversationId);
                            e.onNext(dbMessageModel);
                        }
                    }
                }));
    }

    private List<DbMessageModel> collectMessages(DataSnapshot dataSnapshot) {
        List<DbMessageModel> data = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            DbMessageModel dbMessageModel = ds.getValue(DbMessageModel.class);
            data.add(dbMessageModel);
        }
        return data;
    }

    private Single<List<DbMessageModel>> getInitialMessages(String conversationId) {
        return Single.create(e -> conversationsRef
                .child(conversationId)
                .child(FirebaseConstants.NODE_MESSAGES)
                .orderByChild(FirebaseConstants.FIELD_CONVERSATION_TIMESTAMP)
                .addValueEventListener(new AddValueEventSuccessListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        e.onSuccess(collectMessages(dataSnapshot));
                    }
                }));
    }
}
