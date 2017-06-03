package com.clpstudio.domainlib.services;

import android.util.Log;

import com.clpstudio.domainlib.dagger.FirebaseModule;
import com.clpstudio.domainlib.firebase.AddValueEventSuccessListener;
import com.clpstudio.domainlib.firebase.FirebaseAddChildAddedListener;
import com.clpstudio.domainlib.models.ConversationModel;
import com.clpstudio.domainlib.models.RegisteredUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.clpstudio.domainlib.utils.FirebaseConstants.NODE_CONVERSATIONS;
import static com.clpstudio.domainlib.utils.FirebaseConstants.NODE_MEMBERS;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class ConversationService {

    @Inject
    @FirebaseModule.Conversations
    DatabaseReference conversationsRef;

    @Inject
    @FirebaseModule.Users
    DatabaseReference usersRef;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    public ConversationService() {
    }

    public Observable<Boolean> subscribeConversationAdded() {
        return Observable.create(e -> conversationsRef
                .addChildEventListener(new FirebaseAddChildAddedListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("bSocial", "New Conversation added!");
                        ConversationModel conversationModel = dataSnapshot.getValue(ConversationModel.class);
                        if (conversationModel != null) {

                            e.onNext(true);
                        }
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
