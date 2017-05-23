package com.clpstudio.bsocial.data.models.firebase;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by clapalucian on 16/05/2017.
 */

@Parcel
public class RegisteredUser {

    String userId;
    String email;
    String imageUrl;

    public RegisteredUser() {

    }

    @ParcelConstructor
    public RegisteredUser(String userId, String email, String imageUrl) {
        this.email = email;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
