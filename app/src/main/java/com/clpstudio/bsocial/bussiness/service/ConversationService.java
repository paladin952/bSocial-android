package com.clpstudio.bsocial.bussiness.service;

import android.util.Log;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.core.firebase.FirebaseOdChildAddedListener;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.data.models.conversations.Message;
import com.clpstudio.bsocial.data.models.firebase.MemberActive;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.google.firebase.auth.FirebaseAuth;
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
    private static final String DB_FIELD_EMAIL = "email";
    private static final String LOG_TAG = ConversationService.class.getSimpleName();

    @Inject
    @FirebaseModule.Messages
    DatabaseReference messagesRef;

    @Inject
    @FirebaseModule.Conversations
    DatabaseReference conversationsRef;

    @Inject
    @FirebaseModule.Members
    DatabaseReference membersRef;

    @Inject
    FirebaseAuth firebaseAuth;

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

    public Single<String> createConversation(RegisteredUser friend) {
        return Single.create(e -> {
            String conversationId = conversationsRef.push().getKey();
            ConversationModel conversationModel = new ConversationModel(conversationId, "", "");
            conversationsRef.child(conversationId).setValue(conversationModel);

            RegisteredUser currentUser = new RegisteredUser(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getEmail());
            membersRef.child(conversationId).child(currentUser.getUserId()).setValue(new MemberActive(true));
            membersRef.child(conversationId).child(friend.getUserId()).setValue(new MemberActive(true));
            e.onSuccess(conversationId);
        });
    }

    public Single<List<ConversationModel>> getListOfConversations() {
        return getConversationIdsOfCurrentUser()
                .toObservable()
                .flatMap(strings -> {
                    List<Observable<ConversationModel>> conversationsSingles = new ArrayList<>();
                    for (String id : strings) {
                        conversationsSingles.add(getConversation(id));
                    }

                    return Observable.fromIterable(conversationsSingles)
                            .flatMap(conversationModelObservable -> conversationModelObservable);
                })
                .toList();
    }

    private Observable<ConversationModel> getConversation(String id) {
        return Observable.create(e -> conversationsRef.child(id).addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConversationModel model = dataSnapshot.getValue(ConversationModel.class);
                e.onNext(model);
                e.onComplete();
            }
        }));
    }

    private Single<List<String>> getConversationIdsOfCurrentUser() {
        return Single.create(e -> {
            String userId = firebaseAuth.getCurrentUser().getUid();
            membersRef.orderByKey().addValueEventListener(new AddValueEventSuccessListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> data = new ArrayList<>();
                    for (DataSnapshot conversationsSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot userIdsSnapshot : conversationsSnapshot.getChildren()) {
                            String key = userIdsSnapshot.getKey();
                            if (key.equals(userId)) {
                                data.add(conversationsSnapshot.getKey());
                            }
                        }
                    }
                    e.onSuccess(data);
                }
            });
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
                }
            }
        }));
    }

}
