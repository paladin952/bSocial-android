package com.clpstudio.bsocial.bussiness.service;

import android.util.Log;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.core.firebase.FirebaseConstants;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

import static com.clpstudio.bsocial.core.firebase.FirebaseConstants.NODE_CONVERSATIONS;
import static com.clpstudio.bsocial.core.firebase.FirebaseConstants.NODE_MEMBERS;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationService {


    private static final String LOG_TAG = ConversationService.class.getSimpleName();

    @Inject
    @FirebaseModule.Conversations
    DatabaseReference conversationsRef;

    @Inject
    @FirebaseModule.Users
    DatabaseReference usersRef;

    @Inject
    FirebaseAuth firebaseAuth;

    private CompositeDisposable compositeDisposable;

    @Inject
    public ConversationService() {
    }

    public Observable<Boolean> subscribeConversationAdded() {
        return Observable.create(e -> usersRef
                .child(firebaseAuth.getCurrentUser().getUid())
                .child(FirebaseConstants.NODE_CONVERSATIONS)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("luci", "onChildAdded");
                        Log.d("luci", dataSnapshot.toString());
                        String userId = dataSnapshot.getKey();
                        if (firebaseAuth.getCurrentUser().getUid().equals(userId)) {
                            Log.d("luci", "onChildAdded current user");
                            e.onNext(true);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("luci", "onChildChanged");
                        Log.d("luci", dataSnapshot.toString());
                        String userId = dataSnapshot.getKey();
                        if (firebaseAuth.getCurrentUser().getUid().equals(userId)) {
                            Log.d("luci", "onChildChanged current user");
                            e.onNext(true);
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }));
    }

    public Single<String> createConversation(RegisteredUser friend) {
        return Single.create(e -> {
            String conversationId = conversationsRef.push().getKey();
            ConversationModel conversationModel = new ConversationModel(conversationId, "Test", "");

            //add data into conversations node
            conversationsRef.child(conversationId).setValue(conversationModel);
            conversationsRef.child(conversationId).child(NODE_MEMBERS).child(firebaseAuth.getCurrentUser().getUid()).setValue(true);
            conversationsRef.child(conversationId).child(NODE_MEMBERS).child(friend.getUserId()).setValue(true);

            //add data into users node
            usersRef.child(firebaseAuth.getCurrentUser().getUid()).child(NODE_CONVERSATIONS).child(conversationId).setValue(true);
            usersRef.child(friend.getUserId()).child(NODE_CONVERSATIONS).child(conversationId).setValue(true);
            e.onSuccess(conversationId);
        });
    }

    public Single<List<ConversationModel>> getListOfConversations() {
        return getConversationIdsOfCurrentUser()
                .toObservable()
                .map(strings -> {
                    List<Observable<ConversationModel>> conversationsSingles = new ArrayList<>();
                    for (String id : strings) {
                        conversationsSingles.add(getConversation(id));
                    }
                    return conversationsSingles;
                })
                .flatMap(observables -> Observable.fromIterable(observables)
                        .flatMap(conversationModelObservable -> conversationModelObservable))
                .toList();
    }

    private Observable<ConversationModel> getConversation(String id) {
        return Observable.create(e -> conversationsRef.child(id).addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConversationModel model = dataSnapshot.getValue(ConversationModel.class);
                if (model != null) {
                    List<String> memberIds = new ArrayList<>();
                    for (DataSnapshot membersSnapshot : dataSnapshot.child(NODE_MEMBERS).getChildren()) {
                        String id = membersSnapshot.getKey();
                        boolean isVisible = membersSnapshot.getValue(Boolean.class);
                        if (isVisible) {
                            memberIds.add(id);
                        }
                    }
                    if (!memberIds.isEmpty()) {
                        model.setMembersIds(memberIds);
                    }
                    e.onNext(model);
                    e.onComplete();
                } else {
                    e.onComplete();
                }
            }
        }));
    }

    private Single<List<String>> getConversationIdsOfCurrentUser() {
        return Single.create(e -> {
            String userId = firebaseAuth.getCurrentUser().getUid();
            usersRef.child(userId).child(NODE_CONVERSATIONS).orderByKey().addValueEventListener(new AddValueEventSuccessListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> data = new ArrayList<>();
                    for (DataSnapshot conversationsSnapshot : dataSnapshot.getChildren()) {
                        String key = conversationsSnapshot.getKey();
                        boolean isVisible = conversationsSnapshot.getValue(Boolean.class);
                        if (isVisible) {
                            data.add(key);
                        }
                    }
                    e.onSuccess(data);
                }
            });
        });
    }
}
