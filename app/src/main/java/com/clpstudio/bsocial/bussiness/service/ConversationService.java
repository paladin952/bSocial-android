package com.clpstudio.bsocial.bussiness.service;

import android.util.Log;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.core.firebase.FirebaseOdChildAddedListener;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.data.models.conversations.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationService {

    private static final String DB_FIELD_TIMESTAMP = "timestamp";
    private static final String LOG_TAG = ConversationService.class.getSimpleName();

    @Inject
    @FirebaseModule.Messages
    DatabaseReference messagesRef;

    @Inject
    public ConversationService() {
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
        return Single.create(e -> messagesRef.child(conversationId).orderByChild(DB_FIELD_TIMESTAMP).addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                e.onSuccess(collectMessages(dataSnapshot));
            }
        }));
    }

    public Observable<List<Message>> getMessages(String conversationId) {
        return getInitialMessages(conversationId).toObservable();
    }

    public Completable sendMessage(String conversationId, Message message) {
        return Completable.create(e -> messagesRef.child(conversationId).push().setValue(message, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.d(LOG_TAG, "Send message successful!");
                e.onError(databaseError.toException());
            } else {
                Log.d(LOG_TAG, "Send message failed!");
                e.onComplete();
            }
        }));
    }

    public Single<List<ConversationModel>> getListOfConversations() {
        return Single.create(e -> {
            List<ConversationModel> data = new ArrayList<>();
            data.add(new ConversationModel("zxz", "Lucian Clapa", ""));
            data.add(new ConversationModel("adsadsa", "Lucian", ""));
            data.add(new ConversationModel("Asdsa", "Clapa", ""));
            data.add(new ConversationModel("dsadsadsa", "Ioana minzat", ""));
            data.add(new ConversationModel("asdasdsa", "blabla", ""));
            e.onSuccess(data);
        });
    }

    public Observable<Message> subscribeMessageAdded(String conversationId) {
        return Observable.create(e -> messagesRef.child(conversationId).addChildEventListener(new FirebaseOdChildAddedListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "New message received!");
                Message message;
                message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    Log.d(LOG_TAG, "New message = " + message.toString());
                    e.onNext(message);
                } else {
                    e.onError(new RuntimeException("Something went wrong"));
                }
            }
        }));
    }

}
