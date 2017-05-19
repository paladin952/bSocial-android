package com.clpstudio.bsocial.data.models.ui;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by clapalucian on 16/05/2017.
 */

@Parcel
public class FriendsItemModel {

    String email;

    public FriendsItemModel() {
    }

    @ParcelConstructor
    public FriendsItemModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
