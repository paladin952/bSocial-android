package com.clpstudio.bsocial.bussiness.service;

import com.clpstudio.bsocial.core.dagger.FirebaseModule;
import com.clpstudio.bsocial.core.firebase.AddValueEventSuccessListener;
import com.clpstudio.bsocial.data.models.conversations.ConversationModel;
import com.clpstudio.bsocial.data.models.firebase.MemberActive;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by clapalucian on 5/6/17.
 */

public class ConversationService {


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

    public Single<String> createConversation(RegisteredUser friend) {
        return Single.create(e -> {
            String conversationId = conversationsRef.push().getKey();
            ConversationModel conversationModel = new ConversationModel(conversationId, "Test", "");
            conversationsRef.child(conversationId).setValue(conversationModel);
            String photo = firebaseAuth.getCurrentUser().getPhotoUrl() != null ? firebaseAuth.getCurrentUser().getPhotoUrl().toString() : "";
            RegisteredUser currentUser = new RegisteredUser(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getEmail(), photo);
            membersRef.child(conversationId).child(currentUser.getUserId()).setValue(new MemberActive(true));
            membersRef.child(conversationId).child(friend.getUserId()).setValue(new MemberActive(true));
            e.onSuccess(conversationId);
        });
    }

    public Single<List<ConversationModel>> getListOfConversations() {
        return getListOfConversationsNoMembers()
                .toObservable()
                .flatMap(conversationModel -> {
                    List<Observable<ConversationModel>> streams = new ArrayList<>();
                    for (ConversationModel model : conversationModel) {
                        streams.add(getMembersOfConversation(model));
                    }

                    return Observable.merge(streams);
                })
                .toList();
    }

    private Single<List<ConversationModel>> getListOfConversationsNoMembers() {
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

    private Observable<ConversationModel> getMembersOfConversation(ConversationModel conversationModel) {
        return Observable.create(e -> membersRef.child(conversationModel.getId()).orderByKey().addValueEventListener(new AddValueEventSuccessListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> membersIds = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    membersIds.add(ds.getKey());
                }
                conversationModel.setMembersIds(membersIds);
                e.onNext(conversationModel);
                e.onComplete();
            }
        }));
    }
}
