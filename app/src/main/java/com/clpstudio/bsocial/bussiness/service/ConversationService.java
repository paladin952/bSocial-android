package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.data.models.conversations.ConversationNameModel;
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
//        Observable<List<Message>> observable = Observable.create(e -> messagesRef.child(conversationId).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                List<Message> data = collectMessages(conversationId, dataSnapshot);
//                e.onNext(data);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        }));
        return getInitialMessages(conversationId).toObservable();
//        return observable;
    }

    public Completable sendMessage(String conversationId, Message message) {
        return Completable.create(e -> messagesRef.child(conversationId).push().setValue(message, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                e.onError(databaseError.toException());
            } else {
                e.onComplete();
            }
        }));
    }

    public Single<List<ConversationNameModel>> getListOfConversations() {
        return Single.create(e -> {
            List<ConversationNameModel> data = new ArrayList<>();
            data.add(new ConversationNameModel("Lucian Clapa", ""));
            data.add(new ConversationNameModel("Lucian", ""));
            data.add(new ConversationNameModel("Clapa", ""));
            data.add(new ConversationNameModel("Ioana minzat", ""));
            data.add(new ConversationNameModel("blabla", ""));
            e.onSuccess(data);
        });
    }

}
