package com.clpstudio.bsocial.core.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by clapalucian on 19/05/2017.
 */

public abstract class FirebaseAddChildAddedListener implements ChildEventListener {
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //override if needed
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        //override if needed
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        //override if needed
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //override if needed
    }
}
