package com.clpstudio.domainlib.services;

import android.util.Log;

import com.clpstudio.domainlib.dagger.FirebaseModule;
import com.clpstudio.domainlib.firebase.AddValueEventSuccessListener;
import com.clpstudio.domainlib.firebase.FirebaseAddChildAddedListener;
import com.clpstudio.domainlib.models.Message;
import com.clpstudio.domainlib.utils.FirebaseConstants;
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


    public Observable<List<Message>> getMessages(String conversationId) {
        return getInitialMessages(conversationId).toObservable();
    }

    public Completable sendMessage(String conversationId, Message message) {
        return Completable.create(e -> conversationsRef
                .child(conversationId)
                .child(FirebaseConstants.NODE_MESSAGES)
                .push()
                .setValue(message, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(LOG_TAG, "Send message successful!");
                        e.onError(databaseError.toException());
                    } else {
                        Log.d(LOG_TAG, "Send message failed!");
                        e.onComplete();
                    }
                }));
    }

    public Observable<Message> subscribeMessageAdded(String conversationId) {
        return Observable.create(e -> conversationsRef
                .child(conversationId)
                .child(FirebaseConstants.NODE_MESSAGES)
                .addChildEventListener(new FirebaseAddChildAddedListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(LOG_TAG, "New message received!");
                        Message message;

                        message = dataSnapshot.getValue(Message.class);
                        if (message != null) {
                            Log.d(LOG_TAG, "New message = " + message.toString());
                            e.onNext(message);
                        }
                    }
                }));
    }

    private List<Message> collectMessages(DataSnapshot dataSnapshot) {
        List<Message> data = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Message message = ds.getValue(Message.class);
            data.add(message);
        }
        return data;
    }

    private Single<List<Message>> getInitialMessages(String conversationId) {
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
