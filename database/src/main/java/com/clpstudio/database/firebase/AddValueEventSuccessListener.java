package com.clpstudio.database.firebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by clapalucian on 16/05/2017.
 */

public abstract class AddValueEventSuccessListener implements ValueEventListener {

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //override if needed
    }
}
