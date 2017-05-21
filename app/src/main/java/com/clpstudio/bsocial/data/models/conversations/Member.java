package com.clpstudio.bsocial.data.models.conversations;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by clapalucian on 20/05/2017.
 */

@Parcel
public class Member {

    String email;

    public Member() {
    }

    @ParcelConstructor
    public Member(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}